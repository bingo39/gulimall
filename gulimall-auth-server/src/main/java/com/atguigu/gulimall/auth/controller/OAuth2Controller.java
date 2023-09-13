package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.HttpUtils;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.vo.MemberRespVo;
import com.atguigu.gulimall.auth.vo.SocialUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String gitee(@RequestParam("code") String code) throws Exception {
        Map<String, String> map = new HashMap();
        map.put("client_id","71fdfac0ffdf265a0c293206db3f2fac2a6c8627bea1ce1ab137d8a28dbb93eb");
        map.put("redirect_uri","http://auth.gulimall.com/oauth2.0/gitee/success");
        map.put("client_secret","77dc9a8ceb8c6e3e624d022236910c2aed370f615bb5188da2d04054c2f42960");
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
