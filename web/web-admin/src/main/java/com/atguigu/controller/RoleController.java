package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Role;
import com.atguigu.service.RoleService;
import com.github.pagehelper.PageInfo;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-11  10:56
 * 因为spring底层的动态代理技术:
 * 1. 如果有接口，就使用jdk的动态代理技术:创建接口的实现类对象
 * 2. 如果没有接口，就使用cglib的动态代理技术:创建类的子类对象
 */
@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
    @Reference
    private RoleService roleService;
    private static final String PAGE_INDEX = "role/index";
    private static final String PAGE_CREATE = "role/create";
    private static final String PAGE_EDIT = "role/edit";
    private static final String LIST_ACTION = "redirect:/role";
    private static final String PAGE_ASSIGNSHOW = "role/assignShow";


    @PreAuthorize("hasAuthority('role.show')")
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //filters就是获取到的客户端的请求参数，里边可能包含:pageNum、pageSize、roleName
        //1. 判断请求参数中是否传入了pageNum和pageSize，如果没有则给其赋默认值
        if (filters.get("pageNum") == null || "".equals(filters.get("pageNum"))) {
            filters.put("pageNum",1);
        }
        if (filters.get("pageSize") == null || "".equals(filters.get("pageSize"))) {
            filters.put("pageSize",10);
        }

        //2. 调用业务层的方法查询分页信息
        PageInfo<Role> pageInfo = roleService.findPage(filters);
        //3. 将分页数据存储到请求域
        model.addAttribute("page",pageInfo);
        //4. 将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        return PAGE_INDEX;
    }

    @PreAuthorize("hasAuthority('role.create')")
    @GetMapping("/create")
    public String create(){
        return PAGE_CREATE;
    }

    @PreAuthorize("hasAuthority('role.create')")
    @PostMapping("/save")
    public String save(Role role,Model model){
        //1. 调用业务层的方法保存角色信息
        roleService.insert(role);
        return successPage(model,"新增角色信息成功");
    }

    @PreAuthorize("hasAuthority('role.edit')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id")Long id,Model model){
        //id就代表要查询的角色的id
        //1. 调用业务层的方法根据id查询角色信息
        Role role = roleService.getById(id);
        //2. 将查询到的role存储到请求域中
        model.addAttribute("role",role);
        return PAGE_EDIT;
    }

    @PreAuthorize("hasAuthority('role.edit')")
    @PostMapping("/update")
    public String update(Role role,Model model){
        //1. 调用业务层的方法修改角色信息
        roleService.update(role);
        return successPage(model,"修改角色信息成功");
    }

    @PreAuthorize("hasAuthority('role.delete')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id")Long id){
        //1. 调用业务层的方法根据id删除角色信息
        roleService.delete(id);

        //2. 重新查询所有角色
        return LIST_ACTION;
    }

    @PreAuthorize("hasAuthority('role.assign')")
    @GetMapping("/assignShow/{id}")
    public String assignShow(@PathVariable("id") Long id,Model model){
        //id是要分配权限的那个角色的id
        //查询页面需要的数据，并且存储到请求域中
        List<Map<String, Object>> zNodes = roleService.findPermissionByRoleId(id);
        //将zNodes转成json字符串，存储到请求域
        model.addAttribute("zNodes", JSON.toJSONString(zNodes));
        //将角色id存储到请求域
        model.addAttribute("roleId",id);
        return PAGE_ASSIGNSHOW;
    }

    @PreAuthorize("hasAuthority('role.assign')")
    @PostMapping("/assignPermission")
    public String assignPermission(@RequestParam("roleId") Long roleId,
                                   @RequestParam("permissionIds") List<Long> permissionIds,
                                   Model model){
        //roleId表示要分配权限的那个角色的id
        //permissionIds表示要给该角色分配的所有权限的id
        roleService.saveRolePermission(roleId,permissionIds);
        return successPage(model,"分配权限成功");
    }
}
