package com.base.web.controller.api;

import com.base.web.bean.*;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.controller.wxPay.WxPayCommon;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Api(value = "ClientOrderApiController", description = "客户端主页我的试穿")
@RequestMapping("/api/clienttryorder/")
@RestController
public class ClientTryGoodsApiController {

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private RefundExchangeService refundExchangeService;

    @Resource
    private TUserService tUserService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private GoodsSpeService goodsSpeService;

    @Resource
    private ShopService shopService;

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientTryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示全部订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientTryOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", null);
        return ResponseMessage.ok(tryInfoService.clientShowOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientWaiting", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示待发货订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientWaiting(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", 0);
        return ResponseMessage.ok(tryInfoService.clientShowOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientTryOn", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示试穿中订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientTryOn(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", 2);
        return ResponseMessage.ok(tryInfoService.clientShowOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientComment", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示待评价订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：状态，0待派单，showClientWaiting待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showClientComment(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", 6);
        return ResponseMessage.ok(tryInfoService.clientShowOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "{orderId}/ClientCancelOrder", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "取消订单")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage ClientCancelOrder(@PathVariable("orderId") Long orderId) {

        //添加试衣次数
//        TUser tUser = tUserService.selectByPk(WebUtil.getLoginUser().getId());
//        tUser.setResidueDegree(tUser.getResidueDegree() + 1);
//        tUserService.update(tUser);
        //添加商品数量
        TryInfo tryInfo = tryInfoService.selectByPk(orderId);
        GoodsSpecification goodsSpecification = goodsSpeService.selectByPk(tryInfo.getGoodsSpecId());
        goodsSpecification.setQuality(goodsSpecification.getQuality() + tryInfo.getNum());
        tryInfoService.changeStatus(orderId, 8);
        return ResponseMessage.ok(goodsSpeService.update(goodsSpecification));
    }

    //------------------------------------------------------------------------------------------------------------------
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
    public ResponseMessage ClientRemindTime(TryInfo tryInfo) {
        tryInfo.setRemindTime(new Date());
        return ResponseMessage.ok(tryInfoService.update(tryInfo));
    }


    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "ClientRefund", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "files", value = "图片",
                    required = true, dataType = "file", format = "byte", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "申请原因",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "refundType", value = "退款类型0:仅退款,1退货退款",
                    required = true, dataType = "int", paramType = "form")
    })
    @ApiOperation(value = "申请退款(接受JSON类型参数)")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage ClientRefund(@RequestBody RefundExchange refundExchange) {
        //判断是否还能退款，取消退款，拒绝退款可以再次申请
        RefundExchange repeatRefund =refundExchangeService.createQuery()
                .where(RefundExchange.Property.childOrderId, refundExchange.getChildOrderId())
                .and(RefundExchange.Property.userId, WebUtil.getLoginUser().getId())
                .and(RefundExchange.Property.type, 1).single();
        if(repeatRefund != null && (repeatRefund.getStatus() != 6 || repeatRefund.getStatus() != 7)){
            return ResponseMessage.ok("已申请退款");
        }
        //申请退款
        TryInfo tryInfo = tryInfoService.selectByPk(refundExchange.getChildOrderId());
        if(refundExchange.getImageIdArray().length > 0){
            Long refId = fileRefService.addImageRefId(refundExchange.getImageIdArray(), 8);
            refundExchange.setApplyImageId(refId);
        }
        refundExchange.setUserId(WebUtil.getLoginUser().getId());
        refundExchange.setType(1);
        refundExchange.setStatus(0);
        refundExchange.setPrice(tryInfo.getPrice());
        refundExchange.setShopId(tryInfo.getShopId());
        refundExchange.setApplicationTime(new Date());
        refundExchange.setId(GenericPo.createUID());
        tryInfoService.changeStatus(refundExchange.getChildOrderId(), 4);
        return ResponseMessage.ok(refundExchangeService.addRefundExchange(refundExchange));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "ClientComment", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "files", value = "图片",
                    required = true, dataType = "file", format = "byte", paramType = "form"),
            @ApiImplicitParam(name = "content", value = "评价内容",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "star", value = "评价星级:1-5",
                    required = true, dataType = "int", paramType = "form")
    })
    @ApiOperation(value = "评价")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage ClientComment(@RequestBody GoodsComment goodsComment) {
        Long refId = fileRefService.addImageRefId(goodsComment.getFiles(), 6);
        goodsComment.setOrderType(goodsComment.getOrderType());
        goodsComment.setImageId(refId);
        goodsComment.setUserId(WebUtil.getLoginUser().getId());
        goodsComment.setCreateTime(new Date());
        goodsComment.setId(GenericPo.createUID());
        tryInfoService.changeStatus(goodsComment.getOrderId(), 7);
        goodsCommentService.addGoodsComment(goodsComment);
        TryInfo tryInfo = tryInfoService.selectByPk(goodsComment.getOrderId());
        BigDecimal price = tryInfo.getPrice();
//        //订单分润
//        OrderProfit orderProfit = new OrderProfit();
//        orderProfit.setOrderUserId(goodsComment.getUserId());
//        orderProfit.setType(0);
//        orderProfit.setChildId(goodsComment.getOrderId());
//        orderProfit.setGoodsId(goodsComment.getGoodsId());
//        orderProfit.setPrice(price);
//        orderProfit.setCreateTime(new Date());
//        tUserService.OrderProfit(orderProfit, shopService.selectByPk(tryInfo.getShopId()).getUserId());
        return ResponseMessage.ok("评价成功");
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "付款")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage submitOrder(TryInfo tryInfo, HttpServletRequest request) {
        BigDecimal bigDecimalTypePrice = goodsService.selectByPk(tryInfo.getGoodsId()).getPrice();
        tryInfo.setPrice(bigDecimalTypePrice);
        //判断支付类型 0:微信支付，1：余额支付
        if (tryInfo.getPayType() != 0) {
            if (!tUserService.payByEarn(bigDecimalTypePrice)) {
                return ResponseMessage.error("余额不足");
            }
            tryInfo.setStatus(3);
        } else {
            tryInfoService.update(tryInfo);
            String goodsName = goodsService.selectByPk(tryInfo.getGoodsId()).getGoodsName();
            return WxPayCommon.wxPay(tryInfo.getId().toString(), bigDecimalTypePrice, goodsName, request);
        }
        tryInfoService.update(tryInfo);
        return ResponseMessage.ok("支付成功");
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/updateClientTryOrderBuyStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "支付完成调用")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage updateClientTryOrderBuyStatus(TryInfo tryInfo) {
        tryInfo.setStatus(3);
        tryInfo.setBuyTime(new Date());
        tryInfoService.update(tryInfo);
        return ResponseMessage.ok("支付成功");
    }

}
