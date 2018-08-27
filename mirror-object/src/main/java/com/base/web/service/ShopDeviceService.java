package com.base.web.service;

import com.base.web.bean.ShopDevice;



/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface ShopDeviceService extends GenericService<ShopDevice,Long> {
    int deldevice(String deviceId);
}
