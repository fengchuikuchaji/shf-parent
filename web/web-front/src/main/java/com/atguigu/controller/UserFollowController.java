package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.UserInfo;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.result.Result;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 包名:com.atguigu.controller
 *
 * @author Leevi
 * 日期2022-06-20  11:46
 */
@RestController
@RequestMapping("/userFollow")
public class UserFollowController {
    @Reference
    private UserFollowService userFollowService;

    @RequestMapping("/auth/follow/{houseId}")
    public Result addFollow(@PathVariable("houseId") Long houseId, HttpSession session){
        //1. 获取session中的用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        //2. 调用业务层的方法保存关注信息
        userFollowService.addFollow(userInfo.getId(),houseId);
        return Result.ok();
    }

    @GetMapping("/auth/list/{pageNum}/{pageSize}")
    public Result findListPage(@PathVariable("pageNum") Integer pageNum,
                               @PathVariable("pageSize") Integer pageSize,
                               HttpSession session){
        //1. 获取session中的用户信息
        UserInfo userInfo = (UserInfo) session.getAttribute("USER");
        //2. 调用业务层的方法查询当前用户的关注列表的分页数据
        PageInfo<UserFollowVo> pageInfo = userFollowService.findListPage(userInfo.getId(),pageNum,pageSize);
        //3. 返回结果
        return Result.ok(pageInfo);
    }

    @GetMapping("/auth/cancelFollow/{id}")
    public Result cancelFollow(@PathVariable("id") Long id){
        //取消关注其实就是根据id删除关注数据
        userFollowService.delete(id);

        return Result.ok();
    }
}
