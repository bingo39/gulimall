package com.atguigu.gulimall.member.service;

import com.atguigu.gulimall.member.Exception.PhoneExistException;
import com.atguigu.gulimall.member.Exception.UserNameExistException;
import com.atguigu.gulimall.member.vo.MemBerLoginVo;
import com.atguigu.gulimall.member.vo.MemberRegisterVo;
import com.atguigu.gulimall.member.vo.SocialUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-21 15:27:35
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegisterVo memberRegisterVo);

    void checkPhoneUnique(String phone) throws PhoneExistException;

    void checkUserNameUnique(String userName) throws UserNameExistException;

    MemberEntity login(MemBerLoginVo memBerLoginVo);

    /**
     * 社交登录
     */
    MemberEntity login(SocialUser socialUser) throws Exception;

}

