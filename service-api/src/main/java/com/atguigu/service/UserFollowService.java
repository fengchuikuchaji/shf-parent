package com.atguigu.service;

import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.PageInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-20  11:10
 */
public interface UserFollowService {
    /**
     * 判断用户是否已关注房源
     * @param userId
     * @param houseId
     * @return
     */
    Boolean isFollow(Long userId,Long houseId);

    /**
     * 添加关注
     * @param userId
     * @param houseId
     */
    void addFollow(Long userId, Long houseId);

    /**
     * 查询用户关注的房源列表的分页数据
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<UserFollowVo> findListPage(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 取消关注
     * @param id
     */
    void delete(Long id);
}
