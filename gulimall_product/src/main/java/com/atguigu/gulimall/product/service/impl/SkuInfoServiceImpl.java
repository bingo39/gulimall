package com.atguigu.gulimall.product.service.impl;


import com.atguigu.gulimall.product.entity.SkuImagesEntity;
import com.atguigu.gulimall.product.entity.SpuInfoDescEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.SkuSaleAttrValueService;
import com.atguigu.gulimall.product.service.SpuInfoDescService;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SkuItemVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.SkuInfoDao;
import com.atguigu.gulimall.product.entity.SkuInfoEntity;
import com.atguigu.gulimall.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Autowired
    private SkuImagesServiceImpl skuImagesService;
    @Autowired
    private SpuInfoDescService spuInfoDescService;
    @Autowired
    private AttrGroupService attrGroupService;
    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        QueryWrapper<SkuInfoEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            queryWrapper.and((wrapper) -> {
                wrapper.eq("sku_id", key).or().like("sku_name", key);
            });
        }

        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {

            queryWrapper.eq("catalog_id", catelogId);
        }

        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(catelogId)) {
            queryWrapper.eq("brand_id", brandId);
        }

        String min = (String) params.get("min");
        if (!StringUtils.isEmpty(min)) {
            queryWrapper.ge("price", min);
        }

        String max = (String) params.get("max");

        if (!StringUtils.isEmpty(max)) {
            try {
                BigDecimal bigDecimal = new BigDecimal(max);

                if (bigDecimal.compareTo(new BigDecimal("0")) == 1) {
                    queryWrapper.le("price", max);
                }
            } catch (Exception e) {

            }

        }


        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> list = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return list;
    }

    @Override
    public SkuItemVo item(Long skuId) throws ExecutionException, InterruptedException {

        SkuItemVo skuItemVo = new SkuItemVo();

        // 异步编排优化 [放入线程池]
        /**
         * 备注：这里使用并行化，而不是串行（即在线程背后不加then而是所有线程一起抢资源）
         */
//        // 1.查询的表单：pms_sku_info
//        SkuInfoEntity skuInfoEntity = getById(skuId);
//        skuItemVo.setSkuInfoEntity(skuInfoEntity);
//        Long catalogId = skuInfoEntity.getCatalogId();  //sku对应的三级分类id
//        Long spuId = skuInfoEntity.getSpuId();
        CompletableFuture<SkuInfoEntity> infoFuture = CompletableFuture.supplyAsync(() -> {
            SkuInfoEntity skuInfoEntity = getById(skuId);
            skuItemVo.setSkuInfoEntity(skuInfoEntity);
            return skuInfoEntity;
        }, threadPoolExecutor);

        //2. 图片信息：pms_sku_images
//        List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
//        skuItemVo.setSkuImagesEntityList(skuImagesEntities);
        //备注：3.4.5业务需要等待1业务完成才能进行，2任务与其他任务没有关系，所以可以自行开启一个线程
        CompletableFuture<Void> imageFuture = CompletableFuture.runAsync(() -> {    //不需要返回结果
            List<SkuImagesEntity> skuImagesEntities = skuImagesService.getImagesBySkuId(skuId);
            skuItemVo.setSkuImagesEntityList(skuImagesEntities);
        }, threadPoolExecutor);

        //3. 获取spu的销售属性组合【spu包含sku】
//        List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(spuId);
//        skuItemVo.setSkuItemSaleAttrVo(saleAttrVos);

        CompletableFuture<Void> saleAttrFuture = infoFuture.thenAcceptAsync((res) -> {
            List<SkuItemSaleAttrVo> saleAttrVos = skuSaleAttrValueService.getSaleAttrBySpuId(res.getSpuId());
            skuItemVo.setSkuItemSaleAttrVo(saleAttrVos);
        }, threadPoolExecutor);

        //4.spu的介绍【表：pms_spu_info_desc】
//        SpuInfoDescEntity spuInfoDescServiceById = spuInfoDescService.getById(spuId);
//        skuItemVo.setSpuInfoDescEntity(spuInfoDescServiceById);

        CompletableFuture<Void> descFuture = infoFuture.thenAcceptAsync((res) -> {
            SpuInfoDescEntity spuInfoDescServiceById = spuInfoDescService.getById(res.getSpuId());
            skuItemVo.setSpuInfoDescEntity(spuInfoDescServiceById);
        }, threadPoolExecutor);


        //5. sku的“规格与包装”参数信息
//        List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupService.getAttrGroupWithAttrsBySpuId(spuId,catalogId);
//        skuItemVo.setSpuItemAttrGroupVos(attrGroupWithAttrsBySpuId);
        CompletableFuture<Void> attrGroupVos = infoFuture.thenAcceptAsync((res) -> {
            List<SpuItemAttrGroupVo> attrGroupWithAttrsBySpuId = attrGroupService.getAttrGroupWithAttrsBySpuId(res.getSpuId(), res.getCatalogId());
            skuItemVo.setSpuItemAttrGroupVos(attrGroupWithAttrsBySpuId);
        }, threadPoolExecutor);

        //等所有任务都完成
        CompletableFuture.allOf(imageFuture, saleAttrFuture, descFuture, attrGroupVos).get();

        return skuItemVo;
    }

}