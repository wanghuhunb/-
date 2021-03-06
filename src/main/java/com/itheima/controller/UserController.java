package com.itheima.controller;

import com.itheima.common.ValidateCodeUtils;
import com.itheima.entity.R;
import com.itheima.entity.User;
import com.itheima.entity.dto.LoginPhoneDto;
import com.itheima.entity.dto.UserDto;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

/**
 * 用户相关接口
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * \
     * 发送短信
     *
     * @param userDto
     * @return
     */
    @PostMapping("sendMsg")
    public R sendMsg(@RequestBody UserDto userDto) {
        //生成验证码
        String code = ValidateCodeUtils.generateValidateCode4String(4);
        //调取短信方法
        System.out.println("验证码" + code);
        //  SendSms.sendMsg(userDto.getPhone(),code);
        //把验证码放到session里面
        /*HttpSession session = request.getSession();
        session.setAttribute(userDto.getPhone(),code);*/
        //把验证码放入到redis中并设置时间为5分钟
        stringRedisTemplate.opsForValue().set(userDto.getPhone(), code);
        stringRedisTemplate.expire(userDto.getPhone(), 5, TimeUnit.MINUTES);
        return R.success("发送验证码成功");
    }

    /**
     * 登录功能
     */
    @PostMapping("/login")
    public R login(@RequestBody LoginPhoneDto LoginPhoneDto) {
        //从session中获取验证码
        HttpSession session = request.getSession();
        /*String code = (String) session.getAttribute(LoginPhoneDto.getPhone());*/
        //从redis中获取验证码
        String code = stringRedisTemplate.opsForValue().get(LoginPhoneDto.getPhone());
        if (code == null) {
            return R.error("请输入验证码");
        }
        //如果验证吗不成功
        if (!code.equalsIgnoreCase(LoginPhoneDto.getCode())) {
            return R.error("验证码错误");
        }
        R r = userService.login(LoginPhoneDto);
        //登录成功将id存到session里面
        User data = (User) r.getData();
        //System.out.println("sesson--------------"+data.getId());
        session.setAttribute("userId", data.getId());
        return R.success("登陆成功");
    }
/**
 * 退出登录
 */
    @PostMapping("/loginout")
    public R loginout (){
        //退出登录清空session中的id
        HttpSession session = request.getSession();
        session.removeAttribute("userId");
        return R.success("推出成功");
    }


}
