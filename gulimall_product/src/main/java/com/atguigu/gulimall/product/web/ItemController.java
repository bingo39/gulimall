package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.concurrent.ExecutionException;

/**
 * 商品详情信息
 */
@Controller
public class ItemController {

    @Autowired
    SkuInfoService skuInfoService;

    //展现sku商品详情
    @GetMapping("/{skuId}.html")
    public String skuItem(@PathVariable("skuId") Long skuId, Model model) throws ExecutionException, InterruptedException {
        System.out.println("准备查询" + skuId + "详情");
        SkuItemVo skuItemVo = skuInfoService.item(skuId);
        //model是把查询出来的spu和sku放入到thymeleaf渲染页面
        model.addAttribute("item", skuItemVo);
        for (SkuImagesEntity img : skuItemVo.getSkuImagesEntityList()) {
            System.out.println("图片路径：" + img.getImgUrl());
        }
        return "item";
    }

}
