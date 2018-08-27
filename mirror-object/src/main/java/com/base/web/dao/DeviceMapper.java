package com.base.web.dao;

import com.base.web.bean.Device;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:30  2018/4/2
 */
public interface DeviceMapper extends GenericMapper<Device, Long> {

    //启用和禁用设备
    int changeStatus(@Param("status") String status, @Param("deviceId") String deviceId);

    //<!--查询未分配的设备且状态正常-->
    List<Map> queryUndistributedDevice();
}
