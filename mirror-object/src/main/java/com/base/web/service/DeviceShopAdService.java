package com.base.web.service;

import com.base.web.bean.DeviceShopAd;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface DeviceShopAdService extends GenericService<DeviceShopAd,Long> {

    int insertDeviceShopAdList(DeviceShopAd deviceShopAd);

    int cancelPush(Long adDataId);
}
