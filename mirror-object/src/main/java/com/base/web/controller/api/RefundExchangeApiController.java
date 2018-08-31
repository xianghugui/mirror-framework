package com.base.web.controller.api;

import com.base.web.bean.OrderDetail;
import com.base.web.bean.OrderProfit;
import com.base.web.bean.RefundExchange;
import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.QueryParam;
import com.base.web.controller.GenericController;
import com.base.web.controller.wxPay.WXRefund;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.SchedulerUtil;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Api(value = "RefundExchangeApiController", description = "商家端退货退款")
@RequestMapping("/api/refundexchange/")
@RestController
public class RefundExchangeApiController extends GenericController<RefundExchange, Long> {

    @Resource
    private RefundExchangeService refundExchangeService;

    @Resource
    private ShopService shopService;

    @Resource
    private OrderService orderService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private VideoOrderService videoOrderService;

    @Resource
    private GoodsSpeService goodsSpeService;

    @Resource
    private TUserService tUserService;

    @Resource
    private OrderProfitService orderProfitService;

    @Resource
    private SchedulerUtil schedulerUtil;

    @RequestMapping(value = "showRefunds", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示全部订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>" +
            "List&lt;Map&gt;｛<br>" +
            "refundId：退款表ID<br>" +
            "orderId：订单ID<br>" +
            "applicationTime：申请时间<br>" +
            "type：退货单类型，0：平台购买，1：试衣购买，2：视频购买<br>" +
            "status：状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，6：退款请求关闭（用户操作）7：商家拒绝退款<br>" +
            "userName：申请人名称<br>" +
            "price：退款金额<br>" +
            "videoImageSrc：视频图片资源路径<br>" +
            "videoSrc：视频资源路径<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showRefunds(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        return ResponseMessage.ok(refundExchangeService.showRefunds(param, req));
    }
//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showOrderRefunds", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示购物单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>" +
            "List&lt;Map&gt;｛<br>" +
            "refundId：退款表ID<br>" +
            "orderId：订单ID<br>" +
            "applicationTime：申请时间<br>" +
            "status：状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，6：退款请求关闭（用户操作）7：商家拒绝退款<br>" +
            "userName：申请人名称<br>" +
            "price：退款金额<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showOrderRefunds(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        return ResponseMessage.ok(refundExchangeService.showOrderRefunds(param, req));
    }
//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showTryGoodsRefunds", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示试衣单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>" +
            "List&lt;Map&gt;｛<br>" +
            "refundId：退款表ID<br>" +
            "orderId：订单ID<br>" +
            "applicationTime：申请时间<br>" +
            "status：状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，6：退款请求关闭（用户操作）7：商家拒绝退款<br>" +
            "userName：申请人名称<br>" +
            "price：退款金额<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showTryGoodsRefunds(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        return ResponseMessage.ok(refundExchangeService.showTryGoodsRefunds(param, req));
    }

//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showVideoOrderRefunds", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示询价单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>" +
            "List&lt;Map&gt;｛<br>" +
            "refundId：退款表ID<br>" +
            "orderId：订单ID<br>" +
            "applicationTime：申请时间<br>" +
            "status：状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，6：退款请求关闭（用户操作）7：商家拒绝退款<br>" +
            "userName：申请人名称<br>" +
            "price：退款金额<br>" +
            "videoSrc：视频资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showVideoOrderRefunds(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        return ResponseMessage.ok(refundExchangeService.showVideoOrderRefunds(param, req));
    }

//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{refundId}/showRefundsInfo", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId", value = "退款ID",
                    required = true, dataType = "String", paramType = "path")
    })
    @ApiOperation(value = "显示退款详情", notes = "返回data为｛<br>" +
            "refundId：退款ID<br>" +
            "content：退款原因<br>" +
            "price：退款金额<br>" +
            "applicationTime：申请时间<br>" +
            "dealTime：处理时间<br>" +
            "status：状态0:申请退货退款 1：同意退货 2：用户退货3:商家确认收货 4：平台退款，5：完成退款请求，6：退款请求关闭（用户操作）7：商家拒绝退款<br>")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showRefundsInfo(@PathVariable("refundId") String refundId, Integer orderType, HttpServletRequest req) {
        QueryParam param = new QueryParam();
        param.getParam().put("refundId", refundId);
        param.getParam().put("orderType", orderType);
        Map map = refundExchangeService.showRefundsInfo(param);
        //申请退款图片
        if (map.get("altImageSrc") != null) {
            param.getParam().put("dataType", 8);
            param.getParam().put("recordId", map.get("altImageSrc").toString());
            List<Map> imgs2 = fileRefService.queryResourceByRecordId(param, req);
            if (imgs2 != null && imgs2.size() > 0) {
                map.put("altImageSrc", imgs2);
            }
        }
        return ResponseMessage.ok(map);
    }

//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "agreeRefunds", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId", value = "退款ID",
                    required = true, dataType = "String", paramType = "path")
    })
    @ApiOperation(value = "同意退款")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage agreeRefunds(RefundExchange refundExchange) {
        refundExchange.setStatus(1);
        refundExchange.setRemindTime(null);
        return ResponseMessage.ok(refundExchangeService.update(refundExchange));
    }

