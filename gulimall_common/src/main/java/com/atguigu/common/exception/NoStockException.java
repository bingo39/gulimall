package com.atguigu.common.exception;

public class NoStockException extends RuntimeException{

    private Long skuId;
    public NoStockException(Long skuId){
        super("商品id为："+skuId+"没有足够的库存");
    }

    public NoStockException(String msg){
        super(msg);
    }
}
