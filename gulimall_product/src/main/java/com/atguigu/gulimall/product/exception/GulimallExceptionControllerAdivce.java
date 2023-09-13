package com.atguigu.gulimall.product.exception;

import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

/**
 * 集中处理所有异常处理类
 */

@Slf4j
//@ResponseBody
//@ControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
@RestControllerAdvice(basePackages = "com.atguigu.gulimall.product.controller")
public class GulimallExceptionControllerAdivce {

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handleVaildException(MethodArgumentNotValidException e) {
        log.error("数据校验出现问题:{}，异常类型:{}", e.getMessage(), e.getClass());
        Map<String, String> errorMap = new HashMap();
        BindingResult bindingResult = e.getBindingResult();
        bindingResult.getFieldErrors().forEach((item) -> {
            // 错误提示消息；可在Entity中配置
            String defaultMessage = item.getDefaultMessage();
            // 获取错误的属性的名字
            String field = item.getField();
            errorMap.put(field, defaultMessage);
        });
        return R.error(BizCodeEnume.VAILD_EXCEPTION.getCode(), BizCodeEnume.VAILD_EXCEPTION.getMsg()).put("data", errorMap);
    }

    // 最大处理异常
    @ExceptionHandler(value = Throwable.class)
    public R handleException(Throwable throwable) {
        log.error("错误：", throwable);
        return R.error(BizCodeEnume.UNKNOW_EXCEPTION.getCode(), BizCodeEnume.UNKNOW_EXCEPTION.getMsg());
    }
}
