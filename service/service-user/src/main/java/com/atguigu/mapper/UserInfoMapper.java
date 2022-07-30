package com.atguigu.mapper;

import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-20  10:08
 */
public interface UserInfoMapper {
    /**
     * 根据手机号查询用户信息
     * @param phone
     * @return
     */
    UserInfo getByPhone(String phone);

    /**
     * 保存用户信息
     * @param userInfo
     */
    void insert(UserInfo userInfo);
}
