package com.atguigu.base;

import com.github.pagehelper.PageInfo;

import java.util.Map;

/**
 * 包名:com.atguigu.base
 *
 * @author Leevi
 * 日期2022-06-13  09:23
 */
public interface BaseService<T> {
    /**
     * 新增
     * @param t
     */
    void insert(T t);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    T getById(Long id);

    /**
     * 修改
     * @param t
     */
    void update(T t);

    /**
     * 根据id删除
     * @param id
     */
    void delete(Long id);

    /**
     * 分页查询信息
     * @param filters
     * @return
     */
    PageInfo<T> findPage(Map<String, Object> filters);
}
