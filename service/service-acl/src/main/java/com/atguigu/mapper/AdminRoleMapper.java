package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.AdminRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-20  15:51
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {
    /**
     * 根据用户id查询该用户已分配的角色id列表
     * @param adminId
     * @return
     */
    List<Long> findRoleIdListByAdminId(Long adminId);

    /**
     * 根据adminId删除已分配的所有角色
     * @param adminId
     */
    void deleteByAdminId(Long adminId);

    /**
     * 批量添加用户分配的角色
     * @param adminId
     * @param roleIds
     */
    void insertBatch(@Param("adminId") Long adminId,@Param("roleIds") List<Long> roleIds);

    /**
     * 根据adminId和roleIds删除用户已分配的角色
     * @param adminId
     * @param removeRoleIds
     */
    void deleteByAdminIdAndRoleIds(@Param("adminId") Long adminId,@Param("roleIds") List<Long> removeRoleIds);

    /**
     * 查询用户是否分配过某个角色
     * @param adminId
     * @param roleId
     * @return
     */
    AdminRole findByAdminIdAndRoleId(@Param("adminId") Long adminId,@Param("roleId") Long roleId);
}
