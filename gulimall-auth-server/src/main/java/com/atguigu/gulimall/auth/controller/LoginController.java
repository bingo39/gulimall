package com.atguigu.gulimall.auth.controller;

import com.alibaba.fastjson.TypeReference;
import com.atguigu.common.constant.AuthServerConstant;
import com.atguigu.common.exception.BizCodeEnume;
import com.atguigu.common.utils.R;
import com.atguigu.common.vo.MemberRespVo;
import com.atguigu.gulimall.auth.feign.MemberFeignService;
import com.atguigu.gulimall.auth.feign.ThirdPartFeignService;
import com.atguigu.gulimall.auth.vo.UserLoginVo;
import com.atguigu.gulimall.auth.vo.UserRegistVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
public class LoginController {

    @Autowired
    private ThirdPartFeignService thirdPartFeignService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private MemberFeignService memberFeignService;

    /**
     * 需求&思考：只是发送一个请求直接跳转到一个页面【类似下面controller方法】，不需要其他操作，只是单纯的跳转
     * 可以考虑springMVC viewcontroller;可以将请求和页面映射过来，作用：省略写下面的空方法
     * 【在config包中查看】
     */

//    @GetMapping("/login.html")
    public String loginPage(){
        return "login";
    }

//    @GetMapping("/register.html")
    public String registerPage(){
        return "register";
    }

    @ResponseBody
    @GetMapping("/sms/sendCode")
    public R sendCode(@RequestParam("phone") String phoneNumber){
        //TODO 1.接口防刷
        //1.1验证码频率
        String redisKey = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + phoneNumber);
        if(!StringUtils.isEmpty(redisKey)){
            long l = Long.parseLong(redisKey.split("_")[1]);
            //系统为毫秒单位
            if(System.currentTimeMillis()-l <60*1000){
                //60秒内不能再发
                return R.error(BizCodeEnume.SMS_CODE_EXCEPTION.getCode(), BizCodeEnume.SMS_CODE_EXCEPTION.getMsg());
            }
        }

        //随机验证码
//        String substring = "code:"+UUID.randomUUID().toString().substring(0, 4);  //【北京深智恒际科技有限公司】这个验证码接口无法接收字母;且长度只有
        int random = new Random().nextInt(9);
        String substring = Integer.toString(random).substring(0,4);
        //2.验证码校验
            //redis：key-phone,value-code 格式【sms:code:159xxxx123->123】
            //key:前缀+手机号，value:验证码，消亡时间，时间单位
        redisTemplate.opsForValue().set(AuthServerConstant.SMS_CODE_CACHE_PREFIX+phoneNumber,substring+"_"+System.currentTimeMillis(),10, TimeUnit.MINUTES);

        thirdPartFeignService.sendCode(phoneNumber,"code:"+substring);
        return R.ok();
    }

    // 重定向携带数据。利用session原理。将数据放在session中。
    // 只要跳到下一个页面取出这个数据以后，session里面的数据就会删掉
    // TODO【分布式情况下，session会存在问题】
    @PostMapping("/register")
    public String register(@Valid UserRegistVo userRegistVo, BindingResult result, Model model, RedirectAttributes redirectAttributes){
//BindingResult:持有验证运行的结果，也就是配合着@Valid注解校验bean的结果
        if(result.hasErrors()){
            Map<String, String> errors = result.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
//            model.addAttribute("errors",errors);
            redirectAttributes.addFlashAttribute("errors",errors);
             //return "register";      //转发模式，会自行拼串前后缀,由thymeleaf重新渲染 【会产生的问题：刷新页面导致模板重复提交】
            //重定向模式转发，【前端发送的服务是post，而register.html页面是转发后接收的页面，这里默认只是支持get【重定向不会携带数据，即post】，会产生的问题是： Request method 'POST' not supported】
            return "redirect:http://auth.gulimall.com/register.html";
        }

        //真正注册，调用远程服务进行注册
            //1.校验验证码
        String code = userRegistVo.getCode();
        String s = redisTemplate.opsForValue().get(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegistVo.getPhone());
        if(!StringUtils.isEmpty(s)){
            if(code.equals(s.split("_")[0])){
                //校验成功，删除验证码,令牌机制
                redisTemplate.delete(AuthServerConstant.SMS_CODE_CACHE_PREFIX + userRegistVo.getPhone());
                //调用gulimall-member模块，注册进数据库
                R register = memberFeignService.register(userRegistVo);
                if(register.getCode()==0){
                    //成功注册
                    return "redirect:http://auth.gulimall.com/login.html";
                }else {
                    Map<String, String> errors = new HashMap<>();
                    errors.put("msg",register.getData("msg",new TypeReference<String>(){}));
                    return "redirect:http://auth.gulimall.com/register.html";
                }

            }else{
                HashMap<String, String> errors = new HashMap<>();
                errors.put("code","验证码错误");
                redirectAttributes.addFlashAttribute("errors",errors);
                //校验出错，返回注册页
                return "redirect:http://auth.gulimall.com/register.html";
            }
        }else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("code","验证码错误");
            redirectAttributes.addFlashAttribute("errors",errors);
            //校验出错，返回注册页
            return "redirect:http://auth.gulimall.com/register.html";
        }

        //注册成功回登录页
            //由于config包映射了login.html的地址，所以这里返回不需要用全路径：http://auth.gulimall.com/login.html
//        return "redirect:/login.html";
    }

    @PostMapping("/login")
    public String login(UserLoginVo vo, RedirectAttributes redirectAttributes, HttpSession session){
        //远程登录
        R login = memberFeignService.login(vo);
        if(login.getCode() == 0){
            //成功
            MemberRespVo data = login.getData("data", new TypeReference<MemberRespVo>() {
            });
            session.setAttribute(AuthServerConstant.lOGIN_USER,data);
            login.getData("data",new TypeReference<MemberRespVo>(){});
            return "redirect:http://gulimall.com";
        }else {
            HashMap<String, String> errors = new HashMap<>();
            errors.put("msg",login.getData("msg",new TypeReference<String>(){}));
            redirectAttributes.addFlashAttribute("errors",errors);
            return "redirect:http://auth.gulimall.com/login.html";
        }
    }

    /**
     * 补充完善：登录页跳转
     * 登录后，再次访问登录页面，也会跳到gulimall.com,而不是auth.gulimall.com
     * 备注：不用addViewControllers方法做url映射，不然不方便写逻辑，还是直接用controller方法接收
     */
    @GetMapping("/login.html")
    public String LoginPage(HttpSession httpSession){
        Object attribute = httpSession.getAttribute(AuthServerConstant.lOGIN_USER);
        if(attribute == null){
            // 没登陆过，发送登录
            return "login";
        }else {
            //登陆过，重定向到gulimall.com
            return "redirect:http://gulimall.com";
        }
    }



    /**
     * 备注：
     *① 解决重定向项共享数据【即可以获取到post请求】
     * RedirectAttributes是springmvc提供的内置方法，作用：重定向视图，并且携带数据
     */
}
