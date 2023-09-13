package com.atguigu.gulimall.product.web;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.gulimall.product.vo.Catelog2VO;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 鼠标点击分类，页面自动显示
 */

@Controller
public class IndexController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //打印线程号
        System.out.println("当前线程号：" + Thread.currentThread().getId());
        // 查出所有1级分类
        List<CategoryEntity> categoryEntityList = categoryService.getLevel1Categorys();

        /**
         * 视图拼串
         * thymeleaf默认前缀：classpath:/templates/
         * thymeleaf默认后缀：.html
         */

        model.addAttribute("categorys", categoryEntityList);
        return "index";

    }

    // 请求 index/catalog.json
    @ResponseBody
    @GetMapping("index/catalog.json")
    public Map<String, List<Catelog2VO>> getCatalogJson() {
        Map<String, List<Catelog2VO>> cataLogJson = categoryService.getCataLogJson();
        return cataLogJson;
    }

    // 测试网关响应时间
    //测试可重复锁
    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        //只要锁名一样，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");
        //加锁
        lock.lock();    //阻塞式等待
//        lock.lock(10, TimeUnit.SECONDS);    //10秒自动解锁，自动解锁时间一定要大于业务执行时间【设置了解锁时间，redisson就不会自动续期】
        try {
            System.out.println("加锁成功，执行业务。。。。");
            //模拟业务执行时间
            Thread.sleep(30000);
        } catch (Exception e) {

        } finally {
            //解锁
            System.out.println("释放锁。。。。" + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }

    // 测试读写锁
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        String s = "";
        // 加写锁
        RLock rLock = lock.writeLock();
        try {
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            stringRedisTemplate.opsForValue().set("writeValue", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }

        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        String s = "";
        RReadWriteLock lock = redissonClient.getReadWriteLock("rw-lock");
        // 加读锁
        RLock rLock = lock.readLock();
        try {
            rLock.lock();
            s = stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

}
