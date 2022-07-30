package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.UserInfo;
import com.atguigu.mapper.UserInfoMapper;
import com.atguigu.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-06-20  10:07
 */
@Service(interfaceClass = UserInfoService.class)
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public UserInfo getByPhone(String phone) {
        return userInfoMapper.getByPhone(phone);
    }

    @Override
    public void insert(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }
}
