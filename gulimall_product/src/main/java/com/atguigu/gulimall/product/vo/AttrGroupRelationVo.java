package com.atguigu.gulimall.product.vo;

import lombok.Data;

@Data
public class AttrGroupRelationVo {

    // 关联关系发送的两个参数
    private Long attrId;
    private Long attrGroupId;

}
