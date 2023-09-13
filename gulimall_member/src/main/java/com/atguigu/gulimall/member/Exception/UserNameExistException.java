package com.atguigu.gulimall.member.Exception;

public class UserNameExistException extends RuntimeException{

    public UserNameExistException(){
        super("用户名存在异常");
    }
}
