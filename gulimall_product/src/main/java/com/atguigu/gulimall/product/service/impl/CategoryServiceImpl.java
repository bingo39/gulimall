package com.atguigu.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.gulimall.product.service.CategoryBrandRelationService;
import com.atguigu.gulimall.product.vo.Catelog2VO;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.atguigu.common.utils.PageUtils;
import com.atguigu.common.utils.Query;

import com.atguigu.gulimall.product.dao.CategoryDao;
import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    //本地缓存
//    private Map<String,Object> cache = new HashMap();

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //步骤1 查出所有分类
        // 继承ServiceImpl,而CategoryDao也是继承于baseMapper进行查询的，且已经把CategoryDao放入到ServiceImpl，所以不用再注入CategoryDao
        List<CategoryEntity> allEntities = baseMapper.selectList(null);
        //步骤2 组装所有父子分类
        //2.1) 找到所有的一级分类    （parent_id为0）
        List<CategoryEntity> level1Menus = allEntities.stream().filter((entity) ->
                //  { return entity.getParentCid() == 0; }
                entity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, allEntities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return menu1.getSort() - menu2.getSort();
        }).collect(Collectors.toList());
        /**
         * lamdba解释：
         * stream流操作
         * xxx.stream.filter    => 转换为流
         * return (可以省略if while之类的判断) 返回需要的数据（过滤操作）
         * xxx.collect(Collectors.toList())  =>把流数据转为集合，其中Collectors.toList表示转为List集合
         *  sorted(Comparator) 排序方法
         *   map()  将一个流中的值转换为另一个类型的流
         * 拓展：
         * 只有一个表达式可以省略{} 和 return
         */


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        // TODO 1.检查当前删除的菜单，是否被别的地方引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> pathList = new ArrayList();
        findParentPath(catelogId, pathList);
        return pathList.toArray(new Long[pathList.size()]);
    }

    /**
     * 级联更新所有关联数据
     *
     * @param category
     * @CacheEvict:缓存失效模式 【要删除多个缓存分区的办法】
     * 1. 同时进行多种缓存操作 @Caching组合操作
     * 2. 指定删除某个分区下的所有数据   @CacheEvict的allEntries = true属性
     * 约定：
     * 存储同一个类型的数据，都指定为同一个分区
     */
    //备注：SpringCache的注解用的是SPL表达式，字符串一定要加单引号
    // 单操作
    @CacheEvict(value = "category", allEntries = true) //删除这个category分区下的所有缓存【这是spring中的一个标识，redis中是无法查看到】
    // 组合操作
