package com.atguigu.gulimall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.utils.R;
import com.atguigu.gulimall.cart.feign.ProductFeignService;
import com.atguigu.gulimall.cart.interceptor.CartInterceptor;
import com.atguigu.gulimall.cart.service.CartService;
import com.atguigu.gulimall.cart.to.UserInfoTo;
import com.atguigu.gulimall.cart.vo.Cart;
import com.atguigu.gulimall.cart.vo.CartItem;
import com.atguigu.gulimall.cart.vo.SkuInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProductFeignService productFeignService;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    private final String CART_PREFIX = "gulimall:cart:"; //购物车前缀

    @Override
    public CartItem addToCart(Long skuId, Integer num) throws ExecutionException, InterruptedException {

        BoundHashOperations<String, Object, Object> cartOps = getCarOps();

        String res = (String) cartOps.get(skuId.toString());
        if(StringUtils.isEmpty(res)){
            //购物车缓存没有该商品,新商品信息添加到购物车
            CartItem cartItem = new CartItem();
            //异步任务
            CompletableFuture<Void> getSkuInfoTask = CompletableFuture.runAsync(() -> {
                //1.远程查询当前要添加的商品信息
                R skuInfo = productFeignService.getSkuInfo(skuId);
                SkuInfoVo data = skuInfo.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                });
                cartItem.setCheck(true);
                cartItem.setCount(num);
                cartItem.setImage(data.getSkuDefaultImg());
                cartItem.setTitle(data.getSkuTitle());
                cartItem.setSkuId(skuId);
                cartItem.setPrice(data.getPrice());

            }, threadPoolExecutor);

            //3.远程查询sku的组合信息【多线程】
            CompletableFuture<Void> getSkuSaleAttrValues = CompletableFuture.runAsync(() -> {
                List<String> skuSaleAttrValues = productFeignService.getSkuSaleAttrValues(skuId);
                cartItem.setSkuAttr(skuSaleAttrValues);
            }, threadPoolExecutor);

            //要等异步操作都完成，才能返回数据对象
            CompletableFuture.allOf(getSkuInfoTask,getSkuSaleAttrValues).get();
            //远程调用线程结束后，在redis中保存商品【key为当前商品id】
            String json = JSON.toJSONString(cartItem);//为了方便存储，转为json串
            cartOps.put(skuId.toString(),json);
            return cartItem;
        }else {
            //购物车有该商品，修改数量count即可
            CartItem cartItem = JSON.parseObject(res, CartItem.class);
            cartItem.setCount(cartItem.getCount()+num);
            //修改完count，再转为json存入redis中
            cartOps.put(skuId.toString(),JSON.toJSONString(cartItem));
            return cartItem;
        }
    }

    @Override
    public CartItem getCartItem(Long skuId) {
        BoundHashOperations<String, Object, Object> cartOps = getCarOps();
        String str = (String) cartOps.get(skuId.toString());
        CartItem cartItem = JSON.parseObject(str, CartItem.class);
        return cartItem;
    }

    @Override
    public Cart getCart() throws ExecutionException, InterruptedException {
        Cart cart = new Cart();
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(userInfoTo.getUserId()!=null){
            //在线购物车【登录】(合并购物车)
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            redisTemplate.boundHashOps(cartKey);
                //如果临时购物车有数据，则合并
            String templeCartKey = CART_PREFIX + userInfoTo.getUserKey();
            List<CartItem> templeCartItems = getCartItems(templeCartKey);
            if(templeCartItems!=null){
                for(CartItem item:templeCartItems){
                    addToCart(item.getSkuId(),item.getCount());
                }
                //清除临时购物车的数据
                clearCart(templeCartKey);

            }
            // 获取登陆后的购物车数据【包含临时购物车和登陆后的购物车的数据】
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);

        }else {
            //.临时购物车【未登录】
            String cartKey = CART_PREFIX + userInfoTo.getUserKey();
            //获取临时购物车的所有购物项
            List<CartItem> cartItems = getCartItems(cartKey);
            cart.setItems(cartItems);
        }
        return cart;
    }

    @Override
    public void clearCart(String carkey) {
        redisTemplate.delete(carkey);
    }

    @Override
    public void checkItem(Long skuId, Integer check) {
        BoundHashOperations carOps = getCarOps();
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCheck(check==1?true:false);
        String s = JSON.toJSONString(cartItem);
        carOps.put(skuId.toString(),s);
    }

    @Override
    public void checkItemCount(Long skuId, Integer num) {
        CartItem cartItem = getCartItem(skuId);
        cartItem.setCount(num);

        BoundHashOperations carOps = getCarOps();
        carOps.put(skuId.toString(),JSON.toJSONString(cartItem));
    }

    @Override
    public void deletItem(Long skuId) {
        BoundHashOperations carOps = getCarOps();
        carOps.delete(skuId.toString());
    }

    @Override
    public List<CartItem> getUserCartItems() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        if(userInfoTo==null){
            return null;
        }else {
            String cartKey = CART_PREFIX + userInfoTo.getUserId();
            List<CartItem> cartItems = getCartItems(cartKey);
            List<CartItem> collect = cartItems.stream()
                    .filter(item -> {  //lambda过滤器
                return item.getCheck();    //只要获取选中的购物项
            }).map(item->{
                            //获取到cartItem对象后，实时更新价格
                        Long userId = userInfoTo.getUserId();
                        String data = (String)productFeignService.getPrice(userId).get("data");
                        item.setPrice(new BigDecimal(data));
                        return item;
                    }).collect(Collectors.toList());
            return collect;
        }
    }

    /**
     * 获取到指定操作的购物车
     *  redis中统一的前置操作
     * @return
     */
    private BoundHashOperations getCarOps() {
        UserInfoTo userInfoTo = CartInterceptor.threadLocal.get();
        // 1.判断用户是否登录【用临时购物车/在线购物车】
        String cartKey = "";
        if(userInfoTo.getUserId()!=null){
            //在线购物车
            cartKey = CART_PREFIX+userInfoTo.getUserId();
        }else{
            //临时购物车
            cartKey = CART_PREFIX+userInfoTo.getUserKey();
        }
        //2.需求：redis有该商品信息保存，则修改数量；无该商品信息，则添加
        //redisTemplate.boundHashOps()：绑定redis的hash操作，后续的操作都是以该key值为主
            // 补充：类似于用 redisTemplate.opsForValue().get(cartKey)一条条的在redis中查找获取
        BoundHashOperations<String, Object, Object> hashOperations = redisTemplate.boundHashOps(cartKey);
        return hashOperations;
    }

    /**
     * 获取指定操作的购物车【为了获取购物车内所有数据】
     */
    public List<CartItem> getCartItems(String cartKey){
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(cartKey);
        List<Object> values = hashOps.values();
        //将添加的商品信息放入到购物车的购物项中
        if(values!=null&&values.size()>0){
            List<CartItem> collect = values.stream().map((obj) -> {
                String str = obj.toString();
                CartItem cartItem = JSON.parseObject(str, CartItem.class);
                return cartItem;
            }).collect(Collectors.toList());
            return collect;
        }
        return null;
    }


    /**
     * 备注：
     * ① redis中存储的数据，无论临时/在线购物车，key值的格式分别是"cart:spuId"/"cart:xxxx"，key都是用”cart“作为前缀
     */
}
