package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-21  11:10
 */
public interface PermissionService extends BaseService<Permission> {
    /**
     * 查询用户的动态菜单
     * @param adminId
     * @return
     */
    List<Permission> findMenuPermissionByAdminId(Long adminId);

    /**
     * 查询所有的菜单(构建出父子级关系)
     * @return
     */
    List<Permission> findAllMenu();

    /**
     * 根据adminId查询用户的所有权限的code
     * @param adminId
     * @return
     */
    List<String> findCodePermissionListByAdminId(Long adminId);

    /**
     * 查询所有权限的code
     * @return
     */
    List<String> findAllCodePermission();
}
