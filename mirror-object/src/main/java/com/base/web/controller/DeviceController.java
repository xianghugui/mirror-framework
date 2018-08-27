package com.base.web.controller;

import com.base.web.bean.Device;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.DeviceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.Date;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 13:34  2018/4/2
 */
@RestController
@RequestMapping(value = "/device")
@AccessLogger("设备管理")
@Authorize(module = "device")
public class DeviceController extends GenericController<Device, Long> {

    @Resource
    private DeviceService deviceService;

    @Override
    protected DeviceService getService() {
        return this.deviceService;
    }

    @RequestMapping(value = "/querydevice", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryDevice(QueryParam param){
        Object data = this.deviceService.select(param);
        return ResponseMessage.ok(data);
    }
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Authorize(action = "C")
    public ResponseMessage addDevice(@RequestBody Device device) {
        Device old =   getService().createQuery().where(Device.Property.username,device.getUsername()).single();
        Device old2 =   getService().createQuery().where(Device.Property.deviceCode,device.getDeviceCode()).single();
        if(old != null){
           return   ResponseMessage.error("用户名已存在，请重新填写",201);
        }
        if(old2 != null){
            return   ResponseMessage.error("设备编码已存在，请重新填写",202);
        }
        device.setCreateTime(new Date());
        device.setStatus(0);
        return ResponseMessage.ok(this.deviceService.insert(device));
    }
    @RequestMapping(value = "/{deviceId}/enable",method = RequestMethod.PUT)
    public ResponseMessage shopEnable(@PathVariable("deviceId")String deviceId){
        return ResponseMessage.ok(getService().changeStatus("0",deviceId));
    }
    @RequestMapping(value = "/{deviceId}/disable",method = RequestMethod.PUT)
    public ResponseMessage shopDisable(@PathVariable("deviceId")String deviceId){
        return ResponseMessage.ok(getService().changeStatus("2",deviceId));
    }
    @RequestMapping(value = "/{deviceId}/repair",method = RequestMethod.PUT)
    public ResponseMessage shopRepair(@PathVariable("deviceId")String deviceId){
        return ResponseMessage.ok(getService().changeStatus("1",deviceId));
    }
    @RequestMapping(value = "/querydevicebyshopid/{shopId}",method = RequestMethod.GET)
    public ResponseMessage querydevicebyshopid(@PathVariable("shopId")String shopId){
        return ResponseMessage.ok(getService().querydevicebyshopid(shopId));
    }
    @RequestMapping(value = "/queryUndistributedDevice",method = RequestMethod.GET)
    public ResponseMessage queryUndistributedDevice(){
        return ResponseMessage.ok(getService().queryUndistributedDevice());
    }
}
