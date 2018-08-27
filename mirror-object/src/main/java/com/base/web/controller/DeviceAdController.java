package com.base.web.controller;

import com.base.web.bean.DeviceAd;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.DeviceAdService;
import com.base.web.service.resource.FileRefService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;


/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/DevicePush")
@AccessLogger("设备广告")
//@Authorize(module = "DeviceAd")
public class DeviceAdController extends GenericController<DeviceAd, Long> {

    @Resource
    private DeviceAdService deviceAdService;

    @Resource
    private FileRefService fileRefService;

    @Override
    protected DeviceAdService getService() {
        return this.deviceAdService;
    }


    @RequestMapping(value = "/queryAllAd",method = RequestMethod.GET)
    @AccessLogger("查询全部设备广告")
    @Authorize(action = "R")
    public ResponseMessage queryAll(QueryParam param,HttpServletRequest request){
        PagerResult<Map> data=deviceAdService.queryAllAd(param);
        for(Map deviceAd:data.getData()){
            param.getParam().put("refId",deviceAd.get("adDataId"));
            deviceAd.put("resourcePath",fileRefService.queryTypeByRefId(param,request));
        }
        return ok(data)
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }

    @RequestMapping(value = "/queryAdById/{adId}",method = RequestMethod.GET)
    @AccessLogger("查询")
    @Authorize(action = "R")
    public ResponseMessage queryAdById(@PathVariable("adId") Long adId){
        return ok(getService().queryAdById(adId));
    }



}
