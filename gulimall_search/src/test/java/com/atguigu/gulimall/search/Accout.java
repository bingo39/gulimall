/**
 * 测试类的账号信息对象
 */
package com.atguigu.gulimall.search;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Accout {

    private int account_number;
    private int balance;
    private String firstname;
    private String lastname;
    private int age;
    private String gender;
    private String address;
    private String employer;
    private String email;
    private String city;
    private String state;

}