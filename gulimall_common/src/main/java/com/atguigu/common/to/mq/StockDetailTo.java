package com.atguigu.common.to.mq;

import lombok.Data;

@Data
public class StockDetailTo {


    /**
     * id
     */
    private Long id;
    /**
     * sku_id
     */
    private Long skuId;
    /**
     * sku_name
     */
    private String skuName;
    /**
     * 购买个数
     */
    private Integer skuNum;
    /**
     * 工作单id
     */
    private Long taskId;

    /**
     * 锁定的仓库id
     */
    private Long wareId;

    /**
     * 锁定状态
     */
    private Integer stockStatus;

}
