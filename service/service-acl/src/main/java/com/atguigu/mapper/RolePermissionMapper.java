package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.RolePermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-21  09:19
 */
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    /**
     * 根据角色id查询该角色已分配的权限id列表
     * @param roleId
     * @return
     */
    List<Long> findPermissionIdListByRoleId(Long roleId);

    /**
     * 删除当前角色分配的所有权限
     * @param roleId
     */
    void deleteByRoleId(Long roleId);

    /**
     * 批量插入角色权限关系
     * @param roleId
     * @param permissionIds
     */
    void insertBatch(@Param("roleId") Long roleId,@Param("permissionIds") List<Long> permissionIds);

    /**
     * 批量删除角色分配的权限
     * @param roleId
     * @param permissionIds
     */
    void removeRolePermission(@Param("roleId") Long roleId,@Param("permissionIds") List<Long> permissionIds);

    /**
     * 根据roleId和permissionId查询角色分配的权限
     * @param roleId
     * @param permissionId
     * @return
     */
    RolePermission findByRoleIdAndPermissionId(@Param("roleId") Long roleId,@Param("permissionId") Long permissionId);
}
