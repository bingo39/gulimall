package com.atguigu.gulimall.product.vo;

import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import lombok.Data;

import java.util.List;

@Data
public class SkuItemVo {
    SkuInfoEntity skuInfoEntity;    //sku基本信息

    boolean hasStock = true;    //是否有货

    List<SkuImagesEntity> skuImagesEntityList;  //sku图片信息

    List<SkuItemSaleAttrVo> skuItemSaleAttrVo;    //sku的销售属性组合

    SpuInfoDescEntity spuInfoDescEntity;    //spu介绍【商品介绍】

    List<SpuItemAttrGroupVo> spuItemAttrGroupVos;         //spu的规格参数


}
