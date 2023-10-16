package com.atguigu.gulimall.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.exception.NoStockException;
import com.atguigu.common.to.mq.OrderTo;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimall.order.constant.OrderConstant;
import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.atguigu.gulimall.order.enume.OrderStatusEnum;
import com.atguigu.gulimall.order.feign.CartFeignService;
import com.atguigu.gulimall.order.feign.MemberFeignService;
import com.atguigu.gulimall.order.feign.ProductFeignService;
import com.atguigu.gulimall.order.feign.WmsFeignService;
import com.atguigu.gulimall.order.interceptor.LoginUserInterceptor;
import com.atguigu.gulimall.order.service.OrderItemService;
import com.atguigu.gulimall.order.to.OrderCreateTo;
import com.atguigu.gulimall.order.vo.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.order.dao.OrderDao;
import com.atguigu.gulimall.order.entity.OrderEntity;
import com.atguigu.gulimall.order.service.OrderService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> confirmVoThreadLocal = new ThreadLocal<>();

    @Autowired
    MemberFeignService memberFeignService;
    @Autowired
    CartFeignService cartFeignService;
    @Autowired
    ThreadPoolExecutor executor;
    @Autowired
    WmsFeignService wmsFeignService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    ProductFeignService productFeignService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {

        OrderConfirmVo confirmVo = new OrderConfirmVo();
        //会员id可以通过拦截器获取
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();

        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        //异步调用远程服务
        CompletableFuture<Void> addressFuture = CompletableFuture.runAsync(() -> {
            //防止异步情况，获取不到ThreadLocal里的内容
            RequestContextHolder.setRequestAttributes(requestAttributes);
            //1。远程调用收货地址列表
            List<MemberAddressVo> address = memberFeignService.getAddress(memberRespVo.getId());
            confirmVo.setAddress(address);
        }, executor);
        CompletableFuture<Void> cartFuture = CompletableFuture.runAsync(() -> {

            RequestContextHolder.setRequestAttributes(requestAttributes);
            //2.远程查询购物车所有选中的购物项【cart服务从redis中获取的,userId从localThread中获取】
            List<OrderItemVo> items = cartFeignService.getCurrentUserCartItems();
            //feign在远程调用之前要构造请求，调用很多拦截器
            //RequestInterceptor interceptor:requestInterceptors
            confirmVo.setItems(items);
        }, executor).thenRunAsync(()->{
            List<OrderItemVo> items = confirmVo.getItems();
            List<Long> skuIdCollect = items.stream().map(item -> {
                return item.getSkuId();
            }).collect(Collectors.toList());
            R skusHasStock = wmsFeignService.getSkusHasStock(skuIdCollect);
            List<SkuStockVo> data = skusHasStock.getData(new TypeReference<List<SkuStockVo>>() {});
            if(data!=null){
                Map<Long, Boolean> map = data.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                confirmVo.setStock(map);
            }
        },executor);
        //3.查询用户积分
        Integer integration = memberRespVo.getIntegration();
        //4.其他数据后续再补充
        //5.防重令牌
        String token = UUID.randomUUID().toString().replace("-", "");
            //返回给页面
        confirmVo.setOrderToken(token);
            //保存令牌在redis中【key格式：order:token:{userId}】
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX+memberRespVo.getId(),token,30, TimeUnit.MINUTES);


        CompletableFuture.allOf(addressFuture,cartFuture).get();
        confirmVo.setIntegration(integration);
        return confirmVo;
    }

