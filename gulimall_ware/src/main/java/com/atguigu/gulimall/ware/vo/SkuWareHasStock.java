package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * 商品有库存的仓库
 */
@Data
public class SkuWareHasStock {
    private Long skuId;
    private List<Long> wareId;
    private Integer num;
}
