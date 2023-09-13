package com.atguigu.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrRespVo extends AttrVo {
    /**
     * 所属分类名字
     */
    private String catelogName;

    /**
     * 所示分组名字
     */
    private String groupName;

    /**
     * 返回分类的完整路径
     */
    private Long[] catelogPath;
}
