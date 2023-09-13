package com.atguigu.gulimall.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.gulimall.member.Exception.AccessTokenException;
import com.atguigu.gulimall.member.Exception.PhoneExistException;
import com.atguigu.gulimall.member.Exception.UserNameExistException;
import com.atguigu.gulimall.member.dao.MemberLevelDao;
import com.atguigu.gulimall.member.entity.MemberLevelEntity;
import com.atguigu.gulimall.member.vo.MemBerLoginVo;
import com.atguigu.gulimall.member.vo.MemberRegisterVo;
import com.atguigu.gulimall.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.member.dao.MemberDao;
import com.atguigu.gulimall.member.entity.MemberEntity;
import com.atguigu.gulimall.member.service.MemberService;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    @Autowired
    private MemberLevelDao memberLevelDao;
    @Autowired
    private MemberDao memberDao;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegisterVo memberRegisterVo) {

        MemberEntity memberEntity = new MemberEntity();
        MemberDao memberDao = this.baseMapper;

        //设置默认等级
        MemberLevelEntity memberLevelEntity = memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(memberLevelEntity.getId());
        //检查用户名和手机号是否唯一。目的让controller能感知异常，异常机制
        checkPhoneUnique(memberRegisterVo.getPhone());
        checkUserNameUnique(memberRegisterVo.getUserName());

        //设置手机号
        memberEntity.setMobile(memberRegisterVo.getPhone());
        //设置用户名
        memberEntity.setUsername(memberRegisterVo.getUserName());
        //设置密码 【数据库存储密码要进行加密处理】
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberEntity.setPassword(passwordEncoder.encode(memberRegisterVo.getPassWord()));
        //其他的默认信息

        memberDao.insert(memberEntity);
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException{
        MemberDao memberDao = this.baseMapper;
        Long mobile = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if(mobile>0){
            throw new PhoneExistException();
        }
    }

    @Override
    public void checkUserNameUnique(String userName) throws UserNameExistException{
        MemberDao memberDao = this.baseMapper;
        Long count = memberDao.selectCount(new QueryWrapper<MemberEntity>().eq("username", userName));
        if(count>0){
            throw new UserNameExistException();
        }
    }

    @Override
    public MemberEntity login(MemBerLoginVo memBerLoginVo) {
        //账号匹配【用户名&手机号】
        MemberDao memberDao = this.baseMapper;
        MemberEntity entity = memberDao.selectOne(new QueryWrapper<MemberEntity>().eq("username", memBerLoginVo.getLoginAcct()).
                or().eq("mobile", memBerLoginVo.getLoginAcct()));
        if(entity == null){
            // 登录失败
            return null;
        }else{
            //1.去数据库查询密码”加盐值“【密码存储方式：md5+盐值】,获取数据库的password
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            //2.密码匹配
            boolean matches = passwordEncoder.matches(memBerLoginVo.getPassWord(), entity.getPassword());
            if(matches){
                return entity;
            }else {
                return null;
            }
        }

    }

    @Override
    public MemberEntity login(SocialUser socialUser) throws Exception {
        //登录和注册合并逻辑
            //备注：视频中“微博”社交不需要Access_token也可以获取用户uid，而gitee则需要再发带token请求
        //2.1 查询当前社交用户的社交账号信息(uid,昵称，性别等)
        Map<String, String> query = new HashMap<>();
        query.put("access_token",socialUser.getAccess_token());
        HttpResponse response = HttpUtils.doGet("https://gitee.com", "/api/v5/user", "get", new HashMap<String, String>(), query);
        if(response.getStatusLine().getStatusCode() == 200){
            String json = EntityUtils.toString(response.getEntity());
            JSONObject jsonObject = JSON.parseObject(json);
            String uid = jsonObject.get("id").toString();
            //1.判断当前社交用户是否已经登陆过系统
            MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_id", uid));
            if(memberEntity !=null){
                //1.用户有注册记录,更新信息
                MemberEntity updateDate = new MemberEntity();
                updateDate.setId(memberEntity.getId());
                updateDate.setAccessToken(socialUser.getAccess_token());
                updateDate.setExpiresIn(socialUser.getExpires_in());
                //...其他的不重要，更不更新无所谓
                memberDao.updateById(updateDate);

                memberEntity.setAccessToken(socialUser.getAccess_token());
                memberEntity.setExpiresIn(socialUser.getExpires_in());
                return memberEntity;
            }else{
                //2.没有查到当前社交用户记录，就需要注册一个
                MemberEntity register = new MemberEntity();
                try{
                        String name = jsonObject.get("name").toString();
                        String email = jsonObject.get("email").toString();
                        String socialId = jsonObject.get("id").toString();
                    //.....等等信息
                        register.setNickname(name);
                        register.setEmail(email);
                        register.setSocialId(socialId);
                }catch (Exception e){
                    /**
                     * 远程查询昵称这些不重要的，即是出现问题也可以忽略
                     */
                }
                register.setAccessToken(socialUser.getAccess_token());
                register.setExpiresIn(socialUser.getExpires_in());
                memberDao.insert(register);
                return register;
            }
        }else {
            throw new AccessTokenException(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
        }

    }


    /**
     * 笔记：
     * ①register是void没有返回值，如果校验“手机号”或“用户名”不唯一时，需要向前端返回错误信息，但void没有返回值，替代方法就是使用异常机制。
     * 通过异常向上抛出，最终能通过捕获异常code，从而统一得到信息
     */
}
