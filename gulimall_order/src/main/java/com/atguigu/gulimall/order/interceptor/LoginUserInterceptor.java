package com.atguigu.gulimall.order.interceptor;


import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.vo.MemberRespVo;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ① 只要是进入订单页面，都是登录状态，所以设置登录拦截器
 * ② spring家拦截器都要实现HandlerInterceptor
 */
@Component
public class LoginUserInterceptor implements HandlerInterceptor {

    public static ThreadLocal<MemberRespVo>loginUser = new ThreadLocal<>();
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //对服务内部的调用进行放行【例如order、ware & rabbitmq 之间的解库存操作】
            //相关请求：/order/order/status/xxxxxx
        String requestURI = request.getRequestURI();
        boolean match = new AntPathMatcher().match("/order/order/status/**",requestURI);
        if(match){
            return true;
        }

        MemberRespVo attribute = (MemberRespVo) request.getSession().getAttribute(AuthServerConstant.lOGIN_USER);//整合springSession，登录状态就会有session用户
        if(attribute!=null){
            //保存登录的用户到threadLocal中，避免线程冲突
            loginUser.set(attribute);
            return true;
        }else {
            //未登录,重定向到登录页
            request.getSession().setAttribute("msg","请先登录");
            response.sendRedirect("http://auth.gulimall.com/login.html");
            return false;
        }
    }
}
