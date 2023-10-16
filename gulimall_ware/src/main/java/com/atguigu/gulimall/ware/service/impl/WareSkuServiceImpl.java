package com.atguigu.gulimall.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.exception.NoStockException;
import com.atguigu.common.to.mq.OrderTo;
import com.atguigu.common.to.mq.StockDetailTo;
import com.atguigu.common.to.mq.StockLockedTo;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.ware.entity.WareOrderTaskDetailEntity;
import com.atguigu.gulimall.ware.entity.WareOrderTaskEntity;
import com.atguigu.gulimall.ware.feign.OrderFeignService;
import com.atguigu.gulimall.ware.feign.ProductFeignService;
import com.atguigu.gulimall.ware.service.WareOrderTaskDetailService;
import com.atguigu.gulimall.ware.service.WareOrderTaskService;
import com.atguigu.gulimall.ware.vo.*;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.ware.dao.WareSkuDao;
import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.atguigu.gulimall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
@RabbitListener(queues = "stock.release.stock.queue")       //解锁操作【事务补偿方案】
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private WareSkuDao wareSkuDao;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private WareOrderTaskService wareOrderTaskService;
    @Autowired
    private WareOrderTaskDetailService wareOrderTaskDetailService;
    @Autowired
    private OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         *根据 skuId & wareId 查询
         */
        QueryWrapper<WareSkuEntity> wareSkuEntityQueryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(skuId)) {
            wareSkuEntityQueryWrapper.eq("sku_id", skuId);
        }
        if (!StringUtils.isEmpty(wareId)) {
            wareSkuEntityQueryWrapper.eq("ware_id", wareId);
        }


        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wareSkuEntityQueryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        // 判断是否已经存在库存记录 ？ 更新操作 ：新增操作
        List<WareSkuEntity> wareSkuEntities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (wareSkuEntities == null || wareSkuEntities.size() == 0) {
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            //默认锁定库存为0
            wareSkuEntity.setStockLocked(0);
            //查询商品名称
            //TODO 用什么办法让异常出现以后不回滚（try...catch是一种方式，高级部分有另外一种方式）
            try {
                // 就算没有获取到商品名，也无需回滚事务。没了就没了
                R info = productFeignService.info(skuId);
                Map<String, Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                if (info.getCode() == 0) {
                    wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
                }
            } catch (Exception e) {

            }
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }
    }

    @Override
    public List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds) {
        List<SkuHasStockVo> collect = skuIds.stream().map(skuId -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            //检索是否有库存
            //查询当前sku的总库存量  : 库存总量 - 占用库存（下单未支付）
            //sql：SELECT SUM(stock - stock_locked) FROM `wms_ware_sku` WHERE sku_id=?
            Long count = baseMapper.getSkuStock(skuId);
            skuHasStockVo.setHasStock(count == null ? false : count > 0);
            skuHasStockVo.setSkuId(skuId);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Transactional(rollbackFor = NoStockException.class)
    @Override
    public Boolean orderLockStock(WareSkuLockVo vo) {

        /**
         * 保存库存工作单的详情
         * 追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskService.save(wareOrderTaskEntity);

        //1.按照下单的收货地址，找到一个就近的仓库，锁定库存

        //2.找到每个商品在哪个仓库都有库存
        List<OrderItemVo> lock = vo.getLock();
        List<SkuWareHasStock> collect = lock.stream().map(item -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪里有库存
            List<Long> wareIds = wareSkuDao.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIds);
            return stock;
        }).collect(Collectors.toList());

        Boolean allLock = true;
        //3.锁定库存
        for(SkuWareHasStock hasStock:collect){
            Boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();
            if(wareIds ==null && wareIds.size()==0){
                //没有任何仓库有这个商品的库存
                throw new NoStockException(skuId);
            }
            /**
             * ① 如果每一个商品都锁定成功，将当前商品锁定了几件发送给rabbitmq
             * ② 锁定失败。前面保存的工作单信息就回滚，发送的消息，即使要解锁记录，由于去数据库查不到id，所以不用解锁
             */
            for (Long wareId:wareIds){
                //锁定成功返回1，失败返回0
                Long count = wareSkuDao.lockSkuStock(skuId, wareId, hasStock.getNum());
                if(count==1){
                    skuStocked = true;
                    //告诉MQ库存锁定成功【后面延迟队列会查询wms_ware_order_task_detail表，表有数据说明锁定成功】
                    WareOrderTaskDetailEntity wareOrderTaskDetailEntity = new WareOrderTaskDetailEntity(null,skuId,null,hasStock.getNum(),wareOrderTaskEntity.getId(),wareId,1);
                    wareOrderTaskDetailService.save(wareOrderTaskDetailEntity);
                    StockLockedTo stockLockedTo = new StockLockedTo();
                    stockLockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo stockDetailTo = new StockDetailTo();
                    BeanUtils.copyProperties(wareOrderTaskDetailEntity,stockDetailTo);
                    //防止回滚之后没有属性
                    stockLockedTo.setStockDetailTo(stockDetailTo);
                    //发送消息给mq
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",stockLockedTo);
                    break;
                }else{
                    //TODO 当前仓库锁失败，重试下一个仓库
                }
            }
            if(skuStocked == false){
                //当前商品所有仓库都没锁住
                throw new NoStockException(skuId);
            }
        }

        //全部都是锁定成功的
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {
            Long id = to.getId();   //库存工作单的id
            StockDetailTo stockDetailTo = to.getStockDetailTo();        //订单任务表wms_ware_order_task
            //查询数据库关于该订单的库存信息。如果有，表示为上述场景；如果没有，则是整个锁库存操作发生意外，无需解锁【该情况数据库会自动回滚，再进行补偿stock_locked字段就错了】
            WareOrderTaskDetailEntity orderTaskDetailEntity = wareOrderTaskDetailService.getById(stockDetailTo.getId());     //即wms_ware_order_task表是否有该记录
            if(orderTaskDetailEntity!=null){
                //解锁
                //获取订单信息
                WareOrderTaskEntity taskEntity = wareOrderTaskService.getById(id);
                String orderSn = taskEntity.getOrderSn();       //根据订单号查询订单状态
                R r = orderFeignService.getOrderStatus(orderSn);
                if(r.getCode()==0){
                    //订单数据返回成功
                    OrderVo data = r.getData("data",new TypeReference<OrderVo>() {});
                    if(data==null || data.getStatus()==4){
                        //订单不存在 && 订单已经被取消了，可以进行解锁操作
                        if(orderTaskDetailEntity.getLockStatus() == 1){
                            //当前库存工作单详情，状态1 已锁定但是未解锁才可以解锁
                            unLockStock(stockDetailTo.getSkuId(),stockDetailTo.getWareId(),stockDetailTo.getSkuNum(),stockDetailTo.getId());
                        }
                    }
                }else {
                    //消息拒绝以后重新放在队列里面，让别人继续消费解锁
                    //远程调用服务失败
                    throw new RuntimeException("远程服务失败");
                }
            }

    }

    /**
     * 业务场景描述：
     * 如果订单创建过程中，出现机器卡顿等原因，没能及时修改掉`oms.order`表中的`status`字段状态，而库存解锁操作此时查询发现状态没改便将消息返回给mq。
     * 后续`status`字段修改状态同时也删除了`order.release.order.queue`队列中的消息。造成了库存解锁操作将永远无法触发。
     */
    @Transactional
    @Override
    public void unlockStock(OrderTo to) throws IOException {
        String orderSn = to.getOrderSn();
        //查一下最新库存的状态，防止重复解锁库存【有点多余】
        WareOrderTaskEntity wareOrderTaskEntity = wareOrderTaskService.getOrderTaskByOrderSn(orderSn);
        Long id = wareOrderTaskEntity.getId();
            //按照工作单找到所有 ”没有解锁的库存“ 进行解锁
        List<WareOrderTaskDetailEntity> entities = wareOrderTaskDetailService.list(new QueryWrapper<WareOrderTaskDetailEntity>()
                .eq("task_id", id)
                .eq("lock_status", 1));
        for (WareOrderTaskDetailEntity entity:entities){
            unLockStock(entity.getSkuId(),entity.getWareId(),entity.getSkuNum(),entity.getId());
        }
    }

    /**
     * 解锁库存
     */
    private void unLockStock(Long skuId,Long wareId,Integer num,Long taskDetailId){
        //库存解锁
        wareSkuDao.unlockStock(skuId,wareId,num);
        //更新工作单的状态为已解锁
        WareOrderTaskDetailEntity orderTaskDetailEntity = new WareOrderTaskDetailEntity();
        orderTaskDetailEntity.setId(taskDetailId);
        orderTaskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(orderTaskDetailEntity);

    }

    @Data
    class SkuWareHasStock{
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }

}