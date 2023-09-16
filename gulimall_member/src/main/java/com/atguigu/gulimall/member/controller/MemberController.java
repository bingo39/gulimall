package com.atguigu.gulimall.member.controller;

import java.util.Arrays;
import java.util.Map;

import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.gulimall.member.Exception.PhoneExistException;
import com.atguigu.gulimall.member.Exception.UserNameExistException;
import com.atguigu.gulimall.member.feign.CouponFeignService;
import com.atguigu.gulimall.member.vo.MemBerLoginVo;
import com.atguigu.gulimall.member.vo.MemberRegisterVo;
import com.atguigu.gulimall.member.vo.SocialUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.service.MemberService;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.R;


/**
 * 会员
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-21 15:27:35
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    @Autowired
    private MemberService memberService;

    //远程接口
    @Autowired
    CouponFeignService couponFeignService;

    // 注册中心调用者 临时编号：23.4.24.02
    @RequestMapping("/coupons")
    public R test() {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setNickname("张三");

        R memberCoupons = couponFeignService.memberCoupons();
        //返回调用者调用成功信息
        return R.ok().put("member", memberEntity).put("coupons", memberCoupons.get("coupons"));

    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member) {
        memberService.save(member);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member) {
        memberService.updateById(member);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 会员注册功能
     */
    @PostMapping("/register")
    public R register(@RequestBody MemberRegisterVo memberRegisterVo){

        try {
            memberService.register(memberRegisterVo);
        }catch (PhoneExistException e){
            return R.error(BizCodeEnume.PHONE_EXIST_EXCEPTION.getCode(),BizCodeEnume.PHONE_EXIST_EXCEPTION.getMsg());
        }catch (UserNameExistException e){
            return R.error(BizCodeEnume.USER_EXIST_EXCEPTION.getCode(),BizCodeEnume.USER_EXIST_EXCEPTION.getMsg());
        }

        return R.ok();
    }

    /**
     * 登录功能
     * 【普通登录】
     */
    @PostMapping("/login")
    public R login(@RequestBody MemBerLoginVo memBerLoginVo){

        MemberEntity entity = memberService.login(memBerLoginVo);
        if(entity!=null){
            return R.ok().setData(entity);
        }else {
         return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }

    /**
     * 登录功能
     * 【社交登录】
     */
    @PostMapping("/oauth2/login")
    public R oauth2Login(@RequestBody SocialUser socialUser) throws Exception {
        MemberEntity entity = memberService.login(socialUser);
        if(entity!=null){
            return R.ok().setData(entity);
        }else {
            return R.error(BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getCode(), BizCodeEnume.LOGINACCT_PASSWORD_INVAILD_EXCEPTION.getMsg());
        }
    }


}
