package com.atguigu.mapper;

import com.atguigu.base.BaseMapper;
import com.atguigu.entity.HouseBroker;
import com.sun.corba.se.pept.broker.Broker;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 包名:com.atguigu.mapper
 *
 * @author Leevi
 * 日期2022-06-15  15:43
 */
public interface HouseBrokerMapper extends BaseMapper<HouseBroker> {
    /**
     * 根据房源id查询房源的经纪人列表
     * @param id
     * @return
     */
    List<Broker> findHouseBrokerListByHouseId(Long id);

    /**
     * 根据houseId和brokerId查询房源的经纪人
     * @param houseId
     * @param brokerId
     * @return
     */
    HouseBroker getByHouseIdAndBrokerId(@Param("houseId") Long houseId,@Param("brokerId") Long brokerId);
}
