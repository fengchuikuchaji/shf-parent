package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Permission;
import com.atguigu.service.PermissionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-21  14:14
 */
@Controller
@RequestMapping("/permission")
public class PermissionController extends BaseController {
    private static final String PAGE_INDEX = "permission/index";
    private static final String PAGE_CREATE = "permission/create";
    private static final String LIST_ACTION = "redirect:/permission";
    private static final String PAGE_EDIT = "permission/edit";
    @Reference
    private PermissionService permissionService;
    @GetMapping
    public String index(Model model){
        //查询出所有的菜单(构建出父子级关系)
        List<Permission> permissionList = permissionService.findAllMenu();
        model.addAttribute("list",permissionList);
        return PAGE_INDEX;
    }

    @GetMapping("/create")
    public String create(Permission permission,Model model){
        //permission中包含:parentId、parentName、type
        model.addAttribute("permission",permission);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Permission permission,Model model){
        permissionService.insert(permission);
        return successPage(model,"新增菜单成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //调用业务层的方法根据id删除菜单
        permissionService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //根据id查询出菜单
        Permission permission = permissionService.getById(id);
        model.addAttribute("permission",permission);

        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Permission permission,Model model){
        //调用业务层的方法修改菜单
        permissionService.update(permission);
        return successPage(model,"修改菜单成功");
    }
}
