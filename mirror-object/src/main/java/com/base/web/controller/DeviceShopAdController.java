package com.base.web.controller;

import com.base.web.bean.DeviceShopAd;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.DeviceShopAdService;
import com.base.web.service.resource.ResourcesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.base.web.core.message.ResponseMessage.ok;


/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/DeviceShopAd")
@AccessLogger("店铺设备广告")
@Authorize(module = "deviceshopad")
public class DeviceShopAdController extends GenericController<DeviceShopAd, Long> {

    @Resource
    private DeviceShopAdService DeviceShopAdService;

    @Resource
    private ResourcesService resourcesService;

    
    @Override
    protected DeviceShopAdService getService() {
        return this.DeviceShopAdService;
    }


    @RequestMapping(value = "/pic/{adDataId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage pic(@PathVariable("adDataId") Long adDataId) {
        List<Long> list = resourcesService.selectImagesResourcesID(adDataId);
        return ok(list);
    }

    @RequestMapping(value = "/insertDeviceShopAdList",method = RequestMethod.POST)
    @AccessLogger("新增")
    @Authorize(action = "C")
    public ResponseMessage insertDeviceShopAdList(@RequestBody DeviceShopAd deviceShopAd){
        return  ResponseMessage.ok(DeviceShopAdService.insertDeviceShopAdList(deviceShopAd));
    }

    @RequestMapping(value = "/cancelPush/{adDataId}",method = RequestMethod.PUT)
    @AccessLogger("删除")
    @Authorize(action = "D")
    public ResponseMessage cancelPush(@PathVariable("adDataId") Long adDataId){
        return  ResponseMessage.ok(DeviceShopAdService.cancelPush(adDataId));
    }


    
}
