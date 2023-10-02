package com.atguigu.gulimall.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车的购物项
 */
@Data
public class OrderItemVo {
    private Long skuId;     //id
    private String title;   //标题
    private String image;   //图片信息
    private List<String> skuAttr;     //套餐属性
    private BigDecimal price;       //价格
    private Integer count;      //数量
    private BigDecimal weight;      //重量
}
