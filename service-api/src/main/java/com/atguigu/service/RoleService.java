package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-11  10:37
 */
public interface RoleService extends BaseService<Role> {
    /**
     * 查询所有角色
     * @return
     */
    List<Role> findAll();

    /**
     * 根据角色的id查询权限
     * @param roleId
     * @return
     */
    List<Map<String,Object>> findPermissionByRoleId(Long roleId);

    /**
     * 给角色分配权限
     * @param roleId
     * @param permissionIds
     */
    void saveRolePermission(Long roleId, List<Long> permissionIds);
}
