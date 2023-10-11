package com.atguigu.gulimall.order.feign;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.order.vo.WareSkuLockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WmsFeignService {

    //检查是否有库存
    @RequestMapping("ware/waresku/hasStock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);

    //计算运费
    @GetMapping("/ware/wareinfo/fare")
    public R getFare(@RequestParam("addrId") Long id);

    //为订单锁定库存
    @PostMapping("/ware/waresku/lock/order")
    public R orderLockStock(@RequestBody WareSkuLockVo vo);
}
