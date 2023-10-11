package com.atguigu.gulimall.order.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class OrderConfirmVo {

    //收货地址列表：ums_member_receive_address
    private List<MemberAddressVo> address;

    //购物车所有选中的购物项
    private List<OrderItemVo> items;

    //会员积分
    private Integer integration;

    //订单总额
    @Getter(AccessLevel.NONE)
    private BigDecimal total;

    //应付价格
    @Getter(AccessLevel.NONE)
    private BigDecimal payPrice;

    //防重复提交令牌
    private String orderToken;

    //库存信息
    Map<Long,Boolean> stock;

    //发票记录、优惠劵信息【暂时不记】



    public Integer getCount(){
        Integer i=0;
        if(items!=null)
            for(OrderItemVo item:items){
               i+=item.getCount();
            }
        return i;
    }

    public BigDecimal getTotal(){
        BigDecimal sum = new BigDecimal("0");
        if(items!=null)
        for(OrderItemVo item:items){
            BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
            sum = sum.add(multiply);
        }
        return sum;
    }

    public BigDecimal getPayPrice(){
        BigDecimal sum = new BigDecimal("0");
        if(items!=null)
            for(OrderItemVo item:items){
                BigDecimal multiply = item.getPrice().multiply(new BigDecimal(item.getCount().toString()));
                sum = sum.add(multiply);
            }
        return sum;
    }

}
