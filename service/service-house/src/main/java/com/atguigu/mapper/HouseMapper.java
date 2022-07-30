package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.House;
import com.atguigu.entity.bo.HouseQueryBo;
import com.atguigu.entity.vo.HouseVo;
import com.github.pagehelper.Page;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-15  11:19
 */
public interface HouseMapper extends BaseMapper<House> {
    /**
     * 根据小区id查询房屋数量
     * @param communityId
     * @return
     */
    Integer findCountByCommunityId(Long communityId);

    /**
     * 前台项目搜索房源分页信息
     * @param houseQueryBo
     * @return
     */
    Page<HouseVo> findListPage(HouseQueryBo houseQueryBo);
}
