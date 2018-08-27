package com.base.web.dao;

import com.base.web.bean.ShopDevice;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface ShopDeviceMapper extends GenericMapper<ShopDevice, Long> {
    //查询店铺下所属设备
    List<Map> querydevicebyshopid(String shopId);
    int deldevice(String deviceId);
}
