package com.atguigu.interceptor;

import com.alibaba.fastjson.JSON;
import com.atguigu.result.Result;
import com.atguigu.result.ResultCodeEnum;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包名:com.atguigu.interceptor
 *
 * @author Leevi
 * 日期2022-06-20  11:49
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前是否已登录，如果已登录则放行
        if (request.getSession().getAttribute("USER") == null) {
            //说明当前未登录
            //让其跳转到登录页面:响应208状态
            response.getWriter().write(JSON.toJSONString(Result.build(null, ResultCodeEnum.LOGIN_AUTH)));
            return false;
        }
        return true;
    }
}
