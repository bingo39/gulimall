package com.atguigu.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品远程调用积分的TO
 */
@Data
public class SpuBoundTo {
    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
