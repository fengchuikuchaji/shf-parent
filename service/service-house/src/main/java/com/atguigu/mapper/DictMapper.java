package com.atguigu.mapper;

import com.atguigu.entity.Dict;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-14  15:24
 */
public interface DictMapper {
    /**
     * 根据父节点的id查询子节点列表
     * @param parentId
     * @return
     */
    List<Dict> findListByParentId(Long parentId);

    /**
     * 根据父节点id查询子节点的数量
     * @param parentId
     * @return
     */
    Integer countIsParent(Long parentId);

    /**
     * 根据父节点的dictCode查询子节点列表
     * @param dictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String dictCode);
}
