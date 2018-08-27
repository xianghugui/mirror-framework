package com.base.web.dao;

import com.base.web.bean.DeviceAd;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface DeviceAdMapper extends GenericMapper<DeviceAd, Long> {
    List<Map> queryAllAd(QueryParam param);

    Map queryAdById(Long adId);

    //    更改广告状态
    int updateStatusByUId(@Param("uId") Long uId, @Param("status") Integer status);


    /**
     * 查询设备的设备广告信息
     * @param  deviceName
     * @return
     */
    Map queryAdByDevice(String deviceName);
}
