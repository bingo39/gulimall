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
public class Skus {

    private List<Attr> attr;
    private String skuName;
    private BigDecimal price;
    private String skuTitle;
    private String skuSubtitle;
    private List<Images> images;
    private List<String> descar;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    private List<Memberprice> memberPrice;
}