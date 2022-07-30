package com.atguigu.service;

import com.atguigu.entity.Dict;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-14  14:55
 */
public interface DictService {
    /**
     * 查询所有的字典数据
     * @param id
     * @return
     */
    List<Map<String, Object>> findZnodes(Long id);

    /**
     * 根据父节点的dictCode查询子节点列表
     * @param dictCode
     * @return
     */
    List<Dict> findDictListByParentDictCode(String dictCode);

    /**
     * 根据父节点的id查询子节点列表
     * @param parentId
     * @return
     */
    List<Dict> findDictListByParentId(Long parentId);
}
