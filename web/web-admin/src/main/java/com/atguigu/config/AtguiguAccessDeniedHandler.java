package com.atguigu.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包名:com.atguigu.config
 *
 * @author Leevi
 * 日期2022-06-22  11:35
 */
public class AtguiguAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //handle方法是:当访问被拒绝时调用该方法处理
        httpServletResponse.sendRedirect("/auth");
    }
}
