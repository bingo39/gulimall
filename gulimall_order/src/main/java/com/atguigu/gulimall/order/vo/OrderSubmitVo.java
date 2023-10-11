package com.atguigu.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 封装订单提交的数据：
 */
@Data
public class OrderSubmitVo {
    private Long addrId;    //地址id
    private Integer payType;  //支付方式


    private String orderToken;  //防重令牌
    private BigDecimal payPrice;    //应该付价格  【购物车内商品价格与实际商品价格对比】
    private String note;    //订单备注
    // 无需提交需要购买的商品，去购物车再获取一遍即可
    //TODO 暂且不实现优惠劵，发票
    //用户相关信息直接去session获取
}
