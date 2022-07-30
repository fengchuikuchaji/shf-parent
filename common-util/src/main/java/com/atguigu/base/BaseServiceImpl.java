package com.atguigu.base;

import com.atguigu.util.CastUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-06-13  09:24
 */
public abstract class BaseServiceImpl<T> {
    /**
     * 获取mapper对象
     * @return
     */
    public abstract BaseMapper<T> getEntityMapper();

    public void insert(T t) {
        getEntityMapper().insert(t);
    }

    public T getById(Long id) {
        return getEntityMapper().getById(id);
    }

    public void update(T t) {
        getEntityMapper().update(t);
    }

    public void delete(Long id) {
        getEntityMapper().delete(id);
    }

    public PageInfo<T> findPage(Map<String, Object> filters) {
        //获取pageNum和pageSize强转成int类型:要考虑强转失败的情况
        int pageNum = CastUtil.castInt(filters.get("pageNum"), 1);
        int pageSize = CastUtil.castInt(filters.get("pageSize"), 10);
        //1. 使用分页插件开启分页
        PageHelper.startPage(pageNum,pageSize);
        //2. 调用持久层的方法根据条件搜索角色列表
        return new PageInfo<>(getEntityMapper().findPage(filters),10);
    }
}
