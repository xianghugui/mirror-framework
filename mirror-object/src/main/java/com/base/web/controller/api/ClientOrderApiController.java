package com.base.web.controller.api;

import com.base.web.bean.*;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.controller.wxPay.WXRefund;
import com.base.web.controller.wxPay.WxPayCommon;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.EvaluationOrderJob;
import com.base.web.util.SchedulerUtil;
import io.swagger.annotations.*;
import org.apache.log4j.Logger;
import org.quartz.DateBuilder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.util.*;

import static org.quartz.DateBuilder.futureDate;

@Api(value = "ClientOrderApiController", description = "客户端主页我的购物")
@RequestMapping("/api/clientorder/")
@RestController
public class ClientOrderApiController {
    private static Logger log = Logger.getLogger(ClientOrderApiController.class);
    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private RefundExchangeService refundExchangeService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private TUserService tUserService;

    @Resource
    private OrderService orderService;

    @Resource
    private GoodsSpeService goodsSpeService;

    @Resource
    private SchedulerUtil schedulerUtil;

    @Resource
    private GoodsService goodsService;

    @Resource
    private OrderProfitService orderProfitService;

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "showClientOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "status", value = "不传查全部，0：待付款, 2：待发货, 3：待收货, 4：待评价",
                    required = false, dataType = "Integer", paramType = "query"),
    })
    @ApiOperation(value = "显示全部订单", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "imageSrc：图片资源路径<br>" +
            "price：价格<br>" +
            "size：规格<br>" +
            "color：颜色<br>" +
            "num：数量<br>" +
            "status：状态，0:待付款， 1 待派单，2：待发货，3：待收货，4：待评价，5：已评价，6：退货，7：订单关闭<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage listOrders(QueryParam param,@RequestParam(value = "status", required = false) Integer status,
                                      HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        param.getParam().put("userId", userId);
        param.getParam().put("status", status);
        return ResponseMessage.ok(orderDetailService.showClientOrders(param, req));
    }

    //------------------------------------------------------------------------------------------------------------------

    /**
     * 微信或者余额支付  PayType = 0 微信支付，1 为余额支付
     *
     * @param orderDetail
     * @return
     */
    @RequestMapping(value = "ClientOrderBuy", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "goodsId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "购买")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage buy(OrderDetail orderDetail, HttpServletRequest request) {
        //多个商品支付
        if (orderDetail.getId() == null) {
            //统一使用t_order主订单ID支付
            //查询订单价格
            Order order = orderService.selectByPk(orderDetail.getOrderId());
            if(order.getOrderStatus() == 1) {
                return ResponseMessage.ok("该订单已支付");
            }
            BigDecimal bigDecimalTypePrice = order.getTotalPrice();
            //微信小程序支付商户订单号
            String out_trade_no = String.valueOf(orderDetail.getOrderId());
            //使用余额支付
                if (orderDetail.getPayType() != 0) {
                    return payByEarn(bigDecimalTypePrice, orderDetail);
                } else {
                    //微信支付
                    StringBuffer goodsName = new StringBuffer();
                    List<OrderDetail> list = orderDetailService.createQuery().where(OrderDetail.Property.orderId, orderDetail.getOrderId()).list();
                    if (list != null && list.size() > 0) {
                        goodsName.append(goodsService.selectByPk(list.get(0).getGoodsId()).getGoodsName());
                        if (list.size() > 1) {
                            goodsName.append("...共").append(list.size()).append("件商品");
                        }
                    }
                    return WxPayCommon.wxPay(out_trade_no, bigDecimalTypePrice, goodsName.toString(), request);
                }
            //单个商品
        } else {
            OrderDetail orderDetail1 = orderDetailService.selectByPk(orderDetail.getId());
            if(orderDetail1.getStatus() == 7) {
                return ResponseMessage.error("订单超时,请刷新");
            }
            if(orderDetail1.getStatus() != 0) {
                return ResponseMessage.error("该订单已支付");
            }
            BigDecimal bigDecimalTypePrice = orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum()));
            String out_trade_no = String.valueOf(orderDetail.getId());
            //使用余额支付
            if (orderDetail.getPayType() != 0) {
                return payByEarn(bigDecimalTypePrice, orderDetail);
            } else {
                //微信支付
                return WxPayCommon.wxPay(out_trade_no, bigDecimalTypePrice, goodsService.selectByPk(orderDetail.getGoodsId()).getGoodsName(), request);
            }

        }
    }

    /**
     * 余额支付
     * @param price
     * @param orderDetail
     * @return
     */
    @Transactional
    public ResponseMessage payByEarn(BigDecimal price, OrderDetail orderDetail){
        if (!tUserService.payByEarn(price)) {
            return ResponseMessage.error("余额不足");
        }
        return orderPayChangeStatus(orderDetail);
    }

    //------------------------------------------------------------------------------------------------------------------
    @RequestMapping(value = "updateClientOrderBuyStatus", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单ID",
                    required = true, dataType = "Long", paramType = "path")
    })
    @ApiOperation(value = "支付成功后修改订单状态")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage updateClientOrderBuyStatus(OrderDetail orderDetail) {
        return orderPayChangeStatus(orderDetail);
    }

    public ResponseMessage orderPayChangeStatus(OrderDetail orderDetail){
        orderDetail.setBuyTime(new Date());
        orderDetail.setStatus(1);
        orderDetail.setUserId(WebUtil.getLoginUser().getId());
        OrderProfit orderProfit = new OrderProfit();
        if (orderDetail.getId() != null) {
            //插入交易记录
            insertOrderProfit(orderProfit, orderDetail);
            //根据订单详情更新
            orderDetailService.update(orderDetail);
            schedulerUtil.removeJob(orderDetail.getId().toString(), SchedulerUtil.ORDER);
        } else {
            //根据订单id更新
            orderDetailService.updateStatusByOrderId(orderDetail);
            List<OrderDetail> list = orderDetailService.createQuery()
                    .where(OrderDetail.Property.orderId, orderDetail.getOrderId())
                    .and(OrderDetail.Property.userId, WebUtil.getLoginUser().getId()).list();
            Order order = new Order();
            order.setId(orderDetail.getOrderId());
            order.setOrderStatus(1);
            orderService.update(order);
            for (int i = 0; i < list.size(); i++) {
                insertOrderProfit(orderProfit, list.get(i));
                schedulerUtil.removeJob(list.get(i).getId().toString(), SchedulerUtil.ORDER);
            }
        }
        return ResponseMessage.ok("支付成功");
    }

    public void insertOrderProfit(OrderProfit orderProfit, OrderDetail orderDetail) {
        orderProfit.setId(GenericPo.createUID());
        if (orderDetail.getOrderId() != null) {
            orderProfit.setParentId(orderDetail.getOrderId());
        }
        orderProfit.setType(0);
        orderProfit.setUserId(orderDetail.getUserId());
        orderProfit.setChildId(orderDetail.getId());
        orderProfit.setGoodsId(orderDetail.getGoodsId());
        orderProfit.setCreateTime(new Date());
        orderProfit.setPrice(orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum())));
        orderProfitService.insert(orderProfit);
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
    public ResponseMessage cancelOrder(@PathVariable("orderId") Long orderId) {
        //恢复商品数量
        OrderDetail orderDetail = orderDetailService.selectByPk(orderId);
        goodsSpeService.updateGoodsSpecQuality(orderDetail.getGoodsSpecId(), orderDetail.getNum());
        orderDetail.setStatus(7);
        orderDetailService.update(orderDetail);
        return ResponseMessage.ok("已取消订单");
    }

    //------------------------------------------------------------------------------------------------------------------
    //状态，0:待付款， 1 待派单，2：待发货，3：待收货，4：待评价，5：已评价，6：退货，7：订单关闭
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
    public ResponseMessage refund(RefundExchange refundExchange) {
        //判断是否还能退款，取消退款，拒绝退款可以再次申请
        Long userId = WebUtil.getLoginUser().getId();
        RefundExchange repeatRefund = refundExchangeService.createQuery()
                .where(RefundExchange.Property.childOrderId, refundExchange.getChildOrderId())
                .and(RefundExchange.Property.userId, userId)
                .and(RefundExchange.Property.type, 0).single();

        if (repeatRefund != null && repeatRefund.getStatus() != 4) {
            return ResponseMessage.error("已申请退款");
        }
        //申请退款
        OrderDetail orderDetail = orderDetailService.selectByPk(refundExchange.getChildOrderId());
        if (refundExchange.getImageIdArray() != null && refundExchange.getImageIdArray().length > 0) {
            Long refId = fileRefService.addImageRefId(refundExchange.getImageIdArray(), 8);
            refundExchange.setApplyImageId(refId);
        }
        refundExchange.setParentOrderId(orderDetail.getOrderId());
        refundExchange.setUserId(userId);
        refundExchange.setType(0);
        refundExchange.setStatus(0);
        refundExchange.setPrice(orderDetail.getPrice());
        refundExchange.setShopId(orderDetail.getShopId());
        refundExchange.setApplicationTime(new Date());
        refundExchange.setId(GenericPo.createUID());

        //未发货直接退款
        if (orderDetail.getStatus() == 1 || orderDetail.getStatus() == 2) {
            if (orderDetail.getPayType() == 0) {
                //微信退款
                String refundCode = null;
                Long payKey;
                BigDecimal total_fee;
                if(orderDetail.getOrderId() == null){
                    OrderDetail orderDetail1 = orderDetailService.selectByPk(orderDetail.getId());
                    total_fee = orderDetail1.getPrice()
                            .multiply(new BigDecimal(orderDetail1.getNum()));
                }else{
                    total_fee = orderService.selectByPk(orderDetail.getOrderId()).getTotalPrice();
                }

                try {
                    if(orderDetail.getOrderId() != null){
                        payKey = orderDetail.getOrderId();
                    }
                    else{
                        payKey = orderDetail.getId();
                    }
                    refundCode = WXRefund.doRefund(payKey, refundExchange.getId(),
                            total_fee, orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum())));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!"OK".equals(refundCode)) {
                    return ResponseMessage.ok("退款失败");
                }
            } else {
                //余额退款
                tUserService.updateEarn(orderDetail.getPrice()
                        .multiply(new BigDecimal(orderDetail.getNum())), userId);
            }
            OrderProfit orderProfit = new OrderProfit();
            orderProfit.setType(1);
            orderProfit.setUserId(userId);
            orderProfit.setPrice(orderDetail.getPrice().multiply(new BigDecimal(orderDetail.getNum())));
            orderProfit.setGoodsId(orderDetail.getGoodsId());
            orderProfit.setChildId(orderDetail.getId());
            orderProfit.setParentId(orderDetail.getOrderId());
            orderProfit.setCreateTime(new Date());
            orderProfitService.insert(orderProfit);
            refundExchange.setStatus(3);
            goodsSpeService.updateGoodsSpecQuality(orderDetail.getGoodsSpecId(),orderDetail.getNum());
        }
        orderDetailService.changeStatus(refundExchange.getChildOrderId(), 6);
        refundExchangeService.addRefundExchange(refundExchange);
        if (orderDetail.getStatus() == 1 || orderDetail.getStatus() == 2) {
            schedulerUtil.removeJob(refundExchange.getChildOrderId().toString(), SchedulerUtil.ORDER);
        }else{
            schedulerUtil.pauseJob(refundExchange.getChildOrderId().toString(), SchedulerUtil.ORDER);
        }
        return ResponseMessage.ok("退款成功");
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
    public ResponseMessage confirmReceipt(@PathVariable("orderId") Long orderId) {
        schedulerUtil.removeJob(orderId.toString(), SchedulerUtil.ORDER);
        OrderDetail orderDetail = orderDetailService.selectByPk(orderId);
        orderDetail.setShouhuoTime(new Date());
        orderDetail.setStatus(4);
        orderDetailService.update(orderDetail);
        Map map = new HashMap<>();
        map.put("id", orderId);
        map.put("type", 0);
        //7天自动跳过评价
        schedulerUtil.addJob(orderId.toString(), SchedulerUtil.ORDER, map, EvaluationOrderJob.class,
                futureDate(5, DateBuilder.IntervalUnit.MINUTE));
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
    public ResponseMessage remindTime(OrderDetail orderDetail) {
        orderDetail.setRemindTime(new Date());
        return ResponseMessage.ok(orderDetailService.update(orderDetail));
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
    public ResponseMessage comment(@RequestBody GoodsComment goodsComment) {
        Long refId = fileRefService.addImageRefId(goodsComment.getFiles(), 6);
        Long userId = WebUtil.getLoginUser().getId();
        Date date = new Date();
        goodsComment.setOrderType(0);
        goodsComment.setImageId(refId);
        goodsComment.setUserId(userId);
        goodsComment.setCreateTime(date);
        goodsComment.setId(GenericPo.createUID());
        goodsCommentService.addGoodsComment(goodsComment);
        orderDetailService.changeStatus(goodsComment.getOrderId(), 5);
        //订单完成返现
        orderDetailService.orderProfit(goodsComment.getOrderId());
        schedulerUtil.removeJob(goodsComment.getOrderId().toString(), SchedulerUtil.ORDER);
        return ResponseMessage.ok("评价成功");
    }


}
