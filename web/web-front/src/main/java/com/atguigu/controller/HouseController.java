package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.*;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.atguigu.result.Result;
import com.atguigu.service.*;
import com.github.pagehelper.PageInfo;
import com.sun.corba.se.pept.broker.Broker;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 包名:com.atguigu.controller
 * @author Leevi
 * 日期2022-06-18  10:10
 * 实体Bean有多种类型:
 * 1. POJO: Plain Old Java Object，简单的Java对象，它是用在与数据库交互(与表对应)
 * 2. VO: Value Object，值对象，它是用在与前端交互(与页面对应)，VO中的数据是用来显示到页面上的
 * 3. DTO: Data Transfer Object，数据传输对象，它的使用场景一般是在两个不同的系统之间进行数据传输(远程调用的时候传输数据)
 * 4. BO: Business Object，业务对象，它是用在与业务逻辑交互(与业务逻辑对应)，它通常用来封装前端传递过来的数据
 *
 */
@RestController
@RequestMapping("/house")
public class HouseController {
    @Reference
    private HouseService houseService;
    @Reference
    private CommunityService communityService;
    @Reference
    private HouseBrokerService houseBrokerService;
    @Reference
    private HouseImageService houseImageService;
    @Reference
    private HouseUserService houseUserService;
    @Reference
    private UserFollowService userFollowService;
    @PostMapping("/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               @RequestBody HouseQueryBo houseQueryBo) {
        //1. 调用业务层的方法搜索房源的分页数据
        PageInfo<HouseVo> pageInfo = houseService.findListPage(pageNum,pageSize,houseQueryBo);
        //2. 返回结果
        return Result.ok(pageInfo);
    }

    @GetMapping("/info/{id}")
    public Result info(@PathVariable("id") Long id, HttpSession session){
        Map<String,Object> responseMap = new HashMap<>();
        //1. 查询当前房源信息
        House house = houseService.getById(id);
        responseMap.put("house",house);
        //2. 查询当前房源所属的小区信息:要查询小区信息，必须得知道小区id
        Community community = communityService.getById(house.getCommunityId());
        responseMap.put("community",community);
        //3. 查询当前房源的经纪人列表
        List<Broker> houseBrokerList = houseBrokerService.findHouseBrokerListByHouseId(id);
        responseMap.put("houseBrokerList",houseBrokerList);
        //4. 查询当前房源的房源图片列表
        List<HouseImage> houseImage1List = houseImageService.findHouseImageList(id, 1);
        responseMap.put("houseImage1List",houseImage1List);
        //5. 查询当前房源的房东列表
        List<HouseUser> houseUserList = houseUserService.findHouseUserListByHouseId(id);
        responseMap.put("houseUserList",houseUserList);

        //6. 查询当前登录的用户是否已关注这个房源
        //6.1 从session中获取当前登录的用户
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        //6.2 判断当前是否已登录
        if (userInfo == null) {
            //当前未登录,那么就一定是未关注
            responseMap.put("isFollow",false);
        }else {
            //已登录，则判断当前用户是否已关注这个房源:就是根据当前用户的id和当前房源的id到user_follow表中查询
            responseMap.put("isFollow",userFollowService.isFollow(userInfo.getId(),id));
        }
        //7. 返回结果
        return Result.ok(responseMap);
    }
}
