package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Role;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-11  10:31
 */
public interface RoleMapper extends BaseMapper<Role> {
    /**
     * 查询全部角色
     * @return
     */
    List<Role> findAll();
}
