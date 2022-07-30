package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-15  15:35
 */
public interface HouseImageMapper extends BaseMapper<HouseImage> {
    List<HouseImage> findHouseImageList(@Param("houseId") Long houseId,@Param("type") int type);
}
