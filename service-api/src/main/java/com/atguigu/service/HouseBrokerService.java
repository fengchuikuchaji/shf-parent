package com.atguigu.service;

import com.atguigu.base.BaseService;
import com.atguigu.entity.HouseBroker;
import com.sun.corba.se.pept.broker.Broker;

import java.util.List;

/**
 * 包名:com.atguigu.service
 *
 * @author Leevi
 * 日期2022-06-15  15:42
 */
public interface HouseBrokerService extends BaseService<HouseBroker> {
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
    HouseBroker getByHouseIdAndBrokerId(Long houseId, Long brokerId);
}
