package com.atguigu.gulimall.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Catelog2VO {
    private String catalog1Id;  //1级父分类id
    private List<Catelog2VO.Catelog3Vo> catalog3List;  //3级子分类
    private String id;
    private String name;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Catelog3Vo {
        private String catalog2Id;  //父分类，2级分类id
        private String id;
        private String name;

    }
}
