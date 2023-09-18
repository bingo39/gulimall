package com.atguigu.gulimall.testssoserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @ResponseBody
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("token")String token){
        String s = redisTemplate.opsForValue().get(token);
        return s;
    }

    @PostMapping("/doLogin")
    public String doLogin(@RequestParam("username") String username, @RequestParam("password")String password, @RequestParam("redirect_url")String url, HttpServletResponse response){

        if(!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)){
            //登录成功，跳回请求的页面【不同的client请求不同的页面】
            String uuid = UUID.randomUUID().toString().replace("-","");
            redisTemplate.opsForValue().set(uuid,username);
            Cookie ssoToken = new Cookie("sso_token",uuid);
            response.addCookie(ssoToken);
            return "redirect:"+url+"?token="+uuid;
        }else{
            return "login";
        }
    }

    @GetMapping("/login.html")
    public String loginPage(@RequestParam("redirect_url") String url, Model model, @CookieValue(value = "sso_token",required = false) String sso_token){
        if(!StringUtils.isEmpty(sso_token)){
            //说明其他域名有登录，给浏览器留下痕迹
            return "redirect:"+url+"?token="+sso_token;
        }
        model.addAttribute("url",url);
        return "login";
    }
}
