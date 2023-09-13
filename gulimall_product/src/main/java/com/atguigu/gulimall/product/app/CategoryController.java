package com.atguigu.gulimall.product.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gulimall.product.entity.CategoryEntity;
import com.atguigu.gulimall.product.service.CategoryService;
import com.atguigu.common.utils.R;


/**
 * 商品三级分类
 *
 * @author bingo39
 * @email bingo815036@163.com
 * @date 2023-04-21 06:29:20
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 功能：
     * 查出所有分类以及子分类，以树形结构组装起来
     */
    @RequestMapping("/list/tree")
    public R list() {

        List<CategoryEntity> entityList = categoryService.listWithTree();
        return R.ok().put("queryCateData", entityList);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId) {
        CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("categoryData", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category) {
        categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改分类
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category) {
        categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     *
     * @RequestBoy ：获取请求体，必须发送POST请求
     * SpringMVC会自动将请求体的数据（json），转换为对应的对象
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds) {
        // 1.检查当前删除的菜单，是否被别的地方引用
        categoryService.removeByIds(Arrays.asList(catIds));
        System.out.println("接收到删除信息");
        return R.ok();
    }

    /**
     * 批量修改
     * -- 前端传updatNodes数组，需要修改里面的信息
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category) {
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

}
