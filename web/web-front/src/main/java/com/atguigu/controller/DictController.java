package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-18  09:31
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;

    @RequestMapping("/findDictListByParentDictCode/{dictCode}")
    public Result findDictListByParentDictCode(@PathVariable("dictCode") String dictCode){
        //1. 调用业务层的方法，根据父节点的dictCode查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentDictCode(dictCode);
        return Result.ok(dictList);
    }

    @GetMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId")Long parentId){
        //1. 调用业务层的方法，根据父节点的id查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
