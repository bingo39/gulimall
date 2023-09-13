package com.atguigu.gulimall.search;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gulimall.search.config.GulimallElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

// p127
// 不懂得参考DSL，elasticSearch官方文档比较系统不好查
@SpringBootTest
@RunWith(SpringRunner.class)
public class GulimallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Test
    public void contextLoads() {
        System.out.println(client);
    }

    @Test
    public void searchData() throws IOException {
//      1.创建检索请求
        SearchRequest searchRequest = new SearchRequest();
        //指定索引
        searchRequest.indices("bank");
        //指定DSL,检索条件
        // SearchSourchBuilder sourceBuilde 封装所有条件
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //1.1) 构造检索条件
        sourceBuilder.query(QueryBuilders.matchQuery("address", "mill"));
        //1.2）按照年龄的值分布进行聚合
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAGG").field("age").size(10);
        sourceBuilder.aggregation(ageAgg);
        //1.3)计算平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAVG").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        System.out.println("检索条件" + sourceBuilder.toString());
        searchRequest.source(sourceBuilder);

//       2.执行检索
        SearchResponse searchResponse = client.search(searchRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);

//        3.分析结果 searchResponse
        System.out.println(sourceBuilder.toString());
        //3.1) 获取所有查到的数据
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit searchHit : searchHits) {
//            元信息：searchHit.getIndex();searchHit.getId();
            String sourceAsString = searchHit.getSourceAsString();
            JSONObject accout = JSON.parseObject(sourceAsString);
            System.out.println("accout值：" + accout);
        }
        //3.2) 获取这次检索到的聚合信息
        Aggregations aggregations = searchResponse.getAggregations();
        Terms ageAGG = aggregations.get("ageAGG");
        for (Terms.Bucket bucket : ageAGG.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();
            System.out.println("年龄：" + keyAsString + "===》分布人数：" + bucket.getDocCount());
        }
        Avg balanceAVG = aggregations.get("balanceAVG");
        System.out.println("平均薪资" + balanceAVG.getValue());
    }

    /**
     * 测试index功能
     * 存储/更新
     */
    @Test
    public void indexTest() throws IOException {
        IndexRequest indexRequest = new IndexRequest("users");
        indexRequest.id("1");
        // 存储方式一：用键值对形式存入
//        indexRequest.source("userName","zhangsan","age","18","gender","男");
        //方式二：用json形式存储
        User user = new User();
        user.setUserName("张三");
        user.setAge(18);
        user.setGender("男");
        String jsonString = JSON.toJSONString(user);
        indexRequest.source(jsonString, XContentType.JSON);    //要保存的内容

        //执行操作
        IndexResponse index = client.index(indexRequest, GulimallElasticSearchConfig.COMMON_OPTIONS);
        // 提取有用的响应数据
        System.out.println(index);

    }

    //模拟json数据
    @Data
    class User {
        private String userName;
        private Integer age;
        private String gender;
    }

}
