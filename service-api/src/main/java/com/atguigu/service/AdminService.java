package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Admin;
import com.atguigu.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-13  10:28
 */
public interface AdminService extends BaseService<Admin> {
    /**
     * 查询所有的管理员
     * @return
     */
    List<Admin> findAll();

    /**
     * 查询用户已分配和未分配的角色列表
     * @param id
     * @return
     */
    Map<String, List<Role>> findRolesByAdminId(Long id);

    /**
     * 给用户分配角色
     * @param adminId
     * @param roleIds
     */
    void saveAssignRole(Long adminId, List<Long> roleIds);

    /**
     * 根据用户名查找用户
     * @param username
     * @return
     */
    Admin getByUsername(String username);
}