//    @GlobalTransactional
    @Transactional
    @Override
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        SubmitOrderResponseVo responseVo = new SubmitOrderResponseVo();
        responseVo.setCode(0);//有异常code为其他数
        confirmVoThreadLocal.set(vo);

        //拦截器中获取登录用户
        MemberRespVo memberRespVo = LoginUserInterceptor.loginUser.get();
        //下单之后，创建订单、验令牌、验价格、锁库存......
        String orderToken = vo.getOrderToken();
            //① 验证令牌【核心：令牌的对比和删除必须保证原子性】
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";    //lua脚本，保证令牌原子性。即让redis中执行保证事务原子性。
        Long result = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class),
                Arrays.asList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberRespVo.getId()),
                orderToken);
        if(result == 0){
            //令牌验证失败
                //1.创建订单，订单项等信息
            responseVo.setCode(1);
            return responseVo;
        }else {
            //令牌创建成功【下单；去创建订单；验收令牌；锁库存】
                //1.创建订单，订单项等信息
            OrderCreateTo order = createOrder();
                //2.验价
            BigDecimal payAmount = order.getOrderEntity().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            // 差价范围在0.01-1之间
           if( Math.abs(payAmount.subtract(payPrice).doubleValue())<0.01){
               //3.保存订单
               saveOrder(order);
               //4.锁定库存【只要有异常，就回滚所有数据】
                    //锁库存的项：订单号，订单项(skuId,skuName，num)
               WareSkuLockVo lockVo = new WareSkuLockVo();
               lockVo.setOrderSn(order.getOrderEntity().getOrderSn());

               List<OrderItemVo> locks = order.getOrderItems().stream().map(item -> {
                   OrderItemVo orderItemVo = new OrderItemVo();
                   orderItemVo.setSkuId(item.getSkuId());
                   orderItemVo.setCount(item.getSkuQuantity());
                   orderItemVo.setTitle(item.getSkuName());
                   return orderItemVo;
               }).collect(Collectors.toList());
               lockVo.setLock(locks);
               //远程锁库存
               R r = wmsFeignService.orderLockStock(lockVo);
               if(r.getCode() == 0){
                   //锁成功
                   responseVo.setOrderEntity(order.getOrderEntity());
                   //TODO 5.远程扣减积分
//                   int i=10/0;  //模拟“order服务提交成功，但ware服务异常，分布式事务能否正常回滚”
                   //订单创建成功，发送消息给mq
                   rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order.getOrderEntity());
                   return responseVo;
               }else{
                   //锁失败
                   String msg=(String) r.get("msg");
                   throw  new NoStockException(msg);
               }
           }else{
               responseVo.setCode(2);
               return responseVo;
           }
        }
    }

    @Override
    public OrderEntity getOrderByOrderSn(String orderSn) {
        OrderEntity orderEntity = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return orderEntity;
    }

    @Override
    public void closeOrder(OrderEntity entity) {
        //查询当前订单的最新状态
        OrderEntity orderEntity = this.getById(entity.getId());
        if(orderEntity.getStatus() == OrderStatusEnum.CREATE_NEW.getCode()){
            //只有待付款才可以关单
            OrderEntity update = new OrderEntity();
            update.setId(orderEntity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity,orderTo);
            //发送mq,可以执行解锁库存操作
           try{
               //TODO 保证消息一定会发送出去。每一个消息都做好日志记录（给数据库保存每一个消息的详细信息,定期扫描数据库重新发送）
               rabbitTemplate.convertAndSend("order-event-exchange","order.release.order",orderTo);
           }catch (Exception e){
               //将没发送成功的消息重新进行发送 例如：while
           }
        }
    }

    /**
     * 保存订单数据
     * @param order
     */
    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrderEntity();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);


        List<OrderItemEntity> orderItems = order.getOrderItems();
