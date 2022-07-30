package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Permission;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-21  09:10
 */
public interface PermissionMapper extends BaseMapper<Permission> {
    /**
     * 查询所有权限
     * @return
     */
    List<Permission> findAll();

    /**
     * 根据用户id查询用户的权限
     * @param adminId
     * @return
     */
    List<Permission> findPermissionListByAdminId(Long adminId);

    /**
     * 根据父id查询子菜单数量
     * @param parentId
     * @return
     */
    Integer findCountByParentId(Long parentId);

    /**
     * 根据adminId查询用户的所有权限code
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
