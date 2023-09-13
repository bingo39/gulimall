package com.atguigu.gulimall.product.entity;

import com.atguigu.common.valid.AddGroup;
import com.atguigu.common.valid.ListValue;
import com.atguigu.common.valid.UpdateGroup;
import com.atguigu.common.valid.UpdateStatusGroup;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import lombok.Data;
import org.hibernate.validator.constraints.URL;


import javax.validation.constraints.*;


/**
 * 备注；
 * 因为校验使用了@Validated，所以要@NotNull/ @NotEmpty/@NotBlank/@URL.....指定校验组
 * (不只是判断是否为空的注解，像@Min/@URL等也是校验注解)
 * 如果是@Valid就不需要给
 */

/**
 * 品牌
 */
@Data
@TableName("pms_brand")
public class BrandEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 品牌id
     */
    @TableId
    @NotNull(message = "修改必须指定品牌id", groups = {UpdateGroup.class})
    @Null(message = "新增不能指定品牌id", groups = {AddGroup.class})
    private Long brandId;
    /**
     * 品牌名
     */
    @NotBlank(message = "品牌名称不能为空", groups = {AddGroup.class, UpdateGroup.class})
    private String name;
    /**
     * 品牌logo地址
     */
    @NotEmpty(groups = {AddGroup.class, UpdateGroup.class})
    @URL(message = "logo必须是一个合法的url地址", groups = {AddGroup.class, UpdateGroup.class})
    private String logo;
    /**
     * 介绍
     */
    @NotEmpty(groups = {AddGroup.class, UpdateGroup.class})
    private String descript;
    /**
     * 显示状态[0-不显示；1-显示]
     *
     * @ListValue：自定义校验注解
     */
    @ListValue(vals = {0, 1}, groups = {AddGroup.class, UpdateStatusGroup.class})
    @NotNull(groups = {AddGroup.class, UpdateStatusGroup.class})
    private Integer showStatus;
    /**
     * 检索首字母
     */
    //@Pattern正则表达式
    @Pattern(regexp = "^[a-zA-Z]$", message = "检索首字母必须是一个字母", groups = {AddGroup.class, UpdateGroup.class})
    private String firstLetter;
    /**
     * 排序
     */
    @NotNull(groups = {AddGroup.class, UpdateGroup.class})
    @Min(value = 0, message = "排序必须大于等于0", groups = {AddGroup.class, UpdateGroup.class})
    private Integer sort;

}
