package com.atguigu.gulimall.search.vo;

import lombok.Data;

import java.util.List;

/**
 * 检索参数,封装页面所有可能传递过来的查询条件
 */
@Data
public class SearchParam {

    private String keyword; //页面传递过来的全文匹配

    private Long catalogId; //三级分类id

    /**
     * sort=saleCount_asc/desc 销量序排序
     * sort=skuPrice_asc/desc    价格升/降序排序
     * sort=hotScore_asc/desc   热度升/降序排序
     */
    private String sort;    //排序条件

    /**
     * 过滤条件
     */
    private Integer hasStock;   //是否显示有货，有货为1，无货为0；【数据较少，就不设置默认值1】

    private String skuPrice;    //价格区间

    private List<Long> brandId;     //安装品牌id进行检索，可多选

    private List<String> attrs;   //属性

    private Integer pageNum = 1;    //页码

    private String _queryString;    //原生所有查询条件


}
