package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseUser;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-15  15:48
 */
public interface HouseUserService extends BaseService<HouseUser> {
    /**
     * 根据房源id查询房东列表
     * @param id
     * @return
     */
    List<HouseUser> findHouseUserListByHouseId(Long id);
}
