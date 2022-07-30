package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.Dict;
import com.atguigu.mapper.DictMapper;
import com.atguigu.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-06-14  14:57
 */
@Service(interfaceClass = DictService.class)
public class DictServiceImpl implements DictService {
    @Autowired
    private DictMapper dictMapper;
    @Override
    public List<Map<String, Object>> findZnodes(Long id) {
        /*List<Map<String,Object>> zNodes = new ArrayList<>();
        //根据id查询对应的子分类
        List<Dict> dictList = dictMapper.findListByParentId(id);
        for (Dict dict : dictList) {
            Map<String,Object> responseMap = new HashMap<>();
            //表示当前节点是否还有子节点,以当前节点的id到hse_dict表中查询子节点的数量，如果大于0，则表示当前节点还有子节点
            Integer count = dictMapper.countIsParent(dict.getId());
            responseMap.put("isParent",count > 0);
            //表示当前节点的名称
            responseMap.put("name",dict.getName());
            responseMap.put("id",dict.getId());

            //将responseMap添加到zNodes中
            zNodes.add(responseMap);
        }*/


        //根据id查询对应的子分类
        List<Dict> dictList = dictMapper.findListByParentId(id);
        //使用Stream API将dictList转换为Map集合
        List<Map<String, Object>> zNodes = dictList.stream()
                .map(dict -> {
                    Map<String, Object> responseMap = new HashMap<>();
                    //表示当前节点是否还有子节点,以当前节点的id到hse_dict表中查询子节点的数量，如果大于0，则表示当前节点还有子节点
                    Integer count = dictMapper.countIsParent(dict.getId());
                    responseMap.put("isParent", count > 0);
                    //表示当前节点的名称
                    responseMap.put("name", dict.getName());
                    responseMap.put("id", dict.getId());
                    return responseMap;
                })
                .collect(Collectors.toList());
        return zNodes;
    }

    @Override
    public List<Dict> findDictListByParentDictCode(String dictCode) {

        return dictMapper.findDictListByParentDictCode(dictCode);
    }

    @Override
    public List<Dict> findDictListByParentId(Long parentId) {
        return dictMapper.findListByParentId(parentId);
    }
}
