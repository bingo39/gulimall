package com.atguigu.gulimall.product.dao;

import com.atguigu.gulimall.product.entity.CommentReplayEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品评价回复关系
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-20 00:02:46
 */
@Mapper
public interface CommentReplayDao extends BaseMapper<CommentReplayEntity> {

}
