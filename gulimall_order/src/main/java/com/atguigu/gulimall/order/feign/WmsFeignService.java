package com.atguigu.gulimall.order.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WmsFeignService {

    //检查是否有库存
    @RequestMapping("ware/waresku/hasStock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
