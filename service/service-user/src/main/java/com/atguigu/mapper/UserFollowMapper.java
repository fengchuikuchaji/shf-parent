package com.atguigu.mapper;

import com.atguigu.entity.UserFollow;
import com.atguigu.entity.vo.UserFollowVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-20  11:11
 */
public interface UserFollowMapper {
    /**
     * 根据用户id和房源id查询关注记录
     * @param userId
     * @param houseId
     * @return
     */
    UserFollow findByUserIdAndHouseId(@Param("userId") Long userId,@Param("houseId") Long houseId);

    /**
     * 新增关注记录
     * @param userFollow
     */
    void insert(UserFollow userFollow);

    /**
     * 更新关注记录
     * @param userFollow
     */
    void update(UserFollow userFollow);

    /**
     * 查询用户关注的房源列表
     * @param userId
     * @return
     */
    Page<UserFollowVo> findListPage(Long userId);

    /**
     * 根据id删除关注记录
     * @param id
     */
    void delete(Long id);
}
