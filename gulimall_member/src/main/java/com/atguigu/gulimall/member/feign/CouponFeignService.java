package com.atguigu.gulimall.member.feign;

import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 远程接口，让Coupon各个服务能被调用
 * 声明式远程调用，也就是查找gulimall_coupon这个服务
 * 注意：
 * ①feign不支持调用的服务名称带有下划线"_"
 */
@FeignClient("gulimall-coupon")
//@Component
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R memberCoupons();

}
