package com.atguigu.gulimall.order.web;

import com.atguigu.common.exception.NoStockException;
import com.atguigu.gulimall.order.service.OrderService;
import com.atguigu.gulimall.order.vo.OrderConfirmVo;
import com.atguigu.gulimall.order.vo.OrderSubmitVo;
import com.atguigu.gulimall.order.vo.SubmitOrderResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ExecutionException;


@Controller
public class OrderWebController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/toTrade")
    public String toTrade(Model model) throws ExecutionException, InterruptedException {
        OrderConfirmVo confirmVo = orderService.confirmOrder();
        model.addAttribute("orderConfirmData",confirmVo);
        //展示订单确认数据
        return "confirm";
    }

    //下单功能
    @PostMapping("/submitOrder")
    public String submitOrder(OrderSubmitVo vo, Model model, RedirectAttributes redirectAttributes){

        try {
            //下单成功来到支付选择
            //下单失败回到订单确认页重新确认订单信息
            SubmitOrderResponseVo responseVo = orderService.submitOrder(vo);
            if(responseVo.getCode()==0){
                model.addAttribute("submitOrderResponse",responseVo);
                return "pay";
            }else{
                String msg="下单失败,";
                switch (responseVo.getCode()){
                    case 1:msg+="令牌失败，订单信息过期，请刷新再提交";break;
                    case 2:msg+="订单商品价格发生变化，请确认后再提交";break;
                    case 3:msg+="库存锁定失败，商品库存不足";break;
                }
                redirectAttributes.addFlashAttribute("msg",msg);
                return "redirect:http://order.gulimall.com/toTrade";
            }
        } catch (Exception e) {
           if(e instanceof NoStockException){
               String message = ((NoStockException) e).getMessage();
               redirectAttributes.addFlashAttribute("msg",message);
           }

            return "redirect:http://order.gulimall.com/toTrade";
        }
    }
}
