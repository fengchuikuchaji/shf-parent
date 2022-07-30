package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.atguigu.mapper.UserFollowMapper;
import com.atguigu.service.UserFollowService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 包名:com.atguigu.service.impl
 *
 * @author Leevi
 * 日期2022-06-20  11:10
 */
@Service(interfaceClass = UserFollowService.class)
public class UserFollowServiceImpl implements UserFollowService {
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Override
    public Boolean isFollow(Long userId, Long houseId) {
        UserFollow userFollow = userFollowMapper.findByUserIdAndHouseId(userId,houseId);
        return userFollow != null && userFollow.getIsDeleted() == 0;
    }

    @Override
    public void addFollow(Long userId, Long houseId) {
        //1. 根据userId和houseId查询关注记录:此时不要考虑isDeleted
        UserFollow userFollow = userFollowMapper.findByUserIdAndHouseId(userId, houseId);
        //2. 判断userFollow是否为空
        if (userFollow == null) {
            //说明当前用户此前从未关注过该房源，那么就要新增一条数据
            userFollow = new UserFollow();
            userFollow.setUserId(userId);
            userFollow.setHouseId(houseId);
            userFollowMapper.insert(userFollow);
        }else {
            //说明当前用户之前关注过该房源, 但是已经取消关注了，那么就只要更新isDeleted为0即可
            if (userFollow.getIsDeleted() == 1) {
                userFollow.setIsDeleted(0);
                userFollowMapper.update(userFollow);
            }
        }
    }

    @Override
    public PageInfo<UserFollowVo> findListPage(Long userId, Integer pageNum, Integer pageSize) {
        //开启分页
        PageHelper.startPage(pageNum, pageSize);

        return new PageInfo<>(userFollowMapper.findListPage(userId));
    }

    @Override
    public void delete(Long id) {
        userFollowMapper.delete(id);
    }
}
