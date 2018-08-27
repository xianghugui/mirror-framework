package com.base.web.controller;

import com.base.web.bean.ShopDevice;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GenericService;
import com.base.web.service.ShopDeviceService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:32  2018/4/3
 */
@RestController
@RequestMapping(value = "/shopdevice")
@AccessLogger("店铺设备")
public class ShopDeviceController extends GenericController<ShopDevice, Long> {

    @Resource
    private ShopDeviceService shopDeviceService;

    @Override
    protected GenericService<ShopDevice, Long> getService() {
        return this.shopDeviceService;
    }
}
