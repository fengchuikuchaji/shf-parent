package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;
import com.atguigu.service.AdminService;
import com.atguigu.service.RoleService;
import com.atguigu.util.FileUtil;
import com.atguigu.util.QiniuUtils;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-13  10:22
 */
@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {
    @Reference
    private AdminService adminService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Reference
    private RoleService roleService;
    private static final String PAGE_ASSIGNSHOW = "admin/assignShow";
    private static final String PAGE_UPLOAD = "admin/upload";
    private static final String PAGE_INDEX = "admin/index";
    private static final String PAGE_EDIT = "admin/edit";
    private static final String LIST_ACTION = "redirect:/admin";

    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 判断是否传入了pageNum和pageSize，如果没有，则赋默认值
        if (filters.get("pageNum") == null || "".equals(filters.get("pageNum"))) {
            filters.put("pageNum",1);
        }
        if (filters.get("pageSize") == null || "".equals(filters.get("pageSize"))) {
            filters.put("pageSize",10);
        }
        //2. 调用业务层的方法查询分页数据
        PageInfo<Admin> pageInfo = adminService.findPage(filters);
        //3. 将查询到的分页数据存入请求域
        model.addAttribute("page",pageInfo);
        //4. 将搜索条件存入请求域
        model.addAttribute("filters",filters);

        System.out.println("hello hot-fix");
        System.out.println("现在处于热修复分支，进行热修复...");
        //5. 显示首页
        return PAGE_INDEX;
    }

    @PostMapping("/save")
    public String save(Admin admin,Model model){
        //0. 用加密器给要添加的用户的密码加密
        admin.setPassword(bCryptPasswordEncoder.encode(admin.getPassword()));
        //1. 调用业务层的方法保存数据
        adminService.insert(admin);
        //2. 显示成功页面
        return successPage(model,"新增用户信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法根据id查询用户信息
        Admin admin = adminService.getById(id);
        //2. 将查询到的用户信息存储到请求
        model.addAttribute("admin",admin);
        //3. 显示修改页面
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Admin admin,Model model){
        //1. 调用业务层的方法更新用户信息
        adminService.update(admin);

        //2. 显示成功页面
        return successPage(model,"修改用户信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //1. 调用业务层的方法根据id删除用户信息
        adminService.delete(id);
        //2. 重定向访问首页
        return LIST_ACTION;
    }

    @GetMapping("/uploadShow/{id}")
    public String uploadShow(@PathVariable("id") Long id,Model model){
        //将id存储到请求域
        model.addAttribute("id",id);
        return PAGE_UPLOAD;
    }

    @PostMapping("/upload/{id}")
    public String upload(@PathVariable("id") Long id,@RequestParam("file") MultipartFile file,Model model) throws IOException {
        //1. 将客户端上传的文件保存到七牛云服务器(保存到七牛云的时候，必须给它指定一个唯一的文件名)
        //1.1 获取客户端上传文件的文件名
        String originalFilename = file.getOriginalFilename();
        //1.2 将这个名字转成一个唯一的文件名
        String uuidName = FileUtil.getUUIDName(originalFilename);
        //1.3 文件上传
        QiniuUtils.upload2Qiniu(file.getBytes(),uuidName);
        //1.4 获取该图片在七牛云上的url
        String headUrl = QiniuUtils.getUrl(uuidName);

        //2. 将改文件的七牛云的url存储到数据库中
        Admin admin = new Admin();
        admin.setId(id);
        admin.setHeadUrl(headUrl);
        adminService.update(admin);

        return successPage(model,"上传头像成功");
    }

    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //id是要分配角色的用户的id
        Map<String, List<Role>> roles = adminService.findRolesByAdminId(id);
        //将查询到的已分配的角色列表和未分配的角色列表存入请求域
        model.addAllAttributes(roles);
        //将用户id存入请求域
        model.addAttribute("adminId",id);
        return PAGE_ASSIGNSHOW;
    }

    @PostMapping("/assignRole")
    public String assignRole(@RequestParam("adminId") Long adminId,
                             @RequestParam("roleIds") List<Long> roleIds,
                             Model model){
        //adminId表示要分配角色的用户的id
        //roleIds表示给用户分配的那些角色的id
        adminService.saveAssignRole(adminId,roleIds);

        return successPage(model,"分配角色成功");
    }
}
