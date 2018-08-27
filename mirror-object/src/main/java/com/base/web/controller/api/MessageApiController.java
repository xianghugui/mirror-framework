package com.base.web.controller.api;


import com.base.web.bean.common.QueryParam;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.ShopAdService;
import com.base.web.service.ShopService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * 资讯查询接口类
 * 2018-07-2
 *
 * @Author: FQ
 * @Date: 2018-07-2 15:07
 */

@Api(value = "MessageApiController", description = "消息查询接口类")
@RequestMapping("/api/message")
@RestController
public class MessageApiController {
    @Resource
    private ShopAdService shopAdService;

    @Resource
    private ShopService shopService;


    @ApiOperation(value = "商家端资讯列表查询接口", notes = "资讯列表查询接口List<Map>中Map数据:" +
            "<br>id : 消息主键 Long" +
            "<br>title : 消息标题 String" +
            "<br>content : 消息内容 String" +
            "<br>createTime : 创建时间 Data", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})

    @RequestMapping(value = "/queryShopMessageList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryMessageList(QueryParam param,Long userId) {
        param.getParam().put("shopId",shopService.queryShopIdByUserId(userId));
        return ResponseMessage.ok(shopAdService.queryShopAd(param));
    }


    @ApiOperation(value = "用户端资讯列表查询接口", notes = "资讯列表查询接口List<Map>中Map数据:" +
            "<br>id : 消息主键 Long" +
            "<br>title : 消息标题 String" +
            "<br>content : 消息内容 String" +
            "<br>createTime : 创建时间 Data", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})

    @RequestMapping(value = "/queryUserMessageList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryUserMessageList(QueryParam param,Long userId) {
        param.getParam().put("userId",userId);
        return ResponseMessage.ok(shopAdService.queryUserAd(param));
    }

    @ApiOperation(value = "资讯详情查询接口", notes = "资讯列表查询接口List<Map>中Map数据:" +
            "<br>id : 消息主键 Long" +
            "<br>title : 消息标题 String" +
            "<br>content : 消息内容 String" +
            "<br>createTime : 创建时间 Data", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})

    @RequestMapping(value = "/queryMessageInfo/{shopAdId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryMessageInfo(@PathVariable("shopAdId") Long shopAdId) {
        return ResponseMessage.ok(shopAdService.queryShopAdInfo(shopAdId));
    }




}
