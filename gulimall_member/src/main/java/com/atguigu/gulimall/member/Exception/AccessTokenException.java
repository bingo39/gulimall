package com.atguigu.gulimall.member.Exception;

public class AccessTokenException extends RuntimeException {
    public AccessTokenException(Integer statusCode,String reasonPhrase){
        super("tokenAccess无法获取用户id,状态码："+statusCode+"，原因："+reasonPhrase);
    }
}
