package com.atguigu.gulimall.product.Feign;

import com.atguigu.common.to.SkuReductionTo;
import com.atguigu.common.to.SpuBoundTo;
import com.atguigu.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 调用远程服务
 * SpringCloud远程调用逻辑：
 * 1.当服务A向服务B远程传输到一个对象，springCloud会自动给将对象转为json
 * 接口方法： R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);就是发送json对象数据，(@RequestBody转换为json)
 * 2.在注册中心找到guilimall-coupon服务。给/coupon/spubounds/save发送请求。将刚刚转的json对象转为请求体位置
 * 3.B服务收到请求
 * 4.B服务将请求体的json转为指定对象;
 * <p>
 * 疑问：
 * 下列方法中，请求体中的对象为SpuBoundTo，而接收对象类型为SpuBoundsEntity，为什么还可以转换成功？
 * 解析：
 * 因为A服务是通过json形式发过去给B对象的，只要B服务解析json的属性能一一对应，也是可以解析成功的。
 */
@FeignClient("gulimall-coupon")
public interface CouponFeignService {
    // 积分服务
    @PostMapping("/coupon/spuBounds/save")
    R saveSpuBounds(@RequestBody SpuBoundTo spuBoundTo);

    // 满减服务
    @PostMapping("/coupon/skuFullReduction/saveInfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
