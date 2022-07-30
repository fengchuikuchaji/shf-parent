package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.Community;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-15  09:34
 */
public interface CommunityService extends BaseService<Community> {
    List<Community> findAll();
}
