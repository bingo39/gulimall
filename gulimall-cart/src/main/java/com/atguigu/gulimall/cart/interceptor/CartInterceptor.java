package com.atguigu.gulimall.cart.interceptor;

import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.constant.CartConstant;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimall.cart.to.UserInfoTo;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * 在执行目标方法之前，判断用户的登录状态。并封装传递给controller目标请求
 * 备注；
 * userId是识别用户身份的;userKey是临时用户身份识别，也是购物车服务后面操作的识别id，所以无论是否临时登录，都会提供一个userkey
 */
@Component
public class CartInterceptor implements HandlerInterceptor {

    //ThreadLocal：共享线程内的数据。即上一个线程传输的数据在下一个线程也可以使用到
    public static ThreadLocal<UserInfoTo> threadLocal = new ThreadLocal<>();

    /**
     * 在目标方法执行之前
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object handler)throws Exception{

        //用户没登录有user-key识别购物车内容；登录了有userId记录
        UserInfoTo userInfo = new UserInfoTo();
        HttpSession session = request.getSession();
        MemberRespVo memberRespVo = (MemberRespVo) session.getAttribute(AuthServerConstant.lOGIN_USER);
       if(memberRespVo!=null){
           userInfo.setUserId(memberRespVo.getId());
       }
        Cookie[] cookies = request.getCookies();
       if(cookies!=null && cookies.length>0){
           for(Cookie cookie:cookies){
               //user-key
               String name = cookie.getName();
               if(name.equals(CartConstant.TEMP_USER_COOKIE_NAME)){
                   userInfo.setUserKey(cookie.getValue());
                   userInfo.setTempUser(false);
               }
           }

       }

       //如果没有临时用户，一定要分配临时用户
       if(StringUtils.isEmpty(userInfo.getUserKey())){
           String uuid = UUID.randomUUID().toString();
           userInfo.setUserKey(uuid);
       }
       //在目标方法执行之前
        threadLocal.set(userInfo);
       return true;

    }

    /**
     * 业务执行之后:分配cookie，让浏览器保存【临时用户是一定要封装的】
     * 备注：
     * ① 业务执行后，让浏览器记录cookie以及临时用户user-key（过期时间一个月），就是方便下次打开浏览器还能查看到
     * ② 用户一个月后再登录，如果有redis持久化的数据，也是可以查看到”购物车“内已有的操作
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,Object handler,ModelAndView modelAndView)throws Exception{
        UserInfoTo userInfoTo = threadLocal.get();
        if(userInfoTo.isTempUser()){        //如果是临时用户才需要放cookie【避免每次打开浏览器都延长过期时间，不重要】
            Cookie cookie = new Cookie(CartConstant.TEMP_USER_COOKIE_NAME, userInfoTo.getUserKey());
            //让浏览器保存cookie，过期时间为一个月
            cookie.setDomain("gulimall.com");
            //持续的延长用户的过期时间
            cookie.setMaxAge(CartConstant.TEMP_USER_COOKIE_TIMEOUT);
            response.addCookie(cookie);
        }
    }

    /**
     * 备注：
     * 拦截器和过滤器都是对页面url进行操作的
     */
}
