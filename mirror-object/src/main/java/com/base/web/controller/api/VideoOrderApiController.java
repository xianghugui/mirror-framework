package com.base.web.controller.api;

import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.GoodsCommentService;
import com.base.web.service.LogisticsService;
import com.base.web.service.ShopService;
import com.base.web.service.VideoOrderService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.AutomaticReceiptJob;
import com.base.web.util.CancelOrderJob;
import com.base.web.util.SchedulerUtil;
import io.swagger.annotations.*;
import org.quartz.DateBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.DateBuilder.futureDate;

@Api(value = "VideoOrderApiController", description="商家端询价管理")
@RequestMapping("/api/videoOrder/")
@RestController
public class VideoOrderApiController {

    @Resource
    private VideoOrderService videoOrderService;
    
    @Resource
    private ShopService shopService;

    @Resource
    private LogisticsService logisticsService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private SchedulerUtil schedulerUtil;

    @RequestMapping(value = "showOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示全部订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showOrders(QueryParam param, HttpServletRequest req){
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", null);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showOfferOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示待报价订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showOfferOrders(QueryParam param, HttpServletRequest req){
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 0);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showbuyOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示待购买订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showbuyOrders(QueryParam param, HttpServletRequest req){
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 1);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }
//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showDeliveryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示待发货订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showDeliveryOrders(QueryParam param, HttpServletRequest req){
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 2);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showTakeOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示待收货订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showTakeOrders(QueryParam param, HttpServletRequest req){
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 3);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showCompleteOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引", required = true,
                    dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value="显示完成订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0,待报价.1待付款，2待发货，3待收货，4退货，5订单完成，6取消订单，7缺货<br>" +
            "userName：收货人名称<br>" +
            "videoSrc：视频资源路径<br>" +
            "videoImageSrc:视频图片资源路径｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showCompleteOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 5);
        return ResponseMessage.ok(videoOrderService.showVideoOrders(param, req));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "offer", method = RequestMethod.POST)
    @ApiOperation(value="店铺报价")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoOrderId", value = "询价单ID", required = true,
                    dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "price", value = "报价金额",
                    required = true, dataType = "int", paramType = "form"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @Transactional
    public ResponseMessage offer(VideoOrder videoOrder){
        if (videoOrder == null){
            return ResponseMessage.error("参数为空");
        }
        videoOrder.setUpdateTime(new Date());

        //TODO 提醒用户已报价？
        videoOrder.setStatus(1);
        videoOrder.setUpdateUserId(WebUtil.getLoginUser().getId());
        videoOrderService.update(videoOrder);
        //两天后自动取消订单
        Map map = new HashMap();
        map.put("id", videoOrder.getId());
        map.put("type", 2);
        schedulerUtil.addJob(String.valueOf(videoOrder.getId()), SchedulerUtil.VIDEOORDER,
                map, CancelOrderJob.class, futureDate(5, DateBuilder.IntervalUnit.MINUTE));
        return ResponseMessage.ok("已报价");
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{videoOrderId}/lack", method = RequestMethod.POST)
    @ApiOperation(value="店铺缺货")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "videoOrderId", value = "询价单ID", required = true,
                    dataType = "int", paramType = "path"),
            @ApiImplicitParam(name = "lackReason", value = "缺货原因",
                    required = true, dataType = "String", paramType = "form"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage lack(@PathVariable("videoOrderId") String videoOrderId, @RequestParam("lackReason")String lackReason){
        if (videoOrderId == null || "".equals(videoOrderId)){
            return ResponseMessage.error("参数为空");
        }
        if (lackReason == null || "".equals(lackReason)){
            return ResponseMessage.error("参数为空");
        }
        return ResponseMessage.ok(videoOrderService.lack(videoOrderId, lackReason));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{videoOrderId}/showOrderInfo", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name="videoOrderId",value="视频订单号",required=true,paramType="path")})
    @ApiOperation(value="显示订单详情", notes = "返回data为List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "createTime：创建时间<br>" +
            "videoImageSrc：视频图片资源路径<br>" +
            "videoSrc：视频资源路径<br>" +
            "goodsPrice： 商品价格<br>" +
            "goodsName： 商品名称<br>" +
            "goodsColor：商品颜色<br>" +
            "goodsSize：商品尺寸<br>" +
            "goodsNumber： 商品数量<br>" +
            "userName：收货人名称<br>" +
            "userPhone：收货人电话<br>" +
            "userAddress：收货地址<br>" +
            "status：状态（0店家发货中，1待收货，2确认收货，3退货,4订单完成）<br>" +
            "expressName:快递名称<br>" +
            "expressNumber:运单号｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showOrderInfo(@PathVariable("videoOrderId") String videoOrderId, HttpServletRequest req){
        Map map = videoOrderService.showVideoOrderInfo(videoOrderId);
        if(map != null && map.size() > 0) {
            QueryParam param = new QueryParam();
            //查询视频和视频图片
            param.getParam().put("type", 1);
            param.getParam().put("recordId", map.get("videoSrc"));
            List<Map> imgs = fileRefService.queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                for (int j = 0; j < imgs.size(); j++) {
                    if (imgs.get(j).get("type").toString().equals("0")) {
                        map.put("imageSrc", imgs.get(j).get("resourceUrl"));
                    } else {
                        map.put("videoSrc", imgs.get(j).get("resourceUrl"));
                    }
                }
            }
            return ResponseMessage.ok(map);
        }
        return ResponseMessage.error("没有数据");
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{videoOrderId}/showAddress", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name="videoOrderId",value="视频订单号",required=true,paramType="path")})
    @ApiOperation(value="发货显示收货地址", notes = "返回data为List&lt;Map&gt;｛<br>" +
            "videoOrderId：视频订单ID<br>" +
            "userName：收货人名称<br>" +
            "userPhone：收货人电话<br>" +
            "userAddress：收货地址<br>｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showAddress(@PathVariable("videoOrderId") String videoOrderId){
        return ResponseMessage.ok(videoOrderService.showAddress(videoOrderId));
    }

//--------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", required = true, paramType = "path"),
            @ApiImplicitParam(name = "expressName", value = "发货公司", required = true),
            @ApiImplicitParam(name = "expressNumber", value = "运单号", required = true),

    })
    @ApiOperation(value="上传快递信息")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @Transactional
    public ResponseMessage delivery(VideoOrder videoOrder){
        videoOrder.setExpressId(videoOrder.getExpressId());
        videoOrder.setTheDeliveryTime(new Date());
        videoOrder.setStatus(3);
        videoOrderService.update(videoOrder);
        Map map = new HashMap();
        map.put("id",videoOrder.getId());
        map.put("type", 2);
        //7天后自动收货
        schedulerUtil.addJob(String.valueOf(videoOrder.getId()),SchedulerUtil.VIDEOORDER,
                map, AutomaticReceiptJob.class,futureDate(5, DateBuilder.IntervalUnit.MINUTE));
        return ResponseMessage.ok("发货成功");
    }

    //--------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{orderId}/showComment", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", required = true, paramType = "path")
    })
    @ApiOperation(value="显示评论", notes = "返回dataMap｛<br>" +
            "commentId：评论ID<br>" +
            "userName：评论人<br>" +
            "createTime：评论时间<br>" +
            "goodsColor：商品颜色<br>｝"+
            "goodsSize：商品尺寸<br>｝"+
            "goodsNum：商品数量<br>｝"+
            "goodsName：商品名称<br>｝"+
            "imageSrc：评论图片List<br>｝"+
            "content：评论内容<br>｝")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    public ResponseMessage showComment(@PathVariable("orderId") String orderId, HttpServletRequest req){
        Map map = goodsCommentService.queryGoodsCommentByOrderId(req, orderId, "2");
        return ResponseMessage.ok(map);
    }

    //----------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "showTrading", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "我的->钱包->交易中->显示交易中订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>" +
            "List&lt;Map&gt;｛<br>" +
            "orderId：订单ID<br>" +
            "goodsName：商品名称<br>" +
            "createTime：创建时间<br>" +
            "price：价格<br>" +
            "status：状态（0店家发货中，1待收货，2确认收货，3退货,4订单完成）<br>")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showTrading(QueryParam param) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        return ResponseMessage.ok(videoOrderService.showTrading(param));
    }

    //----------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "insertVideoOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage insertVideoOrder(VideoOrder videoOrder) {
        videoOrder.setStatus(0);
        videoOrder.setNum(1);
        videoOrder.setUserId(WebUtil.getLoginUser().getId());
        videoOrder.setId(GenericPo.createUID());
        videoOrder.setCreateTime(new Date());
        return ResponseMessage.ok(videoOrderService.insert(videoOrder));
    }


    @RequestMapping(value = "queryLogistics", method = RequestMethod.GET)
    @ApiOperation(value = "发货--快递查询接口", notes = "返回数据：" +
            "id: 主键 Long <br>" +
            "name：快递名<br>")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage queryLogistics() {
        return ResponseMessage.ok(logisticsService.select());
    }

}
