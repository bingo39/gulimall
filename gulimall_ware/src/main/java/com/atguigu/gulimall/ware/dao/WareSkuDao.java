package com.atguigu.gulimall.ware.dao;

import com.atguigu.gulimall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-21 15:30:13
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    // 多个参数要为每个参数生成Param
    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkuStock(@Param("skuId") Long skuId);
}
