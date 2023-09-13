package com.atguigu.gulimall.product.service;

import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * sku信息
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-20 00:02:46
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuInfo(SkuInfoEntity skuInfoEntity);

    PageUtils queryPageByCondition(Map<String, Object> params);

    List<SkuInfoEntity> getSkuBySpuId(Long spuId);

    //查询商品详情
    SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException;
}

