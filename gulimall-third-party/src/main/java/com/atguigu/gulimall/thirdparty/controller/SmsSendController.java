package com.atguigu.gulimall.thirdparty.controller;

import com.atguigu.common.utils.R;
import com.atguigu.gulimall.thirdparty.component.SmsComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sms")
public class SmsSendController {

    @Autowired
    private SmsComponent smsComponent;

    /**
     * 备注：
     * 该服务提供是由别的服务调用的，而不是页面直接调用的。所以添加@GetMapping做请求路径处理
     */
    @GetMapping("/sendCode")
    public R sendCode(@RequestParam("phone") String phoneNumber, @RequestParam("content") String content){
        smsComponent.sendSmsCode(phoneNumber,content);
        return R.ok();
    }
}
