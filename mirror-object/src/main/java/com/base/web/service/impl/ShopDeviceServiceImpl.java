package com.base.web.service.impl;

import com.base.web.bean.ShopDevice;
import com.base.web.dao.ShopDeviceMapper;
import com.base.web.service.ShopDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("ShopDeviceService")
public class ShopDeviceServiceImpl extends AbstractServiceImpl<ShopDevice, Long> implements ShopDeviceService{

    @Resource
    private ShopDeviceMapper shopDeviceMapper;
    
    @Override
    protected ShopDeviceMapper getMapper() {
        return this.shopDeviceMapper;
    }

    @Override
    public int deldevice(String deviceId) {
        return getMapper().deldevice(deviceId);
    }
}
