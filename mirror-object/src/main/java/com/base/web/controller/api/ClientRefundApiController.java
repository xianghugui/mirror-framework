package com.base.web.controller.api;

import com.base.web.bean.RefundExchange;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.LogisticsService;
import com.base.web.service.RefundExchangeService;
import com.base.web.util.SchedulerUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.Date;

@Api(value = "ClientRefundApiController", description = "客户端主页退货退款")
@RequestMapping("/api/clientrefund/")
@RestController
public class ClientRefundApiController {

    @Resource
    private RefundExchangeService refundExchangeService;

    @Resource
    private SchedulerUtil schedulerUtil;

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientRefund/{type}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "切换类型 1 购物单 2 询价单",
                    required = true, dataType = "int", paramType = "query")
    })
    @ApiOperation(value = "显示全部退款订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "refundId：退款ID<br>" +
            "type：订单类型：1 购物， 2：询价<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "videoImageSrc：视频图片资源路径<br>" +
            "videoSrc：视频资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：退款单状态，0:申请退货退款 1：退货中 2：商家确认收货  3:完成退款请求 4：退款请求关闭（用户操作)<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientRefund(QueryParam param, @PathVariable("type") String type, HttpServletRequest req) {
        param.getParam().put("type",type);
        param.getParam().put("userId", WebUtil.getLoginUser().getId());
        return ResponseMessage.ok(refundExchangeService.clientShowRefunds(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "{orderId}/showClientRefundAddress", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "退货订单ID",
                    required = true, dataType = "Long", paramType = "path"),
    })
    @ApiOperation(value = "显示退货地址")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientRefundAddress(@PathVariable("orderId") Long orderId) {
        return ResponseMessage.ok(refundExchangeService.showClientRefundAddress(orderId));
    }

    //------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{orderId}/delivery", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "expressName", value = "发货公司", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "expressNumber", value = "运单号", required = true, dataType = "String", paramType = "form"),
    })
    @ApiOperation(value = "上传快递信息")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage delivery(@PathVariable("orderId") Long orderId, @PathParam("expressId") String expressId,
                                    @PathParam("expressNumber") String expressNumber) {
        Integer type = refundExchangeService.selectByPk(orderId).getType();
        if (type == 0){
            schedulerUtil.removeJob(orderId.toString(), SchedulerUtil.ORDER);
        }else if(type == 2){
            schedulerUtil.removeJob(orderId.toString(), SchedulerUtil.VIDEOORDER);
        }
        return ResponseMessage.ok(refundExchangeService.delivery(orderId, expressId, expressNumber));
    }


    @RequestMapping(value = "clientRemindTime", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "提醒发货")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage ClientRemindTime(RefundExchange refundExchange) {
        refundExchange.setRemindTime(new Date());
        return ResponseMessage.ok(refundExchangeService.update(refundExchange));
    }


}
