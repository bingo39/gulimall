package com.atguigu.gulimall.cart.controller;

import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import com.atguigu.gulimall.cart.to.UserInfoTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class CartController {

    /**
     * 参考：jd做法
     * 浏览器有一个cookie:user-Key 标识用户身份，一个月后过期
     * 如果用户是首次使用购物车功能，都会给一个临时的用户身份;
     *
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做
     * 第一次：如果没有临时用户，帮忙创建一个临时用户
     *
     */
    @GetMapping("/cart.html")
    public String CartListPage(){

        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        System.out.println("suerInfo信息"+userInfoTo);
        return "cartList";
    }
}
