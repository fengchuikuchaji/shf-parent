package com.atguigu.helper;

import com.atguigu.entity.Permission;

import java.util.ArrayList;
import java.util.List;

/**
 * 包名:com.atguigu.helper
 *
 * @author Leevi
 * 日期2022-06-21  11:50
 */
public class PermissionHelper {
    /**
     * 根据权限列表构建父子关系
     * @param permissionList
     * @return
     */
    public static List<Permission> build(List<Permission> permissionList) {
        List<Permission> menu = new ArrayList<>();
        for (Permission permission : permissionList) {
            //判断当前菜单是否是以及菜单
            if (permission.getParentId() == 0) {
                //一级菜单
                //2.1 设置一级菜单的子菜单列表
                permission.setChildren(getChildren(permission, permissionList));
                //2.2 将一级菜单添加到菜单列表中
                menu.add(permission);
            }
        }
        return menu;
    }

    /**
     * 从原始菜单中获取某个权限的子菜单列表
     * @param permission
     * @param originPermissionList
     * @return
     */
    private static List<Permission> getChildren(Permission permission, List<Permission> originPermissionList) {
        //1. 创建一个新的集合，用来存储子菜单
        List<Permission> children = new ArrayList<>();
        //1. 遍历出原始菜单中的每一个权限
        for (Permission child : originPermissionList) {
            //1.1 如果originPermission的父id等于permission的id
            if (permission.getId().equals(child.getParentId())) {
                //originPermission是permission的子菜单，则将originPermission添加到children中
                //子菜单还有没有子菜单呢?
                child.setChildren(getChildren(child,originPermissionList));
                children.add(child);
            }
        }
        return children;
    }
}
