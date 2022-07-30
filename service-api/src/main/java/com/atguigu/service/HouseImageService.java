package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseImage;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-15  15:34
 */
public interface HouseImageService extends BaseService<HouseImage> {
    /**
     * 根据房源id和type查询图片列表
     * @param houseId
     * @param type
     * @return
     */
    List<HouseImage> findHouseImageList(Long houseId, int type);
}
