package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 社交登录请求
 */
@Slf4j
@Controller
public class OAuth2Controller {

    @Autowired
    private MemberFeignService memberFeignService;

    @GetMapping("/oauth2.0/gitee/success")
    public String gitee(@RequestParam("code") String code, HttpSession session) throws Exception {
        Map<String, String> map = new HashMap();
        map.put("client_id",个人client_id);
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/gitee/success");
        map.put("client_secret",个人secret);
        map.put("code",code);
        map.put("grant_type","authorization_code");
        //1.根据code换取access_token
        HttpResponse response = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post",  new HashMap<>(), map, new HashMap<>());

        //2.处理响应回来的json串
        if(response.getStatusLine().getStatusCode() == 200){
            //获取access_token
            String json = EntityUtils.toString(response.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            //远程调用member服务来处理用户信息注册、校验功能
            R r = memberFeignService.oauth2Login(socialUser);
            if(r.getCode()==0){
                MemberRespVo data = r.getData("data", new TypeReference<MemberRespVo>() {
                });
                log.info("登录成功：用户信息为：{}"+data.toString());
                //第一次使用session;命令浏览器保存JSESSIONID这个cookie,以后浏览器访问哪个网站就会带上这个网站的cookie
                    //解决子域之间：gulimall.com -->auth.gulimall.com ; order.gulimall.com
                    //要求：指定域名作用为父域名，即是是子域发的JSESSIONID，父域也可以使用其cookie信息【因为涉及到tomcat发的session,很麻烦，整合springSession来处理】
                session.setAttribute("loginUser",data);
               // 登录成功就跳回首页
                return "redirect:http://gulimall.com";

            }else {
                return "redirect:http://auth.gulimall.com/login.html";
            }

        }else {
            //失败重定向到登录页
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }
}

/**
 * 笔记：
 * ① 要把data的数据存储到远程服务redis中，必须对数据进行系列化，即MemberRespVo要实现Serialzable接口
 * ② 默认发的令牌，作用域只是为当前域，【解决子域共享问题】
 *      查看GulimallSessionConfig解决方案
 * ③ 使用JSON的序列化方式来序列化对象数据到redis中，方便观察
 *      查看GulimallSessionConfig解决方案
 */
