package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.Community;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-15  09:35
 */
public interface CommunityMapper extends BaseMapper<Community> {
    /**
     * 查询所有小区
     * @return
     */
    List<Community> findAll();
}