//        orderItems.stream().map(item->{
//            item.setOrderId(orderEntity.getId());
//            //seata与mybatis-plus saveBatch方法有冲突，退而求次
//            orderItemService.save(item);
//            return item;
//        }).collect(Collectors.toList());
        orderItemService.saveBatch(orderItems);

    }

    /**
     * 创建订单
     * @return
     */
    private OrderCreateTo createOrder(){

        OrderCreateTo createTo = new OrderCreateTo();
        //1.创建订单实体类
        OrderEntity orderEntity = buildOrderEntity();
        //2. 获取购物车中所有的订单项
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderEntity.getOrderSn());
        //3.验价【计算价格、积分等相关信息】
        computePrice(orderEntity,orderItemEntities);

        createTo.setOrderEntity(orderEntity);
        createTo.setOrderItems(orderItemEntities);
        return createTo;
    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal("0.0");
        BigDecimal coupon = new BigDecimal("0.0");
        BigDecimal integration = new BigDecimal("0.0");
        BigDecimal promotion = new BigDecimal("0.0");

        BigDecimal gift = new BigDecimal("0.0");
        BigDecimal growth = new BigDecimal("0.0");
        //订单的总额，叠加每一个订单项的总额信息
        for(OrderItemEntity entity:orderItemEntities){
            total = total.add(entity.getRealAmount());
            coupon = coupon.add(entity.getCouponAmount());
            integration = integration.add(entity.getIntegrationAmount());
            promotion = promotion.add(entity.getPromotionAmount());
            gift = gift.add(new BigDecimal(entity.getGiftIntegration().toString()));
            growth = growth.add(new BigDecimal(entity.getGiftGrowth().toString()));


        }
        //① 订单价格相关的
        orderEntity.setTotalAmount(total);
        //② 应付总额
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setIntegrationAmount(integration);
        orderEntity.setPromotionAmount(promotion);

        //③ 设置积分等信息
        orderEntity.setIntegration(gift.intValue());
        orderEntity.setGrowth(growth.intValue());
        orderEntity.setDeleteStatus(0);     //删除状态，0未删除

    }

    /**
     * 创建订单实体类需要的信息
     */
    private OrderEntity buildOrderEntity() {
        OrderEntity orderEntity = new OrderEntity();
        //生成订单号
        String orderSn = IdWorker.getTimeId();
        orderEntity.setOrderSn(orderSn);
        //设置会员Id【也是从拦截器中拿取】
        orderEntity.setMemberId(LoginUserInterceptor.loginUser.get().getId());

        //收获地址信息
        OrderSubmitVo orderSubmitVo = confirmVoThreadLocal.get();
        R fare = wmsFeignService.getFare(orderSubmitVo.getAddrId());
        FareVo fareResp = fare.getData(new TypeReference<FareVo>() {
        });

        //设置运费信息
        orderEntity.setFreightAmount(fareResp.getFare());

        //设置收货人信息
        orderEntity.setReceiverDetailAddress(fareResp.getAddressVo().getDetailAddress());
        orderEntity.setReceiverCity(fareResp.getAddressVo().getCity());
        orderEntity.setReceiverPhone(fareResp.getAddressVo().getPhone());
        orderEntity.setReceiverPostCode(fareResp.getAddressVo().getPostCode());
        orderEntity.setReceiverProvince(fareResp.getAddressVo().getProvince());
        orderEntity.setReceiverRegion(fareResp.getAddressVo().getRegion());

        //设置订单相关状态信息
        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);

        return orderEntity;
    }

    /**
     * 构建所有订单项数据
     */
    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        //最后确定每个购物项的价格 【即实时商品价格更新与购物车内不一致，最后使用的这个价格】
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if(currentUserCartItems.size()>0&& currentUserCartItems!=null){
            List<OrderItemEntity> itemEntities = currentUserCartItems.stream().map(cartItem -> {
                OrderItemEntity orderItemEntity = buildOrderItem(cartItem);
                orderItemEntity.setOrderSn(orderSn);
                return orderItemEntity;
            }).collect(Collectors.toList());

            return itemEntities;
        }
        return null;
    }

    /**
     * 构建某个订单项的内容
     */
    private OrderItemEntity buildOrderItem(OrderItemVo cartItem) {
        OrderItemEntity orderItemEntity = new OrderItemEntity();
        //1.订单信息：订单号
        //2.商品的spu信息
        Long skuId = cartItem.getSkuId();
        R r = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo spuInfoData = r.getData(new TypeReference<SpuInfoVo>() {
        });
        orderItemEntity.setSpuId(spuInfoData.getId());
        orderItemEntity.setSpuBrand(spuInfoData.getBrandId().toString());
        orderItemEntity.setSpuName(spuInfoData.getSpuName());
        orderItemEntity.setCategoryId(spuInfoData.getCatalogId());      //分类


        //3.商品的sku信息
        orderItemEntity.setSkuId(cartItem.getSkuId());
        orderItemEntity.setSkuName(cartItem.getTitle());
        orderItemEntity.setSkuPic(cartItem.getImage());
        orderItemEntity.setSkuPrice(cartItem.getPrice());
        String delimitedString = StringUtils.collectionToDelimitedString(cartItem.getSkuAttr(), ";");//集合转变为字符串
        orderItemEntity.setSkuAttrsVals(delimitedString);
        orderItemEntity.setSkuQuantity(cartItem.getCount());
        //4.优惠信息【暂且忽略】
        //5.积分信息
                 //姑且设定成长值和积分都是与价格成正比
        orderItemEntity.setGiftGrowth(cartItem.getPrice().intValue());
        orderItemEntity.setGiftIntegration(cartItem.getPrice().multiply(new BigDecimal(cartItem.getCount().toString())).intValue());
        //6.订单项的价格信息
                //打折，积分项，优惠劵等暂且姑且都为零
        orderItemEntity.setPromotionAmount(new BigDecimal("0.0"));
        orderItemEntity.setCouponAmount(new BigDecimal("0.0"));
        orderItemEntity.setIntegrationAmount(new BigDecimal("0.0"));
                //当前订单项的实际金额【总额-优惠】
        BigDecimal orign = orderItemEntity.getSkuPrice().multiply(new BigDecimal(orderItemEntity.getSkuQuantity().toString()));
        orign.subtract(orderItemEntity.getPromotionAmount()).subtract(orderItemEntity.getCouponAmount()).subtract(orderItemEntity.getIntegrationAmount());
        orderItemEntity.setRealAmount(orign);


        return orderItemEntity;
    }
}