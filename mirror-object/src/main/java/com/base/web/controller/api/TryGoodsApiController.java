package com.base.web.controller.api;

import com.base.web.bean.TryInfo;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.GoodsCommentService;
import com.base.web.service.LogisticsService;
import com.base.web.service.ShopService;
import com.base.web.service.TryInfoService;
import com.base.web.service.resource.FileRefService;
import io.swagger.annotations.*;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:53  2018/4/16
 */
@Api(value = "GoodsTryApiController", description = "商家端试穿管理")
@RequestMapping("/api/trygoods/")
@RestController
public class TryGoodsApiController {

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private ShopService shopService;

    @Resource
    private LogisticsService logisticsService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private FileRefService fileRefService;

    @RequestMapping(value = "showTryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示全部订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>" +
            "userName：收货人名称<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", null);
        return ResponseMessage.ok(tryInfoService.showTryOrders(param, req));
    }

    @RequestMapping(value = "showDeliveryTryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示待发货订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>" +
            "userName：收货人名称<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showDeliveryOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 1);
        return ResponseMessage.ok(tryInfoService.showTryOrders(param, req));
    }

    @RequestMapping(value = "showTakeTryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示试衣中（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>" +
            "userName：收货人名称<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showTakeOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 2);
        return ResponseMessage.ok(tryInfoService.showTryOrders(param, req));
    }

    @RequestMapping(value = "showCompleteTryOrders", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "数据查询起始索引",
                    required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "一页查询几条数据",
                    required = true, dataType = "int", paramType = "query"),
    })
    @ApiOperation(value = "显示完成订单（后台根据当前登录商家查询）", notes = "返回data为total：数据总数<br>List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "createTime：创建时间<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>" +
            "userName：收货人名称<br>" +
            "imageSrc：图片资源路径｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showCompleteOrders(QueryParam param, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        Long shopId = shopService.queryShopIdByUserId(userId);
        param.getParam().put("shopId", shopId);
        param.getParam().put("status", 7);
        return ResponseMessage.ok(tryInfoService.showTryOrders(param, req));
    }

    @RequestMapping(value = "{tryInfoId}/showTryOrderInfo", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name = "tryInfoId", value = "试衣订单号", required = true, paramType = "path")})
    @ApiOperation(value = "显示订单详情", notes = "返回data为List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "createTime：创建时间<br>" +
            "imageSrc：图片资源路径<br>" +
            "goodsPrice： 商品价格<br>" +
            "goodsName： 商品名称<br>" +
            "goodsColor：商品颜色<br>" +
            "goodsSize：商品尺寸<br>" +
            "goodsNumber： 商品数量<br>" +
            "userName：收货人名称<br>" +
            "userPhone：收货人电话<br>" +
            "userAddress：收货地址<br>" +
            "status：状态，0待派单，1待发货，2试衣中，3购买，4退回，5商家确认退回，6待评价,7完成订单，8取消订单<br>" +
            "expressName:快递名称<br>" +
            "expressNumber:运单号｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showOrderInfo(@PathVariable("tryInfoId") String tryInfoId, HttpServletRequest req) {
        Map map = tryInfoService.showTryOrderInfo(tryInfoId);
        if (map == null || map.isEmpty()) {
            return ResponseMessage.error("没有数据");
        }
        List<Map> list = new ArrayList();
        list.add(map);
        return ResponseMessage.ok(fileRefService.addImages(list,2, req).get(0));
    }

    @RequestMapping(value = "{tryInfoId}/showAddress", method = RequestMethod.GET)
    @ApiImplicitParams({@ApiImplicitParam(name = "tryInfoId", value = "试衣订单号", required = true, paramType = "path")})
    @ApiOperation(value = "发货显示收货地址", notes = "返回data为List&lt;Map&gt;｛<br>" +
            "tryInfoId：试衣订单ID<br>" +
            "userName：收货人名称<br>" +
            "userPhone：收货人电话<br>" +
            "userAddress：收货地址<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showAddress(@PathVariable("tryInfoId") String tryInfoId) {
        return ResponseMessage.ok(tryInfoService.showAddress(tryInfoId));
    }

//------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "delivery", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tryInfoId", value = "订单号", required = true, paramType = "form"),
            @ApiImplicitParam(name = "expressName", value = "发货公司", required = true,dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "expressNumber", value = "运单号", required = true,dataType = "String", paramType = "form"),
    })
    @ApiOperation(value = "上传快递信息")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage delivery(TryInfo tryInfo) {
        String expressId = logisticsService.insertLogistics(tryInfo.getExpressName()).toString();
        tryInfo.setStatus(2);
        tryInfo.setExpressId(Integer.valueOf(expressId));
        tryInfo.setFahuoTime(new Date());
        return ResponseMessage.ok(tryInfoService.update(tryInfo));
    }

//--------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "{orderId}/showComment", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "orderId", value = "订单号", required = true, paramType = "path")
    })
    @ApiOperation(value = "显示评论", notes = "返回dataMap｛<br>" +
            "commentId：评论ID<br>" +
            "userName：评论人<br>" +
            "createTime：评论时间<br>" +
            "goodsColor：商品颜色<br>｝" +
            "goodsSize：商品尺寸<br>｝" +
            "goodsNum：商品数量<br>｝" +
            "goodsName：商品名称<br>｝" +
            "imageSrc：评论图片List<br>｝" +
            "content：评论内容<br>｝")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage showComment(@PathVariable("orderId") String orderId, HttpServletRequest req) {
        Map map = goodsCommentService.queryGoodsCommentByOrderId(req, orderId, "1");
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
        return ResponseMessage.ok(tryInfoService.showTrading(param));
    }
}
