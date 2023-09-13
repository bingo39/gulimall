package com.atguigu.gulimall.member.Exception;

public class PhoneExistException extends RuntimeException{

    public PhoneExistException(){
        super("手机号存在异常");
    }

}