//----------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{refundId}/refuseRefunds", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId", value = "退款ID",
                    required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "files", value = "图片",
                    required = true, dataType = "file", format = "byte", paramType = "form"),
            @ApiImplicitParam(name = "refuseReason", value = "拒绝原因",
                    required = true, dataType = "String", paramType = "form"),
    })
    @ApiOperation(value = "拒绝退款")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage refuseRefunds(@RequestParam("files") Long[] files, @PathVariable("refundId") String refundId,
                                         @RequestParam("refuseReason") String refuseReason) throws IOException {

        if (files == null || !(files.length > 0)) {
            return ResponseMessage.ok(refundExchangeService.refuseRefunds(refundId, null, refuseReason));
        }
        Long refId = fileRefService.addImageRefId(files, 7);

        return ResponseMessage.ok(refundExchangeService.refuseRefunds(refundId, refId, refuseReason));
    }


    //----------------------------------------------------------------------------------------------------

    //退款单状态，0:申请退货 1:退货中 2:商家确认收货 3：完成退款请求，4：退款请求关闭（用户操作)
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId", value = "退款ID",
                    required = true, dataType = "String", paramType = "path")
    })
    @ApiOperation(value = "确认收货")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage confirm(RefundExchange refundExchange) {
        //确认收货后减少商品数量
        refundExchange = getService().selectByPk(refundExchange.getId());
        if (refundExchange.getStatus() == 1) {
            Integer payType;
            OrderProfit orderProfit = new OrderProfit();
            //购物订单
            if (refundExchange.getType() == 0) {
                OrderDetail orderDetail = orderDetailService.selectByPk(refundExchange.getChildOrderId());
                //取相反数
                goodsSpeService.updateGoodsSpecQuality(orderDetail.getGoodsId(), ~orderDetail.getNum() + 1);
                payType = orderDetailService.selectByPk(refundExchange.getChildOrderId()).getPayType();
                orderProfit.setGoodsId(orderDetail.getGoodsId());
                orderProfit.setPrice(orderDetail.getPrice());
            } else {
                //询价订单
                VideoOrder videoOrder = videoOrderService.selectByPk(refundExchange.getChildOrderId());
                payType = videoOrder.getPayType();
                orderProfit.setPrice(videoOrder.getPrice());
            }
            if (payType != null && payType == 0) {
                refundExchange.setStatus(2);
            } else if (payType != null && payType == 1) {
                tUserService.updateEarn(refundExchange.getPrice(), refundExchange.getUserId());
                //退款添加到交易记录
                orderProfit.setUserId(refundExchange.getUserId());
                orderProfit.setType(1);
                orderProfit.setChildId(refundExchange.getId());
                orderProfit.setCreateTime(new Date());
                orderProfitService.insert(orderProfit);
                refundExchange.setStatus(3);
            }
            getService().update(refundExchange);
        }
        return ResponseMessage.ok("已确认收货");
    }

    @RequestMapping(value = "{refundId}/cancelRefunds", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "refundId", value = "退款ID",
                    required = true, dataType = "String", paramType = "path")
    })
    @ApiOperation(value = "用户取消退款")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage cancelRefunds(@PathVariable("refundId") String refundId) {
        // type 退货单类型（0 平台购买， 2视频购买）
        RefundExchange refundExchange = refundExchangeService.selectByPk(Long.valueOf(refundId));
        if (refundExchange.getType() == 0) {
            orderDetailService.changeStatus(refundExchange.getChildOrderId(), 3);
            schedulerUtil.resumeJob(refundExchange.getChildOrderId().toString(), SchedulerUtil.ORDER);
        }
        if (refundExchange.getType() == 2) {
            videoOrderService.changeStatus(refundExchange.getChildOrderId(), 3);
            schedulerUtil.resumeJob(refundExchange.getChildOrderId().toString(), SchedulerUtil.VIDEOORDER);
        }

        refundExchangeService.delete(refundExchange.getId());
        return ResponseMessage.ok("已取消退款");
    }

    @Override
    protected RefundExchangeService getService() {
        return this.refundExchangeService;
    }


    //退款单状态，0:申请退货 1:退货中 2:商家确认收货 3：完成退款请求，4：退款请求关闭（用户操作)
    @RequestMapping(value = "refund/{orderId}", method = RequestMethod.GET)
    public ResponseMessage refund(@PathVariable("orderId") Long orderId) {
        RefundExchange refundExchange = refundExchangeService.selectByPk(orderId);
        OrderProfit orderProfit = new OrderProfit();

        Long out_trade_no;
        BigDecimal total_fee;
        BigDecimal refund_fee;

        //平台购买
        if (refundExchange.getType() == 0) {
            OrderDetail orderDetail = orderDetailService.selectByPk(refundExchange.getChildOrderId());
            out_trade_no = refundExchange.getParentOrderId();
            total_fee = orderService.selectByPk(refundExchange.getParentOrderId()).getTotalPrice();
            refund_fee = orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum()));

            orderProfit.setParentId(orderDetail.getOrderId());
            orderProfit.setGoodsId(orderDetail.getGoodsId());
            orderProfit.setPrice(orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum())));

        } else {
            out_trade_no = refundExchange.getChildOrderId();
            total_fee = refundExchange.getPrice();
            refund_fee = refundExchange.getPrice();
            orderProfit.setPrice(refundExchange.getPrice());
        }
        if (refundExchange != null) {
            Long out_refund_no = refundExchange.getId();
            //微信退款
            try {
                String refundCode = WXRefund.doRefund(out_trade_no, out_refund_no, total_fee, refund_fee);
                if ("OK".equals(refundCode)) {
                    refundExchange.setStatus(3);
                    refundExchangeService.update(refundExchange);
                } else {
                    return ResponseMessage.error("退款失败");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 添加退款交易记录
        orderProfit.setChildId(refundExchange.getChildOrderId());
        orderProfit.setCreateTime(new Date());
        orderProfit.setType(1);
        orderProfit.setUserId(refundExchange.getUserId());
        orderProfitService.insert(orderProfit);

        return ResponseMessage.ok("退款成功");
    }
}

