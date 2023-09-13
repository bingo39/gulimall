package com.atguigu.gulimall.product;

//import com.aliyun.oss.ClientException;
//import com.aliyun.oss.OSS;
//import com.aliyun.oss.OSSClientBuilder;
//import com.aliyun.oss.OSSException;
//import com.aliyun.oss.model.PutObjectRequest;
//import com.aliyun.oss.model.PutObjectResult;

//import com.aliyun.oss.OSS;

import com.atguigu.gulimall.product.dao.AttrGroupDao;
import com.atguigu.gulimall.product.dao.SkuSaleAttrValueDao;
import com.atguigu.gulimall.product.entity.BrandEntity;
import com.atguigu.gulimall.product.service.AttrGroupService;
import com.atguigu.gulimall.product.service.BrandService;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.SkuItemSaleAttrVo;
import com.atguigu.gulimall.product.vo.SpuItemAttrGroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
// @RunWith注解：使用测试。@RunWith(SpringhRunner.class)就是用springRunner容器来测试
@SpringBootTest
public class GulimallProductApplicationTests {

    // 单元测试，看product项目能否正常启动

    @Autowired
    CategoryService categoryService;

    @Autowired
    BrandService brandService;

//    @Autowired(required = false)
    //OSS ossClient;
//    @Autowired
//    BrandEntity brandEntity;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    AttrGroupService attrGroupService;

    @Autowired
    SkuSaleAttrValueDao skuSaleAttrValueDao;

    @Test
    public void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        //增
//        brandEntity.setDescript("");
//        brandEntity.setName("射雕英雄传");
//        brandService.save(brandEntity);
//        System.out.println("brandService保存成功");

        //改
//        brandEntity.setBrandId(1L);
//        brandEntity.setName("鹿鼎记");
//        brandService.updateById(brandEntity);

        //查
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1));
        list.forEach((item) -> {
            System.out.println("遍历出来的数据：" + item);
        });
    }

//    @Test
//    public void testUpload() throws FileNotFoundException {
//        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
//        String endpoint = "oss-cn-guangzhou.aliyuncs.com";
//        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
//                // 这里填的不是登录阿里云的账号密码，而是子账户。保护安全性
//        String accessKeyId = "LTAI5tMRYRKGwEQD3Gjexogq";
//        String accessKeySecret = "SQXXvsVa01dAyXaldoUAQ5pZSPNnVm";
//        // 填写Bucket名称，例如examplebucket。
//        String bucketName = "direct-rent";
//        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
//        String objectName = "gulimall/39(1).jpg";
//        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
//        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
//        String filePath= "D:\\Desktop\\39(1).jpg";
//
//        // 创建OSSClient实例。
//        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
//
//        try {
//            InputStream inputStream = new FileInputStream(filePath);
//            // 创建PutObjectRequest对象。
//            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
//            // 设置该属性可以返回response。如果不设置，则返回的response为空。
//            putObjectRequest.setProcess("true");
//            // 创建PutObject请求。
//            PutObjectResult result = ossClient.putObject(putObjectRequest);
//            // 如果上传成功，则返回200。
//            System.out.println(result.getResponse().getStatusCode());
//        } catch (OSSException oe) {
//            System.out.println("Caught an OSSException, which means your request made it to OSS, "
//                    + "but was rejected with an error response for some reason.");
//            System.out.println("Error Message:" + oe.getErrorMessage());
//            System.out.println("Error Code:" + oe.getErrorCode());
//            System.out.println("Request ID:" + oe.getRequestId());
//            System.out.println("Host ID:" + oe.getHostId());
//        } catch (ClientException ce) {
//            System.out.println("Caught an ClientException, which means the client encountered "
//                    + "a serious internal problem while trying to communicate with OSS, "
//                    + "such as not being able to access the network.");
//            System.out.println("Error Message:" + ce.getMessage());
//        } finally {
//            if (ossClient != null) {
//                ossClient.shutdown();
//            }
//            System.out.println("上传完成");
//        }
//    }

    // 用Spring Cloud AliCloud OSS代替上面的原生
    @Test
    public void testUpload2() throws FileNotFoundException {
        // ossClient.putObject("direct-rent", "392.jpg", new FileInputStream("D:\\Desktop\\39.jpg"));
        System.out.println("上传成功");
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    public void test3() {
        short temp;
        short dates = 4;
        temp = dates;
    }

    // 测试redis
    @Test
    public void testStringRedisTemplate() {
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();

        // 保存
        ops.set("hello", "word_" + UUID.randomUUID().toString());
        // 查询
        String hello = ops.get("hello");
        System.out.println("之前保存的数据" + hello);
    }

    // 测试redisson
    @Test
    public void testRedisson() {
        System.out.println("创建成功redisson对象" + redissonClient);
    }

    /**
     * 测试商品sku组合详情
     */
    @Test
    public void ProductSkuAttrTest() {
        List<SpuItemAttrGroupVo> attrGroupVos = attrGroupService.getAttrGroupWithAttrsBySpuId(13L, 225L);
        System.out.println("查出来的数据" + attrGroupVos);
    }

    /**
     * 测试spu销售组合
     */
    @Test
    public void ProductSpuAttrTest() {
        List<SkuItemSaleAttrVo> saleAttrsBySpuId = skuSaleAttrValueDao.getSaleAttrsBySpuId(13L);
        System.out.println("查出来spu数据：" + saleAttrsBySpuId);
    }


}
