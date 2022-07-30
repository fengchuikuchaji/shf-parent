package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.en.HouseStatus;
import com.atguigu.entity.Community;
import com.atguigu.entity.House;
import com.atguigu.entity.HouseImage;
import com.atguigu.entity.HouseUser;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import com.sun.corba.se.pept.broker.Broker;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-15  11:33
 */
@Controller
@RequestMapping("/house")
public class HouseController extends BaseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private DictService dictService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseUserService houseUserService;
    private static final String PAGE_INDEX = "house/index";
    private static final String PAGE_CREATE = "house/create";
    private static final String PAGE_EDIT = "house/edit";
    private static final String LIST_ACTION = "redirect:/house";
    private static final String PAGE_SHOW = "house/show";
    @RequestMapping
    public String index(@RequestParam Map<String,Object> filters, Model model){
        //判断是否传入了pageNum和pageSize
        if (filters.get("pageNum") == null || "".equals(filters.get("pageNum") == null)) {
            filters.put("pageNum", 1);
        }
        if (filters.get("pageSize") == null || "".equals(filters.get("pageSize") == null)) {
            filters.put("pageSize", 10);
        }

        //调用业务层的方法，查询分页数据
        PageInfo<House> page = houseService.findPage(filters);
        model.addAttribute("page",page);
        //将搜索条件存储到请求域
        model.addAttribute("filters",filters);
        saveAllDictToModel(model);

        return PAGE_INDEX;
    }

    /**
     * 保存所有的字典数据到请求域
     * @param model
     */
    private void saveAllDictToModel(Model model) {
        //1. 查询所有小区列表，并且存储到请求域
        model.addAttribute("communityList", communityService.findAll());
        //2. 查询所有户型，并且存储到请求域
        model.addAttribute("houseTypeList", dictService.findDictListByParentDictCode("houseType"));
        //3. 查询所有楼层，并且存储到请求域
        model.addAttribute("floorList", dictService.findDictListByParentDictCode("floor"));
        //4. 查询所有的建筑结构，并且存储到请求域
        model.addAttribute("buildStructureList", dictService.findDictListByParentDictCode("buildStructure"));
        //5. 查询所有的朝向,并且存储到请求域
        model.addAttribute("directionList", dictService.findDictListByParentDictCode("direction"));
        //6. 查询所有的装修情况，并且存储到请求域
        model.addAttribute("decorationList", dictService.findDictListByParentDictCode("decoration"));
        //7. 查询所有的房屋用途，并且存储到请求域
        model.addAttribute("houseUseList", dictService.findDictListByParentDictCode("houseUse"));
    }

    @RequestMapping("/create")
    public String create(Model model){
        //1. 查询所有小区列表，并且存储到请求域
        saveAllDictToModel(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(House house,Model model){
        //设置房源的状态:0表示未发布，1表示已发布
        house.setStatus(HouseStatus.UNPUBLISHED.code);

        //1. 调用业务层的方法，保存房源信息
        houseService.insert(house);
        return successPage(model,"新增房源信息成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //1. 调用业务层的方法，根据id查询房源信息
        House house = houseService.getById(id);
        //2. 将查询到的房源信息存储到请求域
        model.addAttribute("house",house);
        //3. 查询小区列表、户型、楼层、建筑结构、朝向、装修情况、房屋用途列表，并且存储到请求域
        saveAllDictToModel(model);
        return PAGE_EDIT;
    }

    @PostMapping("/update")
    public String update(House house,Model model){
        //1. 调用业务层的方法，更新房源信息
        houseService.update(house);
        return successPage(model,"更新房源信息成功");
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        //调用业务层的方法，根据id删除房源信息
        houseService.delete(id);
        return LIST_ACTION;
    }

    @GetMapping("/publish/{id}/{status}")
    public String publish(@PathVariable("id") Long id,@PathVariable("status") int status){
        //发布房源或者取消发布房源:其实就是修改房源信息的状态(status)
        House house = new House();
        house.setId(id);
        house.setStatus(status);
        houseService.update(house);
        //重定向访问首页
        return LIST_ACTION;
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id,Model model){
        //id是要查询详情的那个房源的id
        //1. 查询当前房源自己的信息，存储到请求域
        House house = houseService.getById(id);
        model.addAttribute("house",house);
        //2. 查询当前房源所属的小区信息，存储到请求域
        Community community = communityService.getById(house.getCommunityId());
        model.addAttribute("community",community);
        //3. 查询当前房源的房源图片列表(type=1)，存储到请求域
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id,1);
        model.addAttribute("houseImage1List",houseImage1List);
        //4. 查询当前房源的房产图片列表(type=2)，存储到请求域
        List<HouseImage> houseImage2List = houseImageService.findHouseImageList(id,2);
        model.addAttribute("houseImage2List",houseImage2List);
        //5. 查询当前房源的经纪人列表，存储到请求域
        List<Broker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        model.addAttribute("houseBrokerList",houseBrokerList);
        //6. 查询当前房源的房东列表，存储到请求域
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(id);
        model.addAttribute("houseUserList",houseUserList);

        return PAGE_SHOW;
    }
}
