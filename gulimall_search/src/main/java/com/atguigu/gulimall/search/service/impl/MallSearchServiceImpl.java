package com.atguigu.gulimall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.to.es.SkuEsModel;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.search.Feign.ProductFeignService;
import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import com.atguigu.gulimall.search.constant.EsConstant;
import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.vo.AttrResponseVo;
import com.atguigu.gulimall.search.vo.BrandVo;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MallSearchServiceImpl implements MallSearchService {

    //es客户端接口
    @Resource
    private RestHighLevelClient restHighLevelClient;

    // 远程调用product接口
    @Autowired
    private ProductFeignService productFeignService;

    //去es进行检索
    @Override
    public SearchResult search(SearchParam param) {
        SearchResult result = null;
        // 1.动态构建出查询需要的DSL语句
        SearchRequest searchRequest = buildSeachRequrest(param);
        try {
            // 2.执行检索请求
            SearchResponse response = restHighLevelClient.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
            //3. 分析响应数据封装成需要的格式
            result = buildSeachResult(response, param);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 构建按结果响应数据
     *
     * @param response
     * @return
     */
    private SearchResult buildSeachResult(SearchResponse response, SearchParam param) {

        SearchResult result = new SearchResult();
        List<SkuEsModel> esModelList = new ArrayList();
        //1. 返回所有查询到的商品
        SearchHits hits = response.getHits();
        if (hits.getHits() != null && hits.getHits().length > 0) {
            for (SearchHit hit : hits.getHits()) {
                String sourceAsString = hit.getSourceAsString();//根据kibana面板查看，hits中的hits的_source才是商品信息
                SkuEsModel skuEsModel = JSON.parseObject(sourceAsString, SkuEsModel.class);
                //获取高亮信息
                if (!StringUtils.isEmpty(param.getKeyword())) {
                    HighlightField skuTitle = hit.getHighlightFields().get("skuTitle");
                    String string = skuTitle.getFragments()[0].string();
                    skuEsModel.setSkuTitle(string);
                }
                esModelList.add(skuEsModel);
            }
        }
        result.setProducts(esModelList);
        //2. 当前所有商品涉及到的所有属性信息【要从聚合中分析】
        List<SearchResult.AttrVo> attrVos = new ArrayList<>();
        ParsedNested attrAgg = response.getAggregations().get("attr_agg");
        ParsedLongTerms attr_id_agg = attrAgg.getAggregations().get("attr_id_agg");
        for (Terms.Bucket bucket : attr_id_agg.getBuckets()) {
            SearchResult.AttrVo attrVo = new SearchResult.AttrVo();
            //2.1 属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);
            //2.2 属性名字
            String attrName = ((ParsedStringTerms) bucket.getAggregations().get("attr_name_agg")).getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);
            //2.3 属性所有值【value;有多个不唯一】
            List<String> attrValues = ((ParsedStringTerms) bucket.getAggregations().get("attr_value_agg")).getBuckets().stream().map(item -> {
                String keyAsString = ((Terms.Bucket) item).getKeyAsString();
                return keyAsString;
            }).collect(Collectors.toList());
            attrVo.setAttrValue(attrValues);

            attrVos.add(attrVo);
        }
        result.setAttrs(attrVos);
        //3.当前所有商品涉及到的所有品牌信息
        List<SearchResult.BrandVo> brandVos = new ArrayList();
        ParsedLongTerms brand_agg = response.getAggregations().get("brand_agg");
        for (Terms.Bucket bucket : brand_agg.getBuckets()) {
            SearchResult.BrandVo brandVo = new SearchResult.BrandVo();
            //3.1 品牌id
            long brandId = bucket.getKeyAsNumber().longValue();
            brandVo.setBrandId(brandId);
            //3.2 品牌名
            String brandName = ((ParsedStringTerms) bucket.getAggregations().get("brand_name_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandName(brandName);
            //3.3 品牌图片
            String brandImg = ((ParsedStringTerms) bucket.getAggregations().get("brand_img_agg")).getBuckets().get(0).getKeyAsString();
            brandVo.setBrandImg(brandImg);
            brandVos.add(brandVo);
        }
        result.setBrands(brandVos);
        //4.当前所有商品涉及到的所有分类信息
        List<SearchResult.CatalogVo> catalogVos = new ArrayList();
        ParsedLongTerms catalog_agg = response.getAggregations().get("catalog_agg");    //parsedLongTerms相当于aggregations中的terms
        List<? extends Terms.Bucket> buckets = catalog_agg.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            SearchResult.CatalogVo catalogVo = new SearchResult.CatalogVo();
            // 得到分类id
            String keyAsString = bucket.getKeyAsString();
            catalogVo.setCatalogId(Long.parseLong(keyAsString));
            // 得到分类名
            ParsedStringTerms catalog_name_agg = bucket.getAggregations().get("catalog_name_agg");
            String catalog_name = catalog_name_agg.getBuckets().get(0).getKeyAsString();
            catalogVo.setCatalogName(catalog_name);

            catalogVos.add(catalogVo);
        }
        result.setCatalogs(catalogVos);

        // ======以上可以从聚合信息中获取=============
        //5.分页信息
        //5.1 页码
        result.setPageNum(param.getPageNum());
        //5.2 总记录数 【ES中的hits就是总记录数】
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //5.3 总页码【计算方式：总记录数/页面大小】
        int totalPages = (int) total % EsConstant.PRODUCT_PAGE_SIZE == 0 ? (int) total / EsConstant.PRODUCT_PAGE_SIZE : ((int) total / EsConstant.PRODUCT_PAGE_SIZE + 1);
        result.setTotalPages(totalPages);

        //展示页面的页码计算
        List<Integer> pageNavs = new ArrayList<>();
        for (int i = 1; i < totalPages; i++) {
            pageNavs.add(i);
        }
        result.setPageNavs(pageNavs);

        // 6. 构建面包屑导航功能
        /**
         * 面包屑导航只包含筛选属性，即attrs
         * 【正常面包屑功能隶属于前端操作的】
         */
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            List<SearchResult.NavVo> navVoCollect = param.getAttrs().stream().map(attr -> {
                SearchResult.NavVo navVo = new SearchResult.NavVo();
                //6.1 分析每一个attr传递过来的参数值 【样式：xx-xx:xx (属性类型id_属性名:属性名)】
                navVo.setNavName(attr);
                String[] s = attr.split("_");
                R attrInfo = productFeignService.attrInfo(Long.parseLong(s[0]));
                result.getAttrIds().add(Long.parseLong(s[0]));
                if (attrInfo.getCode() == 0) {
                    AttrResponseVo data = attrInfo.getData("attr", new TypeReference<AttrResponseVo>() {
                    });
                    navVo.setNavName(data.getAttrName());
                } else {
                    navVo.setNavName(s[0]);
                }
                navVo.setNavValue(s[1]);
                //6.2 取消面包屑,跳转位置【将请求地址的url里面的地址置空】
                //思路：拿到所有查询条件，去除当前
                String replace = replaceQueryString(param, attr, "attrs");
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);

                return navVo;
            }).collect(Collectors.toList());
            result.setNavs(navVoCollect);
        }

        //品牌与分类的面包屑添加（上到面包屑，要剔除掉选项框）
        //TODO 分类【不需要导航需求，提示需要】
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            List<SearchResult.NavVo> navs = result.getNavs();
            SearchResult.NavVo navVo = new SearchResult.NavVo();

            navVo.setNavName("品牌");
            //远程查询所有品牌
            R r = productFeignService.brandInfo(param.getBrandId());
            if (r.getCode() == 0) {
                List<BrandVo> brandVoList = r.getData("brand", new TypeReference<List<BrandVo>>() {
                });
                StringBuffer stringBuffer = new StringBuffer();
                String replace = "";
                for (BrandVo brand : brandVoList) {
                    stringBuffer.append(brand.getName() + ";");
                    replace = replaceQueryString(param, brand.getBrandId() + "", "brandId");
                }
                navVo.setNavValue(stringBuffer.toString());
                navVo.setLink("http://search.gulimall.com/list.html?" + replace);
            }

            navs.add(navVo);
        }

        return result;
    }

    // 转换字符串编码
    private String replaceQueryString(SearchParam param, String value, String key) {
        String encode = null;
        try {
            encode = URLEncoder.encode(value, "UTF-8");
            // 浏览器对于空格会编译为“%20”,java则会编译为"+"。进行差异化处理
            encode.replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return param.get_queryString().replace("&" + key + "=" + encode, "");
    }

    /**
     * 准备检索请求
     * 请求需求：模糊匹配；过滤（按照属性，分类，品牌，价格区间，库存）；排序；分类；高亮;聚合分析
     *
     * @return SearchRequest searchRequest
     */
    private SearchRequest buildSeachRequrest(SearchParam param) {

        // 构建DSL语句
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        /**
         * DSL中query部分：模糊匹配；过滤（按照属性，分类，品牌，价格区间，库存）
         */
        //1.构建bool-query
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //1.1  query中must-模糊匹配 【"must":{"match":{"skuTitle":"xxx"}}】
        if (!StringUtils.isEmpty(param.getKeyword())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("skuTitle", param.getKeyword()));
        }
        //1.2 query中filter-过滤
        //1.2.1 按照三级分类 【"filter":{"term":{"catalog":"xxx"}}】
        if (param.getCatalogId() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("catalogId", param.getCatalogId()));
        }
        //1.2.2 按照品牌id（多个） 【"filter":{"term":{"brandId":["xxx","xxx","xxx"]}}】
        //TODO 因为es映射是long类型，SearchParam查询参数中brandId是list<long>,目前暂时先用get(0)顶着，不然p185查询会因为类型不匹配而报错【后台传入的是数组】
        if (param.getBrandId() != null && param.getBrandId().size() > 0) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("brandId", param.getBrandId().get(0)));
        }
        //1.2.3 按照属性（比较复杂，嵌入式）
        /**
         * DSL语句：
         * "filter":"nested":{"path":"attrs","query":{"bool":{"must":[{"term":{"attrs.attrId":{"value":"xxx"}}},
         * {"terms":{"attrs.attrValue":["xxx","xxx"]}}]}}}
         */
        if (param.getAttrs() != null && param.getAttrs().size() > 0) {
            // 页面attr请求，例如：&attr=1_5寸:8寸&attr=2_16G......
            for (String attrStr : param.getAttrs()) {
                //每个遍历的结果都要生成嵌入式的过滤
                BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
                String[] s = attrStr.split("_");
                String attId = s[0];   //检索的属性id
                String[] attrValues = s[1].split(":");//该属性的检索用的值
                nestedBoolQuery.must(QueryBuilders.termQuery("attrs.attrId", attId));
                nestedBoolQuery.must(QueryBuilders.termsQuery("attrs.attrValue", attrValues));
                //QueryBuilders.nestedQuery(String path, QueryBuilder query, ScoreMode scoreMode)
                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBoolQuery, ScoreMode.None);
                boolQueryBuilder.filter(nestedQuery);
            }
        }
        //1.2.4 是否有库存 【"filter":{"term":{"hasStock":"0或1"}}】
        //TODO 备注：因为测试数据较少。如果前端传的param不为空，无论是否有库存都可以进行查询
        if (param.getHasStock() != null) {
            boolQueryBuilder.filter(QueryBuilders.termQuery("hasStock", param.getHasStock() == 1));
        }
        //1.2.5 价格区间 【"filter":{"range":{"skuPrice":{"gte":xxx,"lte":xxx}}}
        if (!StringUtils.isEmpty(param.getSkuPrice())) {
            RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("skuPrice");
            //价格区间格式：xx_xx(用短横杠分割) 例如：1_6000 或 _6000 或6000_
            String[] s = param.getSkuPrice().split("_");
            if (s.length == 2) {
                //有两个值，说明是区间
                rangeQuery.gte(s[0]).lte(s[1]);
                //只有一个值
            } else if (s.length == 1) {
                //以短横杠开始，说明价格小于某个值
                /**
                 * range老版：lte....gte...
                 * range新版：from...to...
                 */
                if (param.getSkuPrice().startsWith("_")) {
                    rangeQuery.lte(s[0]);
                }
                if (param.getSkuPrice().endsWith("_")) {
                    //以短横杠结束，说明价格大于某个值
                    rangeQuery.gte(s[0]);
                }
            }
            boolQueryBuilder.filter(rangeQuery);
        }

        // 把上述所有的条件进行封装
        sourceBuilder.query(boolQueryBuilder);

        /**
         * 2. 排序；分页；高亮
         */
        //2.1 排序 【"sort":{"skuPrice":{"order":desc或asc}}】
        if (!StringUtils.isEmpty(param.getSort())) {
            String sort = param.getSort();
            String[] split = sort.split("_");
            SortOrder order = split[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC;
            sourceBuilder.sort(split[0], order);
        }
        //2.2 分页 【"from":0,"size":5】
        /**
         * 分页计算方式：
         * form = (pageNum-1)*size
         */
        sourceBuilder.from((param.getPageNum() - 1) * EsConstant.PRODUCT_PAGE_SIZE);
        sourceBuilder.size(EsConstant.PRODUCT_PAGE_SIZE);
        //2.3 高亮 【"highlight":{"fields:{"skuTitle":{....},"pre_tags":"<b style='color:red'>","post_tags":"</b>"}】
        if (!StringUtils.isEmpty(param.getKeyword())) {   //有模糊匹配高亮才有意义
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuTitle");
            highlightBuilder.preTags("<b style='color:red'>");
            highlightBuilder.postTags("</b>");
            sourceBuilder.highlighter(highlightBuilder);
        }
        // 3 聚合分析 【"aggs":{....}】
        //3.1 品牌id聚合 【"brand_agg":{"terms": {"field": "brandId","size": 10}}】
        TermsAggregationBuilder brandAgg = AggregationBuilders.terms("brand_agg");
        brandAgg.field("brandId").size(50);
        //3.1.1 品牌聚合的子聚合 【"aggs":{"brand_name_agg":{"terms":{"field":"brandName","size":10}},"brand_img_agg":{"terms":{"field":"brandImg","size":10}}}】
        brandAgg.subAggregation(AggregationBuilders.terms("brand_name_agg").field("brandName").size(1));
        brandAgg.subAggregation(AggregationBuilders.terms("brand_img_agg").field("brandImg").size(1));
        sourceBuilder.aggregation(brandAgg);
        //3.2 分类聚合 catalog_agg 【"catalog_agg":{"terms":{"field":"catalogId","size":10},"aggs":{"catalog_name_agg":{"terms":{"field":"catalogName","size":10}}}】
        TermsAggregationBuilder catalogAgg = AggregationBuilders.terms("catalog_agg").field("catalogId").size(20);
        //3.2.1 分类聚合的子聚合-分类名称聚合
        catalogAgg.subAggregation(AggregationBuilders.terms("catalog_name_agg").field("catalogName").size(1));
        sourceBuilder.aggregation(catalogAgg);
        //3.3 属性聚合【嵌入聚合】
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attr_agg", "attrs");
        //聚合出当前所有attrId
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attr_id_agg").field("attrs.attrId");
        //聚合分析出当前attr_id对应的名字
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_name_agg").field("attrs.attrName").size(1));
        attrIdAgg.subAggregation(AggregationBuilders.terms("attr_value_agg").field("attrs.attrValue").size(50));
        attrAgg.subAggregation(attrIdAgg);
        sourceBuilder.aggregation(attrAgg);

        String s = sourceBuilder.toString();
        System.out.println("最终DSL：" + s);
        return new SearchRequest(new String[]{EsConstant.PRODUCT_INDEX}, sourceBuilder);

        /**
         * 补充：
         * 如果只要查询_source【即所有商品信息hits，不需要聚合，高亮等】，用sourceBuilder.searchSource()即可
         */
    }
}
