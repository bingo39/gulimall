package com.atguigu.gulimall.cart.vo;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物项
 */
@Data
public class CartItem {
    private Long skuId;     //id

    private Boolean check = true;   //选中项

    private String title;   //标题

    private String image;   //图片信息

    private List<String> skuAttr;     //套餐属性

    private BigDecimal price;       //价格

    private Integer count;      //数量

    @Getter(AccessLevel.NONE)
    private BigDecimal totalPrice;

    public BigDecimal getTotalPrice(){
        return  this.price.multiply(new BigDecimal(""+this.count));
    }
}
