package com.atguigu.gulimall.ware.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完成采购的vo
 */
@Data
public class PurchaseDoneVo {
    @NotNull
    private Long id;    //采购单id
    private List<PurchaseItemDoneVo> items;
}
