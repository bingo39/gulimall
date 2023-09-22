package com.atguigu.gulimall.cart.service;

import com.atguigu.gulimall.cart.vo.Cart;
import com.atguigu.gulimall.cart.vo.CartItem;

import java.util.concurrent.ExecutionException;

public interface CartService {

    /**
     * 给购物车添加数据
     */
    CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException;

    /**
     * 获取购物车的购物项（商品）属性
     * @param skuId
     * @return
     */
    CartItem getCartItem(Long skuId);

    /**
     * 获取整个购物车
     */
    Cart getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车数据
     */
    void clearCart(String carkey);

    /**
     * 勾选购物项
     */
    void checkItem(Long skuId, Integer check);

    /**
     * 修改购物项数量
     */
    void checkItemCount(Long skuId, Integer num);

    /**
     * 删除购物项
     */
    void deletItem(Long skuId);
}
