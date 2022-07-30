package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.Dict;
import com.atguigu.result.Result;
import com.atguigu.service.DictService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-14  14:43
 */
@RestController
@RequestMapping("/dict")
public class DictController {
    @Reference
    private DictService dictService;

    @GetMapping("/findZnodes")
    public Result findZnodes(@RequestParam(value = "id",defaultValue = "0")Long id){
        List<Map<String, Object>> znodes = dictService.findZnodes(id);
        return Result.ok(znodes);
    }

    @RequestMapping("/findDictListByParentId/{parentId}")
    public Result findDictListByParentId(@PathVariable("parentId") Long parentId){
        //调用业务层的方法根据父节点id，查询子节点列表
        List<Dict> dictList = dictService.findDictListByParentId(parentId);
        return Result.ok(dictList);
    }
}
