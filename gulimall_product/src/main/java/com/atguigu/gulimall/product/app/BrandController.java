package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.UpdateGroup;
import com.atguigu.common.valid.UpdateStatusGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;

/**
 * 品牌
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-21 06:29:20
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId) {
        BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 查询所有品牌
     */
    @GetMapping("/infos")
    public R brandInfo(@RequestParam("brandId") List<Long> brandIds) {
        List<BrandEntity> brand = brandService.getBrandsByIds(brandIds);
        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     *
     * @Valid:开启校验规则，没有指定校验分组
     * @Validated:spring提供的注解，可以指定校验分组
     */
    @RequestMapping("/save")
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand /*, BindingResult bindingResult**/) {
        //jrs303校验  统一交给GulimallExceptionControllerAdivce处理
//        if(bindingResult.hasErrors()){
//            Map<String,String>map = new HashMap();
//            //获取校验错误的结果
//            bindingResult.getFieldErrors().forEach((item)->{
//                // 错误提示消息；可在Entity中配置
//                String message = item.getDefaultMessage();
//                // 获取错误的属性的名字
//                String field = item.getField();
//                map.put(field,message);
//
//            });
//            return R.error(400,"提交的数据不合法").put("errData",map);
//        }else{
//            brandService.save(brand);
//        }
        brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@Validated(UpdateGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateDetail(brand);

        return R.ok();
    }

    /**
     * 修改状态
     */
    @RequestMapping("/update/status")
    public R updateStatus(@Validated(UpdateStatusGroup.class) @RequestBody BrandEntity brand) {
        brandService.updateById(brand);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds) {
        brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

}
