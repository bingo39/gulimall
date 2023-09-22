package com.atguigu.gulimall.cart.controller;

import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.vo.Cart;
import com.atguigu.gulimall.cart.vo.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;


@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * 参考：jd做法
     * 浏览器有一个cookie:user-Key 标识用户身份，一个月后过期
     * 如果用户是首次使用购物车功能，都会给一个临时的用户身份;
     *
     * 登录：session有
     * 没登录：按照cookie里面带来user-key来做
     * 第一次：如果没有临时用户，帮忙创建一个临时用户
     *
     */
    @GetMapping("/cart.html")
    public String CartListPage(Model model) throws ExecutionException, InterruptedException {

        Cart cart = cartService.getCart();
        model.addAttribute("cart",cart);
        return "cartList";
    }

    /**
     * 添加商品到购物车
     * @return
     */
    @GetMapping("/addToCart")
    public String addToCart(@RequestParam("skuId") Long skuId, @RequestParam("num")Integer num, RedirectAttributes ra) throws ExecutionException, InterruptedException {
        cartService.addToCart(skuId,num);
        ra.addAttribute("skuId",skuId);
        return "redirect:http://cart.gulimall.com/addToCartSuccess.html";
    }

    /**
     * 展现购物车内商品属性
     */
    @GetMapping("/addToCartSuccess.html")
    public String addToCartSuccessPage(@RequestParam("skuId") Long skuId,Model model){
        //重定向到成功页面，再次查询购物车数据即可
        CartItem item =cartService.getCartItem(skuId);
        model.addAttribute("item",item);
        //重定向携带数据不可以使用"redirect:"【该重定向方式页面只能刷新一次，再次刷新后续提交的的数据就不呈现】
        return "success";
    }

    /**
     * 勾选某个购物项
     */
    @GetMapping("/checkItem")
    public String checkItem(@RequestParam("skuId") Long skuId,@RequestParam("check") Integer check){
        cartService.checkItem(skuId,check);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 购物车展示页面改变数量
     */
    @GetMapping("/countItem")
    public String countItem(@RequestParam("skuId") Long skuId,@RequestParam("num") Integer num){

        cartService.checkItemCount(skuId,num);
        return "redirect:http://cart.gulimall.com/cart.html";
    }

    /**
     * 删除购物项
     */
    @GetMapping("/deletItem")
    public String deletItem(@RequestParam("skuId") Long skuId){
        cartService.deletItem(skuId);
        return "redirect:http://cart.gulimall.com/cart.html";
    }
}

/**
 *备注：
 *   ①   RedirectAttributes 类作用：
 *      RedirectAttributes.addFlashAttribute();将数据放在session里面可以在页面取出，但只能取一次
 *      RedirectAttributes.addAttribute("skuId",skuId);将数据放在url路径后面，做请求
 */
