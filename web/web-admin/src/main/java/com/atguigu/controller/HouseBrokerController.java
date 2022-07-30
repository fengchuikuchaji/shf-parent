package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.base.BaseController;
import com.atguigu.entity.Admin;
import com.atguigu.entity.HouseBroker;
import com.atguigu.service.AdminService;
import com.atguigu.service.HouseBrokerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-17  09:06
 */
@Controller
@RequestMapping("/houseBroker")
public class HouseBrokerController extends BaseController {
    private static final String PAGE_CREATE = "houseBroker/create";
    private static final String PAGE_EDIT = "houseBroker/edit";
    private static final String DETAIL_ACTION = "redirect:/house/";

    @Reference
    private AdminService adminService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @GetMapping("/create")
    public String create(HouseBroker houseBroker, Model model){
        //此时houseId在houseBroker中
        //我们将houseBroker放入到model中
        model.addAttribute("houseBroker",houseBroker);
        //查询所有的管理员,并且存储到model中
        saveAllAdminToModel(model);
        return PAGE_CREATE;
    }

    @PostMapping("/save")
    public String save(HouseBroker houseBroker,Model model){
        //houseBroker中封装了俩数据:houseId和brokerId
        //查询当前管理员是否是当前房源的经纪人:根据houseId和brokerId到hse_house_broker表中查询
        HouseBroker dbHouseBroker = houseBrokerService.getByHouseIdAndBrokerId(houseBroker.getHouseId(),houseBroker.getBrokerId());
        if(dbHouseBroker != null) {
            //说明当前管理员已经是当前房源的经纪人，不能重复添加
            throw new RuntimeException("当前管理员已经是当前房源的经纪人,不能重复添加");
        }
        //如果当前管理员不是当前房源的经纪人，则可以添加
        //1. 根据brokerId作为admin的id查询admin
        Admin admin = adminService.getById(houseBroker.getBrokerId());
        //2. 设置broker的name和headUrl
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        //3. 新增
        houseBrokerService.insert(houseBroker);
        return successPage(model,"新增房源经纪人成功");
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //id就是房源经纪人的id
        //调用业务层的方法根据id查询房源经纪人
        HouseBroker houseBroker = houseBrokerService.getById(id);
        model.addAttribute("houseBroker",houseBroker);
        //查询出所有的管理员，并且存储到model中
        saveAllAdminToModel(model);

        return PAGE_EDIT;
    }

    /**
     * 查询所有管理员，并且存储到model中
     * @param model
     */
    private void saveAllAdminToModel(Model model) {
        List<Admin> adminList = adminService.findAll();
        model.addAttribute("adminList", adminList);
    }

    @PostMapping("/update")
    public String update(HouseBroker houseBroker,Model model){
        //houseBroker中有俩数据:id(当前这条经纪人数据的id)和brokerId(要修改成为经纪人的那个管理员的id)

        //一、判断当前是否可以修改:也就是判断当前管理员是否已经是当前房源的经纪人
        //1. 调用业务层的方法根据id，查询房源经纪人信息,查询到这条数据的目的是为了获取houseId
        HouseBroker dbHouseBroker = houseBrokerService.getById(houseBroker.getId());

        //2. 根据houseId和brokerId到hse_house_broker表中查询是否有数据匹配
        if (houseBrokerService.getByHouseIdAndBrokerId(dbHouseBroker.getHouseId(), houseBroker.getBrokerId()) != null) {
            throw new RuntimeException("当前管理员已经是当前房源的经纪人,修改失败");
        }

        //二、当前管理员不是当前房源的经纪人，则可以修改
        //1. 查询出我要修改成为经纪人的那个管理员的信息
        Admin admin = adminService.getById(houseBroker.getBrokerId());

        //2. 设置houseBroker的brokerName和brokerHeadUrl
        houseBroker.setBrokerName(admin.getName());
        houseBroker.setBrokerHeadUrl(admin.getHeadUrl());
        //3. 修改经纪人
        houseBrokerService.update(houseBroker);
        return successPage(model,"修改房源经纪人成功");
    }

    @GetMapping("/delete/{houseId}/{id}")
    public String delete(@PathVariable("houseId") Long houseId,
                         @PathVariable("id") Long id){
        //1.根据id删除房源经纪人
        houseBrokerService.delete(id);
        //2. 重定向访问当前房源的详情页:需要当前房源的id
        return DETAIL_ACTION + houseId;
    }
}
