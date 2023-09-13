package com.atguigu.gulimall.thirdparty;

import com.aliyun.oss.OSS;
import com.atguigu.gulimall.thirdparty.component.SmsComponent;
import com.atguigu.gulimall.thirdparty.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class GulimallThirdPartyApplicationTests {

    @Autowired
    OSS ossClient;

    @Autowired
    SmsComponent smsComponent;

    /**
     * 图片上传功能测试
     */
    @Test
    public void testUpload2() throws FileNotFoundException {
        ossClient.putObject("direct-rent", "你说法制我都觉得好好笑.jpg", new FileInputStream("D:\\Desktop\\你说法制我都绝好好笑.jpeg"));
        System.out.println("上传成功");
    }

    /**
     * 测试短信验证功能
     */
    @Test
    public void sendSms(){
        String host = "https://dfsns.market.alicloudapi.com";
        String path = "/data/send_sms";
        String method = "POST";
        String appcode = "f8cedaeaaaf2455ebd275ddc248ad277";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
            //备注：所谓content-type即是postman的body的选项标签
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "code:1234678");
        bodys.put("template_id", "CST_ptdie100");
        bodys.put("phone_number", "15913542021");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSendCode2(){
        smsComponent.sendSmsCode("15913542021","3333");
    }
}