//    @Caching(evict = {
//            @CacheEvict(value = "category",key = "'getLevel1Categorys'"),
//            @CacheEvict(value = "category",key = "'getCatalogJson'")
//    })
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);

        //更新级联表单
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());

    }

    //每一个需要缓存的数据都要指定放到哪个名字的缓存【缓存的分区（按照业务类型分）】
    //触发将数据保存到缓存的操作【如果缓存中有，方法就不会调用；否则会调用方法将方法结果放入缓存中】 属性sync:给缓存加锁，默认为false
    @Cacheable(value = {"categorys"}, key = "#root.method.name", sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        System.out.println("getLevel1Catagorys.......");
        List<CategoryEntity> parentCid = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return parentCid;
    }

    /**
     * TODO 产生堆外内存溢出：OutOfDirectMemoryError
     * 1) springboot2.0以后默认使用lettuce作为操作redis的客户端。其中lettuce使用netty进行网络通信
     * 2）lettuce的bug导致netty堆外内存溢出 -Xmx300m:netty如果没有指定堆外内存，默认使用-Xmx300m
     * 缓解之策：通过-Dio.netty.maxDirectMemory进行设置
     * 解决方案：
     * 【-Dio.netty.maxDirectMemory设置调大堆外内存只是缓解，但迟早还是会存在堆外内存溢出】
     * ① 升级lettuce客户端
     * ② 切换使用jedis（老版本）
     */

    @Cacheable(value = "category", key = "#root.method.name")
    @Override
    public Map<String, List<Catelog2VO>> getCataLogJson() {
        System.out.println("查询了数据库。。。。");
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        // 1.查出所有1级分类
        List<CategoryEntity> level1Categorys = getLevel1Categorys();
        //2.封装数据
        Map<String, List<Catelog2VO>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每个一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntityList = getParent_cid(selectList, v.getCatId());
            // 2.封装上面结果
            List<Catelog2VO> catelog2VOs = null;
            if (categoryEntityList != null) {
                catelog2VOs = categoryEntityList.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //2.1 找当前二级分类的三级分类id
                    List<CategoryEntity> level3CateLog = getParent_cid(selectList, l2.getCatId());
                    if (level3CateLog != null) {
                        List<Catelog2VO.Catelog3Vo> collect = level3CateLog.stream().map(l3 -> {
                            //2.2封装成指定格式
                            Catelog2VO.Catelog3Vo catelog3Vo = new Catelog2VO.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }
                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOs;
        }));

        return parentCid;

    }


    //整合redis-缓存中获取

    /**
     * gulimall对于数据一致性的解决方案：
     * 1.缓存的所有数据都有过期时间，数据过期下一次查询触发主动更新
     * 2.读写数据的时候，加上分布式的读写锁【经常写，经常读】
     */
    public Map<String, List<Catelog2VO>> getCataLogJson2() {
        /**
         * 备注：
         * 给缓存中放json字符串，拿出来的json字符串，调用逆转为能用的对象类型
         * -- 序列化与反序列化
         */
        /**
         * 1.空结果缓存：解决缓存穿透
         * 2.设置过期时间（加随机值）：解决缓存雪崩
         * 3.加锁：解决缓存击穿
         */

        //缓存1.加入缓存逻辑，缓存中存的数据是json字符串
        //json好处是，跨平台，兼容性强
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (StringUtils.isEmpty(catalogJson)) {
            //缓存2.缓存中没有就通过数据库查找
            System.out.println("缓存不命中.......查询数据库.....");
//            Map<String, List<Catelog2VO>> catalogJsonFromDb = getCataLogJsonFromDbWithLocalLock();
            Map<String, List<Catelog2VO>> catalogJsonFromDb = getCataLogJsonFromDbWithRedisLock();
            return catalogJsonFromDb;
        }

        System.out.println("缓存命中.......直接返回.....");
        //JSON.parseObject(String text, TypeReference<T> type, Feature... features)
        Map<String, List<Catelog2VO>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2VO>>>() {
        });
        return result;

    }
    // 从数据库中获取catalogJson并封装数据

    //使用分布式锁【redisson】

    /**
     * 缓存里面的数据如何和数据库的数据保持一直
     */
    public Map<String, List<Catelog2VO>> getCataLogJsonFromDbWithRedissonLock() {
        //使用redisson框架管理分布式锁    锁的名字-》锁的粒度，越细越好
        //锁的粒度：具体缓存的是某个数据，例如：11号商品，product-11-lock
        RLock lock = redissonClient.getLock("catalogJson-lock");
        //加锁
        lock.lock();
        Map<String, List<Catelog2VO>> dataFromDb;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }
        return dataFromDb;
    }

    //使用分布式锁【用脚本控制】
    public Map<String, List<Catelog2VO>> getCataLogJsonFromDbWithRedisLock() {
        //分布式锁1：去redis占锁
        //setIfAbsent方法相当于redis的setnx
        //设置“占坑”和”过期时间“是原子性，也就是同步
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            //加锁成功...执行业务
            System.out.println("获取分布式锁成功..........");
            Map<String, List<Catelog2VO>> dataFromDb;
            try {
                dataFromDb = getDataFromDb();
            } finally {
                //原子加锁原子解锁
                String script = "if redis.call(\"get\",KEY[1])==ARGV[1]\n" +
                        "    then\n" +
                        "    \treturn redis.call(\"del\",KEY[1])\n" +
                        "    else\n" +
                        "    \treturn 0\n" +
                        "    end;";
                // KEY[1]通过Arrays.asList("lock")，ARGV[1]通过uuid
                stringRedisTemplate.execute(new DefaultRedisScript<Integer>(script, Integer.class), Arrays.asList("lock"), uuid);
            }
//            //分布式锁2：设置过期时间：
//                //备注：【要考虑中间间断的问题】以及原子性问题【也即是“占锁”和“过期时间”要同步，不能分开设置】
//            stringRedisTemplate.expire("lock",30,TimeUnit.SECONDS);
            /**
             * 备注：请求redis也是需要花费时间的【“获取值对比”和“对比删除成功”也必须是原子操作】
             *
             *             String lockValue = stringRedisTemplate.opsForValue().get("lock");
             *             if(uuid.equals(lockValue)){
             *                 //释放锁
             *                 stringRedisTemplate.delete("lock");
             *             }
             *             return dataFromDb;
             */

            return dataFromDb;

        } else {
            //加锁失败....等待100ms，重试【自旋】
            System.out.println("获取分布式锁失败等待重试........");
            return getCataLogJsonFromDbWithRedisLock(); //自旋方式

        }
    }

    //使用本地锁
    public Map<String, List<Catelog2VO>> getCataLogJsonFromDbWithLocalLock() {

        //本地缓存优化：1.如果缓存中有，就用缓存的
//        Map<String, List<Catelog2VO>> catalogJson = (Map<String, List<Catelog2VO>>) cache.get("catalogJson");

        // 加锁：解决缓存击穿
        //只要是同一把锁，就能锁住需要这个锁的所有线程
        /**
         * 加锁方案：
         * 1.synchronized(this):Springboot所有的组件在容器中都是单例的
         * 2.得到锁以后，应该再去缓存确定一次，没有才需要db查询
         */
        //TODO 本地锁：synchronized,JUC包（lock）
        // 在分布式情况下，想要锁住所有，必须用分布式
        synchronized (this) {
            return getDataFromDb();
        }

        // 本地缓存优化：1.缓存没有就放入缓存中
//        cache.put("catalogJson",parentCid);
//        if(cache.get("catalogJson") == null){
//
//        }else{
//            return catalogJson;
//        }
    }

    private Map<String, List<Catelog2VO>> getDataFromDb() {
        String catalogJson = stringRedisTemplate.opsForValue().get("catalogJson");
        if (!StringUtils.isEmpty(catalogJson)) {
            //缓存不为空，直接返回
            Map<String, List<Catelog2VO>> result = JSON.parseObject(catalogJson, new TypeReference<Map<String, List<Catelog2VO>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库。。。。");

        /**
         * 优化1：将数据库的多次查询变为一次【即整理出“数据库查询方法”，查出结果放入缓存中】
         */
        List<CategoryEntity> selectList = baseMapper.selectList(null);

        // 1.查出所有1级分类
        List<CategoryEntity> level1Categorys = getLevel1Categorys();
        //2.封装数据
        Map<String, List<Catelog2VO>> parentCid = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1.每个一级分类,查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntityList = getParent_cid(selectList, v.getCatId());
            // 2.封装上面结果
            List<Catelog2VO> catelog2VOs = null;
            if (categoryEntityList != null) {
                catelog2VOs = categoryEntityList.stream().map(l2 -> {
                    Catelog2VO catelog2VO = new Catelog2VO(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //2.1 找当前二级分类的三级分类id
                    List<CategoryEntity> level3CateLog = getParent_cid(selectList, l2.getCatId());
                    if (level3CateLog != null) {
                        List<Catelog2VO.Catelog3Vo> collect = level3CateLog.stream().map(l3 -> {
                            //2.2封装成指定格式
                            Catelog2VO.Catelog3Vo catelog3Vo = new Catelog2VO.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2VO.setCatalog3List(collect);
                    }
                    return catelog2VO;
                }).collect(Collectors.toList());
            }
            return catelog2VOs;
        }));

        //缓存3.查找到的数据再放入缓存中去,将对象转换为json放入缓存
        String s = JSON.toJSONString(parentCid);
        //设置空结果
        //stringRedisTemplate.opsForValue().set("catalogJson",0);
        //设置缓存：key,value,过期时间，过期时间单位
        stringRedisTemplate.opsForValue().set("catalogJson", s, 1, TimeUnit.DAYS);

        return parentCid;
    }

    //优化1：
    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        // return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        List<CategoryEntity> collect = selectList.stream().filter(item -> {
            return item.getParentCid().equals(parent_cid);
        }).collect(Collectors.toList());
        return collect;
    }

    // 递归查找父分类
    private List<Long> findParentPath(Long catelogId, List<Long> pathList) {
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            this.findParentPath(byId.getParentCid(), pathList);
            pathList.add(catelogId);
        }
        if (byId.getParentCid() == 0) {
            pathList.add(catelogId);
        }
        return pathList;

    }

    // 递归查找children
    private List<CategoryEntity> getChildrens(CategoryEntity rootEntity, List<CategoryEntity> allMenu) {
        List<CategoryEntity> childrenEntities = allMenu.stream().filter(categoryEntity -> {
            // 找到对应的子菜单
            return categoryEntity.getParentCid() == rootEntity.getCatId();
        }).map(categoryEntity -> {
            // 找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, allMenu));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return childrenEntities;
    }

}