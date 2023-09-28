package com.atguigu.gulimall.order.service.impl;

import com.atguigu.gulimall.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.order.dao.OrderItemDao;
import com.atguigu.gulimall.order.entity.OrderItemEntity;
import com.atguigu.gulimall.order.service.OrderItemService;


@Service("orderItemService")
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    //测试监听端口

    /**
     *参数类型：
     * ① Message:原生消息详细信息。头+体
     * ② T<发送的消息类型>
     * ③ Channel 当前传输数据的通道
     *
     * Queue:可以多人监听。只要收到消息，队列就会自动删除消息，只能有一个人收到此消息
     * 场景：
     *  1）订单服务启动多个；同一个消息，只能有一个客户端收到【竞争关系】
     *  2）只有一个消息完全处理结束，才能接收下一个消息
     *  3）@RabbitListener: 类+方法 （监听哪些队列即可）
     *      @RabbitHander: 标在方法上（重载）
     */
//    @RabbitListener(queues = {"hello-java-queue"})
    public void recieveMessage(Message message, OrderReturnReasonEntity content, Channel channel){
        //消息体
        byte[] body = message.getBody();
        //消息头【消息属性信息】
        message.getMessageProperties();
        //消息
        System.out.println("接收到测试消息...."+body+"===》类型"+message.getClass()+"==>内容"+content);
        //--- 通过ack模式签收
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        System.out.println("deliveryTag==》"+deliveryTag);

        //签收货物，非批量模式
        try{
           if(deliveryTag%2==0){
               //收货
               channel.basicAck(deliveryTag,false);
           }else{
               //退货
               //requeue=false 丢弃  requeue=true 发回服务器，重新入队
               channel.basicNack(deliveryTag,false,false);
           }
        }catch (Exception e){
            //网络中断
        }
    }

}