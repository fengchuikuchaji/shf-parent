package com.atguigu.config;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Admin;
import com.atguigu.service.AdminService;
import com.atguigu.service.PermissionService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.config
 * @author Leevi
 * 日期2022-06-22  10:06
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Reference
    private AdminService adminService;
    @Reference
    private PermissionService permissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //我们在这个方法中校验账号和密码
        //username表示用户在登录页面输入的用户名,那么我们就需要去校验username是否正确
        Admin admin = adminService.getByUsername(username);
        if (admin == null) {
            //根据用户名无法查询到用户，也就说明用户名错误
            throw new UsernameNotFoundException("用户名错误");
        }
        //说明用户名正确,接下来校验密码
        //admin中的密码是数据库中的密码，我们只需要将这个密码交给Spring Security,由Spring Security校验
        //在授权的时候，Spring Security要求我们用字符串表示权限,一个字符串(acl_permission表中的code字段)就表示一个权限
        //1. 查询出当前用户的所有code
        //1.1 判断当前用户是否是超级管理员,如果是超级管理员则查询出所有权限code
        List<String> permissionCodeList = null;
        if (admin.getId() == 1) {
            permissionCodeList = permissionService.findAllCodePermission();
        }else {
            //1.2 如果不是超级管理员才根据用户id查询权限code
            permissionCodeList = permissionService.findCodePermissionListByAdminId(admin.getId());
        }
        //Spring Security真正需要的权限集合List<GrantedAuthority>,所以我们要将permissionCodeList中的每一个字符串code映射成GrantedAuthority

        //使用原始for循环
        /*List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        for (String codePermission : permissionCodeList) {
            if (!StringUtils.isEmpty(codePermission)) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(codePermission));
            }
        }*/

        //使用stream
        List<SimpleGrantedAuthority> grantedAuthorityList = permissionCodeList.stream()
                .filter(codePermission -> !StringUtils.isEmpty(codePermission))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        //查询出当前用户的所有权限，交给Spring Security进行授权
        return new User(username,admin.getPassword(),grantedAuthorityList);
    }
}
