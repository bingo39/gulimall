package com.atguigu.gulimall.product.vo;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

/**
 * Auto-generated: 2023-05-28 2:38:28
 *
 * @author www.ecjson.com
 * @website http://www.ecjson.com/json2java/
 */
@Data
public class SpuSaveVo {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private Bounds bounds;
    private List<BaseAttrs> baseAttrs;
    private List<Skus> skus;
}