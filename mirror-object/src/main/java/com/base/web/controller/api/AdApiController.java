package com.base.web.controller.api;


import com.base.web.bean.common.QueryParam;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.DeviceAdService;
import com.base.web.service.resource.FileRefService;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 广告查询接口类
 * 2018-07-2
 *
 * @Author: FQ
 * @Date: 2018-07-2 15:07
 */

@Api(value = "AdApiController", description = "广告查询接口类")
@RequestMapping("/api/ad")
@RestController
public class AdApiController {

    @Resource
    private DeviceAdService deviceAdService;

    @Resource
    private FileRefService fileRefService;


    @ApiOperation(value = "设备广告查询接口", notes = "设备广告查询接口Map中Map数据:" +
            "<br>id : 主键 Long" +
            "<br>userName : 广告发布人 String" +
            "<br>adDataId : 设备广告关联id Long" +
            "<br>status : 广告状态（0待发布，1已发布） Integer"+
            "<br>createTime : 广告发布时间 Data" +
            "<br>startTime : 广告开始时间 Data"+
            "<br>endTime : 广告结束时间 Data", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})

    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "String",
                    name = "deviceName", value = "设备名", required = true)
            })
    @RequestMapping(value = "/queryDeviceAd/{deviceName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryMessageList(@PathVariable("deviceName") String deviceName, HttpServletRequest req) {
        Map devieAdInfo = deviceAdService.queryAdByDevice(deviceName);
        QueryParam param = new QueryParam();
        if(devieAdInfo == null){
            return ResponseMessage.ok(new Object());
        }
        param.getParam().put("refId",devieAdInfo.get("adDataId"));
        devieAdInfo.put("videoSrc",fileRefService.queryTypeByRefId(param,req).get(0).get("resourceUrl"));
        return ResponseMessage.ok(devieAdInfo);
    }




}
