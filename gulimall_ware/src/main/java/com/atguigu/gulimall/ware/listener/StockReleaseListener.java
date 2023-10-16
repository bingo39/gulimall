package com.atguigu.gulimall.ware.listener;


import com.atguigu.common.to.mq.OrderTo;
import com.atguigu.common.to.mq.StockLockedTo;
import com.atguigu.gulimall.ware.service.WareSkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RabbitListener(queues = "stock.release.stock.queue")
@Component
public class StockReleaseListener {

    @Autowired
    WareSkuService wareSkuService;

    /**
     * 事务补偿方案
     * 库存自动解锁  -- 上游：stock-event-exchange
     * 触发场景：下单成功，库存锁定成功，接下来业务调用失败，导致订单回滚。之前锁定的库存就要自动解锁
     * 其中，解锁还需注意：查询wms_ware_order表是否有该订单生成：
     *      1. 没有该订单，必须解锁
     *      2. 有该订单，不能解锁
     *          其中订单状态： 1.已取消，解锁库存
     *                      2.没取消，不能解锁
     * 补充：
     * 只要解锁库存的消息失败，一定要告诉服务解锁失败
     */
    @RabbitHandler
    public void handleStockLockedRelease(StockLockedTo to , Channel channel, Message message) throws IOException {

        System.out.println("收到解锁库存的消息");
        try{
            wareSkuService.unlockStock(to);
            //手动删除消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        }catch (Exception e){
            //处理解锁操作成功，手动ack给rabbitmq回复可以删除消息【防止延迟队列消息给删除，而数据库需要解锁的时候找不到】
            channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
        }

    }

    /**
     * 库存被动解锁  -- 上游：order-event-exchange【也可以是stock-event-exchange，但也要求是order-event-exchange发送过来再转发】
     * 触发条件：下单未支付，超过指定时间自动释放订单完成后，order服务发送消息给mq后被ware服务所监听
     */
    @RabbitHandler
    public void handleOrderCloseRelease(Message message, Channel channel, OrderTo orderTo) throws IOException {
        System.out.println("订单关闭，准备解锁库存");
       try{
           wareSkuService.unlockStock(orderTo);
           //手动删除消息
           channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
       }catch (Exception e){
           channel.basicReject(message.getMessageProperties().getDeliveryTag(),true);
       }
    }

}
