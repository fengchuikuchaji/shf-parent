package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.PageInfo;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-15  11:36
 */
public interface HouseService extends BaseService<House> {
    /**
     * 前台项目搜索房源分页信息
     * @param pageNum
     * @param pageSize
     * @param houseQueryBo
     * @return
     */
    PageInfo<HouseVo> findListPage(Integer pageNum, Integer pageSize, HouseQueryBo houseQueryBo);
}
