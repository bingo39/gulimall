package com.atguigu.gulimall.cart.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 整个购物车
 * （需要计算的属性，必须重写get方法）
 */
@Data
public class Cart {

    private List<CartItem> items;

    @Getter(AccessLevel.NONE)
    private Integer countNum;      //商品数量

    @Getter(AccessLevel.NONE)
    private Integer countType;      //商品类型数量

    @Getter(AccessLevel.NONE)
    private BigDecimal totalAmount;     //商品总价

    private BigDecimal reduce = new BigDecimal("0.00");      //减免价格

    public Integer getCountNum() {
        if(items!=null && items.size()>0){
            for(CartItem item:items){
                countNum+=item.getCount();
            }
        }
        return countNum;
    }

    public Integer getCountType() {
        int count=0;
        if(items!=null && items.size()>0){
            for(CartItem item:items){
                count+=1;
            }
        }
        return count;
    }

    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        //1.计算购物项总价
        if(items!=null && items.size()>0){
            for(CartItem item:items){
                BigDecimal totalPrice = item.getTotalPrice();
                amount = amount.add(totalPrice);
            }
        }
        //2.减去优惠总价
        BigDecimal subtract = amount.subtract(getReduce());
        return subtract;
    }

}
