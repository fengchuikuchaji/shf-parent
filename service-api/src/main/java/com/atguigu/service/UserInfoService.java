package com.atguigu.service;

import com.atguigu.entity.UserInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-20  10:05
 */
public interface UserInfoService {
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
