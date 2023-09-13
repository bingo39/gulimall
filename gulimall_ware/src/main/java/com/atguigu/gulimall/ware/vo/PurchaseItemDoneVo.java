package com.atguigu.gulimall.ware.vo;

import lombok.Data;

/**
 * 完成采购VO的item
 */
@Data
public class PurchaseItemDoneVo {
    private Long itemId;
    private Integer status;
    private String reason;
}
