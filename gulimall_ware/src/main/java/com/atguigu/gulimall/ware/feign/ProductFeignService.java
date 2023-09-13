package com.atguigu.gulimall.ware.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("gulimall-product")
public interface ProductFeignService {

    /**
     * feign两种写法：
     * 1. 让所有请求过网关：
     * ①  @FeignClient("gulimall-geteway")
     * ②   @RequestMapping("/api/product/skuinfo/info/{skuId}")
     * 2. 直接给后台指定服务处理：
     * ① @FeignClient("gulimall-product")
     * ② @RequestMapping("/product/skuinfo/info/{skuId}")
     */
    //商品库存查询商品名称
    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);

}
