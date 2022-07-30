package com.atguigu.base;

import com.github.pagehelper.Page;

import java.util.Map;

/**
 * 包名:com.atguigu.base
 * @author Leevi
 * 日期2022-06-13  09:20
 */
public interface BaseMapper<T> {
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
     * 根据条件搜索列表
     * @param filters
     * @return
     */
    Page<T> findPage(Map<String, Object> filters);
}
