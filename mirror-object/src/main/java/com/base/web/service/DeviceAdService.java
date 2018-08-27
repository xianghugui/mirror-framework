package com.base.web.service;

import com.base.web.bean.DeviceAd;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface DeviceAdService extends GenericService<DeviceAd,Long> {

    // >>查询全部设备广告
    PagerResult<Map> queryAllAd(QueryParam param);

    // >>根据广告id查询设备广告
    Map queryAdById(Long adId);

    //更改广告状态 0 未发布，1 已发布
    int updateStatusByUId(Long uId,Integer status);

    Map queryAdByDevice(String deviceName);

}
