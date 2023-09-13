package com.atguigu.gulimall.product.Feign;

import com.atguigu.common.to.SkuHasStockTo;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient("gulimall-ware")
public interface WareFeignService {

    /**
     * 远程调用完，还要给调用者转换为需要的类型，相对复杂，可以通过服务提供者提前转好
     * 有3种解析方案：
     * 1.R设计的时候加上泛型
     * 2.直接返回想要的结果
     * 3.自己封装解析结果
     *
     * @param skuIds
     * @return
     */
    @RequestMapping("ware/waresku/hasStock")
    R getSkusHasStock(@RequestBody List<Long> skuIds);
}
