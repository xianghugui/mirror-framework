package com.base.web.controller.api;

import com.base.web.bean.OrderProfit;
import com.base.web.bean.RefundExchange;
import com.base.web.bean.Video;
import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.controller.GenericController;
import com.base.web.controller.wxPay.WXRefund;
import com.base.web.controller.wxPay.WxPayCommon;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.OrderProfitService;
import com.base.web.service.RefundExchangeService;
import com.base.web.service.TUserService;
import com.base.web.service.VideoOrderService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.EvaluationOrderJob;
import com.base.web.util.SchedulerUtil;
import io.swagger.annotations.*;
import org.quartz.DateBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.DateBuilder.futureDate;

@Api(value = "ClientVideoOrderApiController", description = "客户端主页我的询价")
@RequestMapping("/api/clientvideoorder/")
@RestController
public class ClientVideoOrderApiController extends GenericController<VideoOrder, Long> {

    @Resource
    private VideoOrderService videoOrderService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private RefundExchangeService refundExchangeService;

    @Resource
    private TUserService tUserService;

    @Resource
    private OrderProfitService orderProfitService;

    @Resource
    private SchedulerUtil schedulerUtill;

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientVideoOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "不传查全部，0：待报价, 2：待购买, 3：待发货, 4：待收货",
                    required = false, dataType = "Integer", paramType = "query"),
    })
    @ApiOperation(value = "显示全部订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "shopName：店铺名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc：视频图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "lackReason：缺货原因<br>" +
            "num：数量<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage listVideoOrders(QueryParam param, @RequestParam(value = "status", required = false) Integer status,
                                               HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", status);
        return ResponseMessage.ok(videoOrderService.clientShowOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "{orderId}/ClientConfirmReceipt", method = RequestMethod.PUT)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "确认收货")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage ClientConfirmReceipt(@PathVariable("orderId") Long orderId) {
        VideoOrder videoOrder = videoOrderService.selectByPk(orderId);
        if(videoOrder.getStatus() == 3){
            videoOrder.setRemindTime(new Date());
            videoOrder.setStatus(5);
            videoOrderService.update(videoOrder);
            Map map = new HashMap<>();
            map.put("id", orderId);
            map.put("type", 2);
            //7天自动跳过评价
            schedulerUtill.addJob(orderId.toString(), SchedulerUtil.VIDEOORDER, map, EvaluationOrderJob.class,
                    futureDate(5, DateBuilder.IntervalUnit.MINUTE));
        }
        return ResponseMessage.ok("已确认收货");
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
    public ResponseMessage ClientRemindTime(VideoOrder videoOrder) {
        videoOrder.setRemindTime(new Date());
        return ResponseMessage.ok(videoOrderService.update(videoOrder));
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
    public ResponseMessage ClientCancelOrder(@PathVariable("orderId") Long orderId) {
        VideoOrder videoOrder = new VideoOrder();
        videoOrder.setId(orderId);
        videoOrder.setStatus(6);
        videoOrderService.update(videoOrder);
        return ResponseMessage.ok("已取消订单");
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "ClientPay", method = RequestMethod.POST)
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
    public ResponseMessage ClientPay(VideoOrder videoOrder, HttpServletRequest request) {
        if (videoOrder.getAddressId() == null) {
            return ResponseMessage.error("用户收货地址为空");
        }
        VideoOrder videoOrder1 = videoOrderService.selectByPk(videoOrder.getId());
        if(videoOrder1.getStatus() == 2){
            return ResponseMessage.ok("订单已支付");
        }
        videoOrderService.update(videoOrder);

        BigDecimal bigDecimalTypePrice = videoOrder1.getPrice();
        //判断支付类型 0:微信支付，1：余额支付
        if (videoOrder.getPayType() != 0) {
            if (!tUserService.payByEarn(bigDecimalTypePrice)) {
                return ResponseMessage.error("余额不足");
            }
            //余额支付成功直接改状态，避免小程序端后续方法出错后导致支付成功不调用此方法
            return videoPayChangeStatus(videoOrder);
        } else {
            return WxPayCommon.wxPay(videoOrder.getId().toString(), bigDecimalTypePrice, "询价商品", request);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "updateClientVideoOrderBuyStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "支付成功")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage updateClientVideoOrderBuyStatus(VideoOrder videoOrder) {
        return videoPayChangeStatus(videoOrder);
    }

    public ResponseMessage videoPayChangeStatus(VideoOrder videoOrder){
        videoOrder.setStatus(2);
        videoOrder.setPaymentTime(new Date());
        getService().update(videoOrder);
        schedulerUtill.removeJob(videoOrder.getId().toString(), SchedulerUtil.VIDEOORDER);

        videoOrder = videoOrderService.selectByPk(videoOrder.getId());
        //插入付款记录
        OrderProfit orderProfit = new OrderProfit();
        orderProfit.setType(0);
        orderProfit.setUserId(videoOrder.getUserId());
        orderProfit.setPrice(videoOrder.getPrice());
        orderProfit.setChildId(videoOrder.getId());
        orderProfit.setCreateTime(new Date());
        orderProfitService.insert(orderProfit);
        return ResponseMessage.ok("支付成功");
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "ClientBuy", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "提交订单")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage ClientBuy(VideoOrder videoOrder) {
        BigDecimal price = videoOrderService.selectByPk(videoOrder.getId()).getPrice();
        if (videoOrder.getNum() > 1) {
            videoOrder.setPrice(price.multiply(new BigDecimal(videoOrder.getNum())));
        }
        return ResponseMessage.ok(getService().update(videoOrder));
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "ClientRefund", method = RequestMethod.POST)
    @ApiOperation(value = "申请退款(接受JSON类型参数)")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage ClientRefund(RefundExchange refundExchange) {
        //判断是否还能退款，取消退款，拒绝退款可以再次申请
        RefundExchange repeatRefund = refundExchangeService.createQuery()
                .where(RefundExchange.Property.childOrderId, refundExchange.getChildOrderId())
                .and(RefundExchange.Property.userId, WebUtil.getLoginUser().getId())
                .and(RefundExchange.Property.type, 2).single();
        if (repeatRefund != null && repeatRefund.getStatus() != 4) {
            return ResponseMessage.error("已申请退款");
        }

        VideoOrder videoOrder = videoOrderService.selectByPk(refundExchange.getChildOrderId());
        if (refundExchange.getImageIdArray().length > 0) {
            FileRef fileRef = new FileRef();
            fileRef.setRefId(GenericPo.createUID());
            fileRef.setDataType(8);
            for (int i = 0; i < refundExchange.getImageIdArray().length; i++) {
                fileRef.setId(GenericPo.createUID());
                fileRef.setResourceId(refundExchange.getImageIdArray()[i]);
                fileRefService.insert(fileRef);
            }
            refundExchange.setApplyImageId(fileRef.getRefId());
        }
        refundExchange.setUserId(WebUtil.getLoginUser().getId());
        refundExchange.setType(2);
        refundExchange.setStatus(0);
        refundExchange.setPrice(videoOrder.getPrice());
        refundExchange.setShopId(videoOrder.getShopId());
        refundExchange.setId(GenericPo.createUID());
        refundExchange.setApplicationTime(new Date());
        if (videoOrder.getStatus() == 2) {
            if (videoOrder.getPayType() == 0) {
                //微信退款
                String refundCode = null;
                BigDecimal total_fee = refundExchange.getPrice();
                try {
                    refundCode = WXRefund.doRefund(videoOrder.getId(), refundExchange.getId(), total_fee, videoOrder.getPrice());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!"OK".equals(refundCode)) {
                    return ResponseMessage.ok("退款失败");
                }
            } else {
                //余额退款
                tUserService.updateEarn(videoOrder.getPrice(), videoOrder.getUserId());
            }
            OrderProfit orderProfit = new OrderProfit();
            orderProfit.setType(1);
            orderProfit.setUserId(videoOrder.getUserId());
            orderProfit.setPrice(videoOrder.getPrice());
            orderProfit.setChildId(videoOrder.getId());
            orderProfit.setCreateTime(new Date());
            orderProfitService.insert(orderProfit);
            refundExchange.setStatus(3);
        }

        refundExchangeService.addRefundExchange(refundExchange);
        videoOrderService.changeStatus(refundExchange.getChildOrderId(), 4);
        if (videoOrder.getStatus() == 2) {
            schedulerUtill.removeJob(videoOrder.getId().toString(), SchedulerUtil.VIDEOORDER);
        }else{
            schedulerUtill.pauseJob(videoOrder.getId().toString(), SchedulerUtil.VIDEOORDER);
        }
        return ResponseMessage.ok("退款成功");
    }

    @Override
    protected VideoOrderService getService() {
        return this.videoOrderService;
    }
}
