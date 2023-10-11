package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * 库存的锁定结果
 */
@Data
public class LockStockResult {
    private Long skuId;     //锁定库存的id
    private Integer num;
    private Boolean locked;
}
