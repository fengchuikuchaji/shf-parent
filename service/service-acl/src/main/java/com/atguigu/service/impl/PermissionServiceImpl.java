package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.base.BaseMapper;
import com.atguigu.base.BaseServiceImpl;
import com.atguigu.entity.Permission;
import com.atguigu.helper.PermissionHelper;
import com.atguigu.mapper.PermissionMapper;
import com.atguigu.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-06-21  11:11
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl extends BaseServiceImpl<Permission> implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public BaseMapper<Permission> getEntityMapper() {
        return permissionMapper;
    }

    @Override
    public List<Permission> findMenuPermissionByAdminId(Long adminId) {
        //1.判断当前用户是否是超级管理员,并且查询用户的权限列表
        List<Permission> permissionList = null;
        if (adminId == 1L) {
            //1.1 如果是超级管理员，直接查询所有的权限
            permissionList = permissionMapper.findAll();
        }else {
            //1.2 如果不是超级管理员，查询根据用户的id，查询出用户的权限
            permissionList = permissionMapper.findPermissionListByAdminId(adminId);
        }
        //2. 将权限列表构建父子关系
        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<Permission> findAllMenu() {
        //1. 查询所有菜单
        List<Permission> permissionList = permissionMapper.findAll();
        return PermissionHelper.build(permissionList);
    }

    @Override
    public List<String> findCodePermissionListByAdminId(Long adminId) {

        return permissionMapper.findCodePermissionListByAdminId(adminId);
    }

    @Override
    public List<String> findAllCodePermission() {

        return permissionMapper.findAllCodePermission();
    }

    @Override
    public void delete(Long id) {
        //判断当前菜单是否有子菜单,如果有子菜单，则不能删除:以当前菜单id作为parentId查询子菜单，如果能查询到，说明有子菜单，不能删除
        Integer count = permissionMapper.findCountByParentId(id);
        if (count > 0) {
            //说明有子菜单，不能删除
            throw new RuntimeException("当前菜单有子菜单，不能删除");
        }
        //没有子菜单，则可以删除
        super.delete(id);
    }
}
