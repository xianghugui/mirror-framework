package com.base.web.controller.api;


import com.base.web.bean.Device;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.session.HttpSessionManager;
import com.base.web.service.DeviceService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "DeviceApiController", description = "设备操作类接口")
@RequestMapping("/api/device/")
@RestController
public class DeviceApiController {
    /**
     * 授权过程所需缓存
     */
    @Autowired(required = false)
    private CacheManager cacheManager;


    /**
     * 设备服务类
     */
    @Resource
    DeviceService deviceService;

    /**
     * httpSession管理器
     */
    @Autowired
    private HttpSessionManager httpSessionManager;



    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @AccessLogger("绑定设备")
    public ResponseMessage login(@RequestParam("username") String username){

        if(username == null || username.equals("")){
            return ResponseMessage.error("请输入设备编号");
        }

        Device device = deviceService.createQuery().where(Device.Property.deviceCode,username).single();

        if (device == null) {
            return ResponseMessage.error("设备不存在");
        }

        return ResponseMessage.ok();
    }

    @RequestMapping(value = "/exit", method = RequestMethod.POST)
    @AccessLogger("解绑设备")
    public ResponseMessage exit(@RequestParam("username") String username,
                                @RequestParam("password") String password){

        Device device = deviceService.createQuery().where(Device.Property.username,username)
                .and(Device.Property.password,password).single();

        if (device == null) {
            return ResponseMessage.error("账号或密码错误");
        }

        return ResponseMessage.ok("登出成功");
    }





}
