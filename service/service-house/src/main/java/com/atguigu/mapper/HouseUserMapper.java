package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-15  15:49
 */
public interface HouseUserMapper extends BaseMapper<HouseUser> {
    /**
     * 根据房源id查询房东列表
     * @param id
     * @return
     */
    List<HouseUser> findHouseUserListByHouseId(Long id);
}
