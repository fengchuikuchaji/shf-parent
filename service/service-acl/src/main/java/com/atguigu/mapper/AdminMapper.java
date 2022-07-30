package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Admin;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-13  10:29
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 根据用户名查询用户
     * @param username
     * @return
     */
    Admin getByUsername(String username);

    /**
     * 根据手机号查询用户
     * @param phone
     * @return
     */
    Admin getByPhone(String phone);

    /**
     * 查询所有的管理员
     * @return
     */
    List<Admin> findAll();
}
