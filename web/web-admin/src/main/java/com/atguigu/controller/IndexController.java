package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Permission;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-21  11:04
 */
@Controller
public class IndexController {
    private static final String PAGE_INDEX = "frame/index";
    @Reference
    private PermissionService permissionService;
    @Reference
    private AdminService adminService;
    @RequestMapping("/")
    public String index(Model model){
        //查询出动态菜单,并且存储到请求域
        //获取到当前登录的用户，动态菜单其实就要对应当前登录的用户的权限
        //1. Spring Security获取当前登录的用户
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        //2. 从user中获取用户名
        String username = user.getUsername();

        //3. 根据username获取admin
        Admin admin = adminService.getByUsername(username);

        //将当前用户信息存储到请求域
        model.addAttribute("admin",admin);

        //调用业务层的方法查询当前用户的动态菜单
        List<Permission> permissionList = permissionService.findMenuPermissionByAdminId(admin.getId());

        //将动态菜单存储到请求域
        model.addAttribute("permissionList",permissionList);
        return PAGE_INDEX;
    }
}
