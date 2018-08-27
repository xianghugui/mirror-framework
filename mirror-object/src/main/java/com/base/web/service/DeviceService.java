package com.base.web.service;

import com.base.web.bean.Device;


import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:31  2018/4/2
 */
public interface DeviceService extends GenericService<Device, Long> {
    //启用和禁用设备
    int changeStatus(String status, String deviceId);

    //查询店铺下所属设备
    List<Map> querydevicebyshopid(String shopId);

    //<!--查询未分配的设备且状态正常-->
    List<Map> queryUndistributedDevice();
}
