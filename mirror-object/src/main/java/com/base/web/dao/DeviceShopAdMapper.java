package com.base.web.dao;

import com.base.web.bean.DeviceShopAd;


/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface DeviceShopAdMapper extends GenericMapper<DeviceShopAd, Long> {
  int cancelPush(Long adDataId);
}
