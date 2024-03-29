package com.atgulimall.gulimall.ssoclient.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;

@Controller
public class HelloController {

    @Value("${sso.server.url}")
    String ssoServerUrl;

    /**
     * 无须登录即可访问
     */
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    /**
     * 感知这次是在ssoserver是登录成后调回来的
     */
    @GetMapping("/boss")
    public String employees(Model model, HttpSession httpSession, @RequestParam(value = "token",required = false)String token){

        if(!StringUtils.isEmpty(token)){
            //去ssoserver登录成功返回来的
            //TODO 去ssoserver获取当前token真正对应的用户信息
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> forEntity = restTemplate.getForEntity("http://ssoserver.com:8080/userInfo?token="+token, String.class);
            String body = forEntity.getBody();
            httpSession.setAttribute("loginUser",body);
        }
        if(httpSession.getAttribute("loginUser") ==null){
            //没登陆，跳转到登录服务器
            return "redirect:"+ssoServerUrl+"?redirect_url=http://client2.com:8082/boss";

        }else{
            ArrayList<String> emps = new ArrayList<>();
            emps.add("张三");
            emps.add("李四");
            model.addAttribute("emps",emps);
            return "list";
        }
    }

}
