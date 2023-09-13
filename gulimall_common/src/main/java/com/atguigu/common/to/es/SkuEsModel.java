package com.atguigu.common.to.es;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品上架model
 * 备注：这个model属性是es对于大量数据索引，时间和空间的一种折中
 */
@Data
public class SkuEsModel {
    private Long skuId;

    private Long spuId;

    private String skuTitle;

    private BigDecimal skuPrice;

    private String skuImg;

    private Long saleCount;

    private Boolean hasStock;

    private Long hostScore;

    private Long brandId;

    private Long catalogId;

    private String brandName;

    private String brandImg;

    private String catalogName;

    //------ 所有商品的规程参数
    private List<Attrs> attrs;

    @Data
    public static class Attrs {
        private Long attrId;
        private String attrName;
        private String attrValue;
    }
}
