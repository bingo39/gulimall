package com.atguigu.gulimall.search.controller;

import com.atguigu.gulimall.search.service.MallSearchService;
import com.atguigu.gulimall.search.vo.SearchParam;
import com.atguigu.gulimall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    private MallSearchService mallSearchService;

    /**
     * springMVC特性：自动将页面提交过来的所有请求查询参数，封装成指定的对象
     */
    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request) {

        // 2.面包屑功能
        param.set_queryString(request.getQueryString());

        //1.根据传递来的页面查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result", result);

        // 返回结果为跳转页面【nginx重定向】，由于thymeleaf标明了前缀为templates,后缀为.html；可省略
        return "list";
    }
}
