package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Community;
import com.atguigu.entity.Dict;
import com.atguigu.service.CommunityService;
import com.atguigu.service.DictService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-15  09:31
 */
@Controller
@RequestMapping("/community")
public class CommunityController extends BaseController {
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    private static final String PAGE_INDEX = "community/index";
    private static final String PAGE_CREATE = "community/create";
    private static final String PAGE_EDIT = "community/edit";
    private static final String LIST_ACTION = "redirect:/community";
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //1. 判断是否传入了pageNum和pageSize
        if (filters.get("pageNum") == null || "".equals(filters.get("pageNum"))) {
            filters.put("pageNum", 1);
        }
        if (filters.get("pageSize") == null || "".equals(filters.get("pageSize"))) {
            filters.put("pageSize", 10);
        }
        //2. 调用业务层的方法查询分页信息
        PageInfo<Community> page = communityService.findPage(filters);
        //3. 将查询到的分页信息保存到request域中
        model.addAttribute("page", page);

        //查询beijing的所有区域列表，保存到请求域中
        saveAreaListToModel(model);

        //搜索条件中可能不包含areaId和plateId，但是页面回显的时候需要，所以我们判断filters中是否包含了areaId和plateId
        //如果不包含，那么就设置为0
        if (filters.get("areaId") == null || "".equals(filters.get("areaId"))) {
            filters.put("areaId", 0);
        }
        if (filters.get("plateId") == null || "".equals(filters.get("plateId"))) {
            filters.put("plateId", 0);
        }
        //5. 搜索回显到页面，将filters搜索条件存储到请求域
        model.addAttribute("filters", filters);
        return PAGE_INDEX;
    }

    /**
     * 查询北京的所有区域列表，保存到请求域中
     * @param model
     */
    private void saveAreaListToModel(Model model) {
        //4. 查询"beijing"的所有的区域列表
        List<Dict> areaList = dictService.findDictListByParentDictCode("beijing");
        //将查询到的区域列表保存到request域中
        model.addAttribute("areaList", areaList);
    }

    @RequestMapping("/create")
    public String create(Model model){
        //调用业务层的方法查询beijing的所有的区域列表
        saveAreaListToModel(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(Community community,Model model){
        //调用业务层的方法保存小区信息
        communityService.insert(community);
        return successPage(model,"保存小区信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法查询小区信息
        Community community = communityService.getById(id);
        //2. 将查询到的小区信息保存到request域中
        model.addAttribute("community", community);
        //3. 调用业务层的方法查询beijing的所有的区域列表
        saveAreaListToModel(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(Community community,Model model){
        //1. 调用业务层的方法更新小区信息
        communityService.update(community);
        return successPage(model,"更新小区信息成功");
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //调用业务层的方法根据id删除小区信息
        communityService.delete(id);
        //重定向访问首页
        return LIST_ACTION;
    }
}
