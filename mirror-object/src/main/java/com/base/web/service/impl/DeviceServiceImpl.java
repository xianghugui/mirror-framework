package com.base.web.service.impl;

import com.base.web.bean.Device;
import com.base.web.dao.DeviceMapper;
import com.base.web.dao.ShopDeviceMapper;
import com.base.web.service.DeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:32  2018/4/2
 */
@Service("DeviceService")
public class DeviceServiceImpl extends AbstractServiceImpl<Device, Long> implements DeviceService {

    @Resource
    private DeviceMapper deviceMapper;

    @Resource
    private ShopDeviceMapper shopDeviceMapper;

    @Override
    protected DeviceMapper getMapper() {
        return this.deviceMapper;
    }

    @Override
    public int changeStatus(String status, String deviceId) {
        return getMapper().changeStatus(status, deviceId);
    }

    @Override
    public List<Map> querydevicebyshopid(String shopId) {
        return shopDeviceMapper.querydevicebyshopid(shopId);
    }

    @Override
    public List<Map> queryUndistributedDevice() {
        return deviceMapper.queryUndistributedDevice();
    }
}
