package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.bo.LoginBo;
import com.atguigu.entity.bo.RegisterBo;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import com.atguigu.service.UserFollowService;
import com.atguigu.service.UserInfoService;
import com.atguigu.util.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-20  09:36
 */
@RestController
@RequestMapping("/userInfo")
public class UserInfoController {
    @Reference
    private UserInfoService userInfoService;
    @Reference
    private UserFollowService userFollowService;
    @GetMapping("/sendCode/{phone}")
    public Result sendCode(@PathVariable("phone")String phone, HttpSession session){
        //模拟使用阿里云短信服务发送验证码
        String code = "1111";
        //使用session存储验证码
        session.setAttribute("CODE",code);
        return Result.ok();
    }

    @PostMapping("/register")
    public Result register(@RequestBody RegisterBo registerBo,HttpSession session){
        //1. 校验验证码是否正确
        //1.1 获取session中的验证码
        String code = (String) session.getAttribute("CODE");
        //1.2 使用session中的验证码和传递过来的验证码进行比较
        if (!code.equalsIgnoreCase(registerBo.getCode())) {
            return Result.build(null, ResultCodeEnum.CODE_ERROR);
        }

        //2. 校验手机号是否已经注册
        //2.1 调用业务层的方法根据手机号查询用户信息,如果查询到了,说明该手机号已经注册
        if (userInfoService.getByPhone(registerBo.getPhone()) != null) {
            return Result.build(null, ResultCodeEnum.PHONE_REGISTER_ERROR);
        }

        //3. 调用业务层的方法保存用户信息
        //3.1 创建UserInfo对象
        UserInfo userInfo = new UserInfo();
        //3.2 将registerBO中的数据赋值给userInfo对象
        BeanUtils.copyProperties(registerBo, userInfo);
        //3.3 设置userInfo的status为1
        userInfo.setStatus(1);
        //3.4 将userInfo中的password进行加密
        userInfo.setPassword(MD5.encrypt(userInfo.getPassword()));
        //3.5 调用业务层的方法保存数据
        userInfoService.insert(userInfo);

        return Result.ok();
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginBo loginBo,HttpSession session){
        //1. 判断用户输入的手机号是否正确
        //1.1 调用业务层的方法根据手机号查询用户信息,如果能查询到，说明你输入的手机对了
        UserInfo userInfo = userInfoService.getByPhone(loginBo.getPhone());
        if (userInfo == null) {
            //1.2 如果没有查询到，说明你输入的手机号错误
            return Result.build(null,ResultCodeEnum.ACCOUNT_ERROR);
        }

        //2. 判断用户输入的密码是否正确
        //2.1 将用户输入的密码进行加密
        if (!MD5.encrypt(loginBo.getPassword()).equals(userInfo.getPassword())) {
            //2.2 如果加密后的密码和数据库中的密码不一致，说明你输入的密码错误
            return Result.build(null,ResultCodeEnum.PASSWORD_ERROR);
        }

        //3. 判断用户的账号是否被锁定
        if (userInfo.getStatus() == 0) {
            return Result.build(null,ResultCodeEnum.ACCOUNT_LOCK_ERROR);
        }

        //4. 保存用户的登录状态:session
        session.setAttribute("USER",userInfo);

        //5. 封装响应数据:包含phone、nickname
        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("phone",userInfo.getPhone());
        responseMap.put("nickName",userInfo.getNickName());

        return Result.ok(responseMap);
    }

    @GetMapping("/logout")
    public Result logout(HttpSession session){
        //退出登录其实是清空session中的数据
        session.invalidate();

        return Result.ok();
    }
}
