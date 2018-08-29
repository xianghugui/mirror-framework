package com.base.web.controller.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.base.web.bean.*;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.bean.po.user.User;
import com.base.web.controller.wxPay.WxPayCommon;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.session.HttpSessionManager;
import com.base.web.core.utils.WebUtil;
import com.base.web.push.JiguangPush;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import io.swagger.annotations.*;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.codec.binary.Base64;
import org.apache.ibatis.annotations.Param;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.hsweb.expands.request.SimpleRequestBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;

import static com.base.web.core.message.ResponseMessage.ok;

@Api(value = "UserApiController", description = "客户端用户主页操作类相关接口")
@RequestMapping("/api/user/")
@RestController

public class UserApiController {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private TUserService tUserService;
    @Resource
    private UserShareService userShareService;

    @Resource
    private UserAgentService userAgentService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private GetMoneyService getMoneyService;

    @Resource
    private GoodsTryService goodsTryService;

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private OrderProfitService orderProfitService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private UserFeatureService userFeatureService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private ShopGoodsPushService shopGoodsPushService;

    @Resource
    private ShopDeviceService shopDeviceService;

    @Resource
    private ShopAddGoodsService shopAddGoodsService;

    @Resource
    private PropertyService propertyService;

    @Resource
    private TryTimeRecordService tryTimeRecordService;

    @Resource
    private ShopService shopService;

    /**
     * httpSession管理器
     */
    @Autowired
    private HttpSessionManager httpSessionManager;


    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "获取用户主页基础数据（余额，代言数，试穿数量，openid等）", notes = "用户登录个人主页，从服务端返回个人基本信息" +
            "返回map类型数据" +
            "<br>{" +
            "<br>Long userId 当前用户id ;" +
            "<br>Long roleId 当前用户角色id(0表示普通用户，1，表示代理，其他表示其他类型用户) ;" +
            "<br>String userName 用户名 ;" +
            "<br>String avatar 用户头像完成地址 ;" +
            "<br>BigDecimal earn 账户余额 ;" +
            "<br>int agentNum 代言商品数量 ;" +
            "<br>int tryNum 平台总试穿次数 ;" +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "home", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage home() {
        Map map = tUserService.userHome(WebUtil.getLoginUser().getId());
        map.put("userId", WebUtil.getLoginUser().getId());
        return ok(map);
    }

    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "分页获取用户代理商品信息", notes = "根据代言状态获取数据 分页数据" +
            "返回分页数据" +
            "<br>{" +
            "<br>Long goodsId 商品id ;" +
            "<br>Integer goodsStatus 商品状态（ 商品状态 默认1 在售，0 下架，2售罄） ;" +
            "<br>String goodsName 商品名称 ;" +
            "<br>Long refImageId 商品图片关联地址（只返回一张商品图片） ;" +
            "<br>BigDecimal price 商品价格 ;" +
            "<br>Date dealTime 代理时间 ;" +
            "<br>int pageIndex 数据查询起始索引（必须上传） ;" +
            "<br>int pageSize 一页查询几条数据 ;" +
            "<br>String goodsImageUrl 商品图片地址 ;" +
            "<br>Integer status 代言商品状态  代理商品状态（0，代卖，1，取消代卖,2删除下架代理商品（假删除）） ;" +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "代理状态（0正在代理，1取消代理）", required = true),
                    @ApiImplicitParam(paramType = "int", dataType = "int", name = "pageIndex", value = "数据查询起始索引", required = true),
                    @ApiImplicitParam(paramType = "int", dataType = "int", name = "pageSize", value = "一页查询几条数据", required = true)})
    @RequestMapping(value = "userAgentGoodsList/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userAgentGoodsList(@PathVariable("status") Integer status, QueryParam queryParam, HttpServletRequest req) {
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        queryParam.getParam().put("status", status);
        return ok(userAgentService.userAgentGoodsListPagerByUserId(queryParam, req));
    }

    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "用户(上)下架，删除，自己代理商品", notes = "用户根据商品id，操作状态（上）下架，删除，相应的代理商品")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "下架商品id", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "status", value = "操作状态（0下架商品再次上架，1表示下架，2删除）", required = true)
            })
    @RequestMapping(value = "userAgentGoodsUpdate/{goodsId}/{status}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userAgentGoodsUpdate(@PathVariable("goodsId") Long goodsId, @PathVariable("status") Integer status) {
        UserAgent userAgent = userAgentService.createQuery()
                .where(UserAgent.Property.goodsId, goodsId).and()
                .where(UserAgent.Property.userId, WebUtil.getLoginUser().getId())
                .single();
        if (userAgent != null) {
            userAgent.setStatus(status);
            userAgentService.update(userAgent);
        } else {
            return ResponseMessage.error("参数不正确，请检查上传数据是否符合要求");
        }
        return ResponseMessage.ok();
    }


    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "用户根据商品id推广代理商品", notes = "用户根据商品id推广代理商品 " +
            "<br>Long goodsId 商品id ;" +
            "<br>String brandName 品牌名称 ;" +
            "<br>Long carouselId 轮播id ;" +
            "<br>String goodsDescribe 商品详情 ;" +
            "<br>String goodsClassName 商品类别 ;" +
            "<br>String goodsName 商品名称 ;" +
            "<br>Long goodsImageId 商品图片id ;" +
            "<br>Integer goodsStock 商品库存 ;" +
            "<br>Integer goodsStatus  商品状态 默认1 在售，0 下架，2售罄 ;" +
            "<br>List<Map> goodsImageList 商品图片地址列表 ;" +
            "<br>List<Map> goodsCarouseList 商品轮播地址列表 ;" +
            "<br>BigDecimal price 商品价格 ;")

    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "推广商品id", required = true)})
    @RequestMapping(value = "userAgentGoodsSpread/{goodsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userAgentGoodsSpread(@PathVariable("goodsId") Long goodsId, QueryParam queryParam, HttpServletRequest req) {
        Object data = userAgentService.userAgentGoodsSpread(goodsId, queryParam, req);
        return ResponseMessage.ok(data);
    }


    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "用户根据商品id推广代理商品", notes = "用户根据商品id推广代理商品 " +
            "<br>Long goodsId 商品id ;" +
            "<br>String brandName 品牌名称 ;" +
            "<br>Long carouselId 轮播id ;" +
            "<br>String goodsDescribe 商品详情 ;" +
            "<br>String goodsClassName 商品类别 ;" +
            "<br>String goodsName 商品名称 ;" +
            "<br>Long goodsImageId 商品图片id ;" +
            "<br>Integer goodsStock 商品库存 ;" +
            "<br>Integer goodsStatus  商品状态 默认1 在售，0 下架，2售罄 ;" +
            "<br>List<Map> goodsImageList 商品图片地址列表 ;" +
            "<br>List<Map> goodsCarouseList 商品轮播地址列表 ;" +
            "<br>BigDecimal price 商品价格 ;")

    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "推广商品id", required = true)})
    @RequestMapping(value = "userShareProfit/{queryType}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userShareProfit(@PathVariable("queryType") Integer queryType, QueryParam queryParam, HttpServletRequest req) {

        return ResponseMessage.ok();
    }


    // ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "(分页)用户评论查询接口", notes = "用户评论查询接口" +
            "<br>star: 评论星级" +
            "<br>color: 商品颜色" +
            "<br>size: 商品尺寸" +
            "<br>createTime: 评论时间" +
            "<br>price: 商品价格" +
            "<br>goodsName: 商品名称" +
            "<br>content: 评论内容" +
            "<br>total: 分页总数" +
            "<br>goodsImagePath: 商品图片路径" +
            "<br>imagePath: 评论图片路径")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
            })
    @RequestMapping(value = "userComment", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userComment(QueryParam queryParam, HttpServletRequest req) {
        queryParam.getParam().put("userId", WebUtil.getLoginUser().getId());
        PagerResult<Map> userCommentList = goodsCommentService.queryCommentByUserId(queryParam);
        List<Map> imageList;
        List<Map> goodsImage;
        for (Map userComment : userCommentList.getData()) {

            // >>用户评论图片路径
            queryParam.getParam().put("dataType", 6);
            queryParam.getParam().put("recordId", userComment.get("imageId"));
            imageList = fileRefService.queryResourceByRecordId(queryParam, req);
            if (imageList != null && imageList.size() > 0) {
                userComment.put("imagePath", imageList);
            }

            // >>商品图片路径
            queryParam.getParam().put("dataType", 2);
            queryParam.getParam().put("recordId", userComment.get("goodsImageId"));
            goodsImage = fileRefService.queryResourceByRecordId(queryParam, req);
            if (goodsImage != null && goodsImage.size() > 0) {
                userComment.put("goodsImagePath", goodsImage.get(0));
            }
        }
        return ResponseMessage.ok(userCommentList);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = " 邀请用户查询接口", notes = "邀请用户查询接口" +
            "<br>createTime: 邀请时间" +
            "<br>name: 邀请用户名" +
            "<br>avatar: 用户头像")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "userRequest", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userRequest() {
        List<Map> userList = tUserService.queryUserByPId(WebUtil.getLoginUser().getId());
        return ResponseMessage.ok(userList);
    }


    //------------------------------------------------------------------------------------------------------------------------------
    @ApiOperation(value = " 用户余额查询接口", notes = "用户余额查询接口" +
            "<br>earn:用户余额")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "userEarn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userEarn() {
        Long uId = WebUtil.getLoginUser().getId();
        return ResponseMessage.ok(tUserService.queryEarn(uId));
    }

    //------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/insertGetMoney/{money}/{bank}", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "提现记录生成接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "money", value = "提现金额", dataType = "BigDecimal", paramType = "path"),
            @ApiImplicitParam(name = "bank", value = "银行", dataType = "String", paramType = "path")
    })
    @Transactional
    public ResponseMessage insertGetMoney(@PathVariable("money") BigDecimal money,
                                          @PathVariable("bank") String bank) {
        if (getMoneyService.insertGetMoney(money, bank) == 0) {
            return ResponseMessage.error("余额不足以提现");
        }
        return ResponseMessage.ok();
    }
    //------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "提现明细查询接口", notes = "查询List<>的值:" +
            "<br>money: 提现金额" +
            "<br>bank: 提现银行" +
            "<br>status: 提现状态（1 提现中，2完成提现）" +
            "<br>createTime:提现时间")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
    })
    @RequestMapping(value = "/queryGetMoney", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGetMoney(QueryParam queryParam) {
        return ResponseMessage.ok(getMoneyService.queryGetMoney(queryParam));
    }
    //---------------------------------------------------------------------------------------

    @ApiOperation(value = "查询用户试穿记录接口", notes = "查询List<>的值:" +
            "<br>goodsId: 商品ID Long" +
            "<br>goodsSpecId: 规格ID Long" +
            "<br>num: 商品数量 Integer" +
            "<br>addressId : 收货地址ID Long" +
            "<br>shoppingStatus :判断是否是在购物车中（0 否，1 是）Integer")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
    })
    @RequestMapping(value = "/queryGoodsTry", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoodsTry(QueryParam queryParam) {
        return ResponseMessage.ok(goodsTryService.queryGoodsTry(queryParam));
    }


    //---------------------------------------------------------------------------------------

    @ApiOperation(value = "查询用户试穿详情接口", notes = "查询List<>的值:" +
            "<br>goodsId: 商品ID Long" +
            "<br>goodsSpecId: 规格ID Long" +
            "<br>num: 商品数量 Integer")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "tryId", value = "试穿订单ID", required = true)
    })
    @RequestMapping(value = "/queryTryInfo/{tryId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryTryInfo(@PathVariable("tryId") Long tryId) {
        return ResponseMessage.ok(tryInfoService.queryTryByTryId(tryId));
    }

    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "查询分润接口", notes = "需传入对象List<>的值:" +
            "<br>orderUserId: 购买者ID" +
            "<br>money:  所得金额" +
            "<br>createTime: 分润时间" +
            "<br>goodsName: 商品名称")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "type", value = "查询类型:0:试衣购买，1：平台购买", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
    })
    @RequestMapping(value = "/queryOrderProfit/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryOrderProfit(@PathVariable("type") Integer type, QueryParam queryParam) {
        return ResponseMessage.ok(orderProfitService.queryOrderProfit(type, queryParam));
    }

    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "查询邀请用户分润详情", notes = "")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Long", name = "orderUserId", value = "邀请用户ID", required = true),
    })
    @RequestMapping(value = "queryOrderProfitListByUserId/{orderUserId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryOrderProfitListByUserId(@PathVariable("orderUserId") Long orderUserId) {
        if (orderUserId == null) {
            return ResponseMessage.ok("暂无数据");
        }
        List<OrderProfit> list = orderProfitService.queryOrderProfitListByUserId(WebUtil.getLoginUser().getId(), orderUserId);
        if (list == null) {
            return ResponseMessage.ok(new ArrayList<>());
        }
        return ResponseMessage.ok(list);
    }

    //-----------------------------------------------------------------------------------------
    @ApiOperation(value = "用户消费查询接口", notes = "查询对象List<>的值:" +
            "<br>createTime: 消费时间" +
            "<br>price:  消费金额" +
            "<br>num: 购买数量" +
            "<br>goodsName: 商品名称")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "type", value = "查询类型:0:订单消费，1：试穿消费", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
    })
    @RequestMapping(value = "/queryUserConsume/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryUserConsume(@PathVariable("type") Integer type, QueryParam queryParam) {
        PagerResult<Map> consumeList = null;
        if (type == 0) {
            consumeList = orderDetailService.queryOrderConsume(queryParam);
        }
        if (type == 1) {
            consumeList = tryInfoService.queryTryInfo(queryParam);
        }
        return ResponseMessage.ok(consumeList);
    }


    //------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/insertAddress", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "新增收货地址接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage insertAddress(@RequestBody UserAddress userAddress) {
        userAddress.setUserId(WebUtil.getLoginUser().getId());
        userAddress.setId(GenericPo.createUID());
        if (userAddress.getStatus() == 1) {
            userAddressService.updateStatus(userAddress.getUserId());
        }
        return ResponseMessage.ok(userAddressService.insert(userAddress));
    }

    //------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/updateAddress/{addressId}", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "编辑收货地址接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "addressId", value = "更改地址ID", dataType = "Long", paramType = "path"),
    })
    @Transactional
    public ResponseMessage updateAddress(@RequestBody UserAddress userAddress, @PathVariable("addressId") Long addressId) {
        userAddress.setUserId(WebUtil.getLoginUser().getId());
        userAddress.setId(addressId);
        if (userAddress.getStatus() == 1) {
            userAddressService.updateStatus(userAddress.getUserId());
        }
        return ResponseMessage.ok(userAddressService.update(userAddress));
    }

    //------------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "删除收货地址接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "addressId", value = "要删除的地址ID", required = true)
            })
    @RequestMapping(value = "deleteAddress/{addressId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public ResponseMessage deleteAddress(@PathVariable("addressId") Long addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setStatus(2);
        return ResponseMessage.ok(userAddressService.update(userAddress));
    }

    //------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/insertUserFeature", method = RequestMethod.POST)
    @ApiOperation(value = "录入身材信息接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage insertUserFeature(UserFeature userFeature) {
        TUser tUser = tUserService.selectByPk(WebUtil.getLoginUser().getId());
        if (tUser.getCardId() == null) {
            tUser.setCardId(GenericPo.createUID());
        }
        FileRef fileRef = new FileRef();
        fileRef.setDataType(9);
        fileRef.setRefId(tUser.getCardId());
        for (int i = 0; i < userFeature.getImageId().length; i++) {
            fileRef.setId(GenericPo.createUID());
            fileRef.setResourceId(userFeature.getImageId()[i]);
            fileRefService.insert(fileRef);
        }
        tUserService.update(tUser);
        //插入身材信息
        userFeature.setUserId(WebUtil.getLoginUser().getId());
        return ResponseMessage.ok(userFeatureService.insert(userFeature));
    }


    /**
     * 判断资源类型
     *
     * @param fileName
     * @return
     */
    private static String getMimeType(String fileName) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileName);
        if (type == null) {
            type = "";
        } else {
            type = "image";
        }
        return type;
    }

    //------------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/updateUserFeature", method = RequestMethod.POST)
    @ApiOperation(value = "编辑身材信息接口" +
            "")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage updateUserFeature(UserFeature userFeature) {
        TUser tUser = tUserService.selectByPk(WebUtil.getLoginUser().getId());
        if (tUser.getCardId() == null) {
            tUser.setCardId(GenericPo.createUID());
            tUserService.update(tUser);
        }
        List<FileRef> list = fileRefService.createQuery().where(FileRef.Property.refId, tUser.getCardId())
                .and(FileRef.Property.dataType, 9).list();
        //更新图片
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setResourceId(userFeature.getImageId()[i]);
            fileRefService.update(list.get(i));
        }

        return ResponseMessage.ok(userFeatureService.update(userFeature));
    }


    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "用户身材信息查询接口", notes = "查询List<>的值:" +
            "uId：身材信息ID" +
            "chest：胸围" +
            "cardId：身份证ID" +
            "weight：体重" +
            "waist：腰围" +
            "hip：臀围" +
            "height：身高")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryUserFeature", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryUserFeature(HttpServletRequest req) {
        QueryParam queryParam = new QueryParam();
        Map userFeature = userFeatureService.queryUserFeature();
        if (userFeature == null) {
            return ResponseMessage.error("没有数据");
        }
        queryParam.getParam().put("dataType", 9);
        queryParam.getParam().put("recordId", userFeature.get("cardId"));
        List<Map> cardImage = fileRefService.queryResourceByRecordId(queryParam, req);
        if (cardImage != null && cardImage.size() > 0) {
            userFeature.put("cardImagePath", cardImage);
        }
        return ResponseMessage.ok(userFeature);
    }

//--------------------------------------------------------------------------------------

    @ApiOperation(value = "商家商品推荐页信息显示", notes = "info:<br>" +
            ":{<br>" +
            "ShopGoodsPushId：id<br>" +
            "userId：用户ID<br>" +
            "height：身高<br>" +
            "weight：体重<br>" +
            "sex：性别<br>" +
            "age：年龄<br>" +
            "like：感兴趣的类别<br>" +
            "phone：手机<br>" +
            "}<br>" +
            "classList:<br>" +
            ":{<br>" +
            "goodsClassId:类别ID<br>" +
            "className:类别名称<br>" +
            "}<br>")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "deviceUserName", value = "设备用户名", required = true),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryShopGoodsPushInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryShopGoodsPushInfo(@Param("deviceUserName") String deviceUserName, HttpServletRequest req) {
        //根据设备ID获取店铺ID
        Device device = deviceService.createQuery().where(Device.Property.username, deviceUserName).single();
        ShopDevice shopDevice = shopDeviceService.createQuery().where(ShopDevice.Property.deviceId, device.getId()).single();
        if (device == null) {
            return ResponseMessage.error("没有查询到此设备");
        }
//        //根据用户ID，店铺ID，查询推送表信息
        Map shopGoodsPush = shopGoodsPushService.selectOrderByTimePickOne(WebUtil.getLoginUser().getId(), shopDevice.getShopId());
        //根据店铺ID查询出店铺下的服装类别信息
        List<Map> classList = shopAddGoodsService.selectClassIdByShopId(shopDevice.getShopId());
        //年龄段列表 1：年龄段，2：适合场合
        List<Map> ageList = propertyService.selectByType(1);
        //根据店铺ID查询出店铺下的适用场合信息
        List<Map> applicationList = shopAddGoodsService.selectApplicationIdByShopId(shopDevice.getShopId());

        //根据店铺ID查询出店铺图片
        Shop shop = shopService.selectByPk(shopDevice.getShopId());
        QueryParam param = new QueryParam();
        param.getParam().put("dataType", 5);
        param.getParam().put("recordId", shop.getBusinessId());
        List<Map> shopImage = fileRefService.queryResourceByRecordId(param, req);
        ArrayList shopImageList = new ArrayList();
        for (Map map : shopImage) {
            shopImageList.add(map.get("resourceUrl"));
        }


        Map map = new HashMap();
        map.put("classList", classList);
        map.put("info", shopGoodsPush);
        map.put("age", ageList);
        map.put("application", applicationList);
        map.put("deviceId", device.getId());
        map.put("shopImage", shopImageList);
        return ResponseMessage.ok(map);
    }


    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "商家商品推荐页信息更新/添加")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "id", value = "设备id", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "height", value = "身高", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "weight", value = "体重", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "sex", value = "性别", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "age", value = "年龄", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "phone", value = "手机", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "like", value = "感兴趣的类别数组", required = true),
            @ApiImplicitParam(paramType = "form", dataType = "String", name = "application", value = "适应场合数组", required = true),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "{deviceId}/updateShopGoodsPushInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateShopGoodsPushInfo(@PathVariable("deviceId") Long deviceId, ShopGoodsPush shopGoodsPush,
                                                   HttpServletRequest req) {
        //当前登录用户ID
        Long loginUserId = WebUtil.getLoginUser().getId();
        //根据设备ID获取店铺ID
        Device device = deviceService.selectByPk(deviceId);
        if (device == null) {
            return ResponseMessage.error("没有查询到此设备");
        }

        ShopDevice shopDevice = shopDeviceService.createQuery().where(ShopDevice.Property.deviceId, device.getId()).single();
        shopGoodsPush.setShopId(shopDevice.getShopId());
        List<Map> list = shopAddGoodsService.selectByShopGoodsPush(shopGoodsPush);
        String goodsId = "";//拼接当前查询条件结果商品ID串

        ArrayList goodsIdList = new ArrayList();
        if (list.size() > 0) {
            for (Map map : list) {
                String currentGoodsId = map.get("goodsId").toString();
                goodsId += currentGoodsId + ",";
                goodsIdList.add(Long.valueOf(map.get("goodsId").toString()));
            }
        }
        shopGoodsPush.setUserId(loginUserId);
        shopGoodsPush.setGoodsId(goodsId);
        shopGoodsPush.setCreateTime(new Date());

        ShopGoodsPush update = shopGoodsPushService.createQuery()
                .where(ShopGoodsPush.Property.shopId, shopDevice.getShopId())
                .and(ShopGoodsPush.Property.userId, loginUserId)
                .single();


        if (update != null) {
            System.out.println("update");
            shopGoodsPush.setId(update.getId());
            shopGoodsPushService.update(shopGoodsPush);
        } else {
            System.out.println("insert");
            shopGoodsPush.setId(GenericPo.createUID());
            shopGoodsPushService.insert(shopGoodsPush);
        }


        //设备表username
        JiguangPush.push(device.getUsername(), JSON.toJSONString(goodsIdList, SerializerFeature.DisableCircularReferenceDetect));
        if (goodsId == "") {
            return ResponseMessage.ok("没有商品");
        }
        return ResponseMessage.ok("提交成功");
    }


    @ApiOperation(value = "商家商品查询", notes = "商家商品查询List<Map>中Map数据:" +
            "<br>goodsId : 主键 Long" +
            "<br>goodsName : 商品名 String" +
            "<br>price : 商品价格 String" +
            "<br>imageSrc : 图片链接", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "queryShopGoodsInfo", method = RequestMethod.POST)
    public ResponseMessage queryShopGoodsInfo(@RequestParam("goodsIdList") String goodsIdList, HttpServletRequest req) {
        List<Long> goodsList = JSON.parseArray(goodsIdList, Long.class);

        List<Map> list = new ArrayList<Map>();

        ShopAddGoods goods;
        QueryParam param = new QueryParam();
        param.getParam().put("dataType", 10);
        for (int i = 0; i < goodsList.size(); i++) {
            goods = shopAddGoodsService.selectByPk(goodsList.get(i));
            if (goods != null) {
                Map map = new HashMap();
                param.getParam().put("recordId", goods.getImageId());
                map.put("goodsName", goods.getName());
                map.put("price", String.valueOf(goods.getPrice()));
                map.put("goodsId", String.valueOf(goods.getId()));
                list.add(i, map);
                list.get(i).put("imageSrc", fileRefService.queryResourceByRecordId(param, req));
                if(goods.getVideoId() != null){
                    list.get(i).put("videoSrc",new StringBuffer(WebUtil.getBasePath(req))
                            .append("file/download/").append(goods.getVideoId()).append(".MP4").toString());
                }else{
                    list.get(i).put("videoSrc","none");
                }
            }
        }
        return ResponseMessage.ok(list);
    }


    //-----------------------------------------------------------------------------------------------------
    @ApiOperation(value = "小程序登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", name = "code", value = "临时登录凭证code", required = true),
    })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/registered", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage registered(@RequestParam("code") String code,
                                      @RequestParam(value = "encryptedData", defaultValue = "") String encryptedData,
                                      @RequestParam(value = "iv", defaultValue = "") String iv,
                                      @RequestParam(value = "parentId", defaultValue = "0") Long parentId,
                                      @RequestParam(value = "goodsId", defaultValue = "0") Long goodsId,
                                      HttpServletRequest request)
            throws NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException,
            NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException, InvalidParameterSpecException,
            IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        if (code == null || code.equals("")) {
            return ResponseMessage.error("请求参数错误");
        }
        //标识用户手机号信息是否存在
        boolean hasPhone = false;
        //根据微信code获取微信openid
        JSONObject result = tUserService.registered(code);
        if (result == null) {
            return ResponseMessage.error("获取用户ID失败,请重新登录");
        }
        String openid = result.get("openid").toString();
        String sessionKey = result.get("session_key").toString();
        TUser tUser = tUserService.createQuery().where(TUser.Property.openId, openid).single();
        //注册
        if (tUser == null) {
            tUser = new TUser();
            tUser.setId(GenericPo.createUID());
            tUser.setOpenId(openid);
            tUser.setStatus(0);
            tUser.setEarn(new BigDecimal(0));
            tUser.setCreateTime(new Date());
            tUser.setRoleId(Long.valueOf("10013"));
            tUser.setResidueDegree(0);
            if (parentId != 0) {
                tUser.setParentId(parentId);
                //插入用户分享关联表
                UserShare userShare = new UserShare();
                userShare.setGoodsId(goodsId);
                userShare.setParentId(parentId);
                userShare.setUserId(tUser.getId());
                userShare.setType(1);
                userShareService.insert(userShare);
            }
            tUserService.insert(tUser);
        } else {
            //根据微信加密信息获取手机号
            if (!encryptedData.equals("") && !iv.equals("")) {
                result = decrypt(encryptedData, iv, sessionKey);
                if (result == null) {
                    return ResponseMessage.error("获取手机号失败");
                }
                tUser.setPhone(result.getString("phoneNumber"));
                tUserService.update(tUser);
                hasPhone = true;
            }
        }
        if (tUser.getPhone() != null && !tUser.getPhone().equals("")) {
            hasPhone = true;
        }
        //登录
        User newUser = new User();
        BeanUtilsBean.getInstance().getPropertyUtils()
                .copyProperties(newUser, tUser);
        httpSessionManager.addUser(newUser, request.getSession());
        Map map = new HashMap();
        map.put("session", request.getSession().getId());
        map.put("hasPhone", hasPhone);
        map.put("userId", tUser.getId());
        return ResponseMessage.ok(map);
    }

    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "生成试衣次数缴费订单")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "tryTimes", value = "试衣次数", required = true),
    })
    @RequestMapping(value = "/createTryTimes", method = RequestMethod.POST)
    public ResponseMessage createTryTimes(Integer tryTimes) {
        TryTimeRecord tryTimeRecord = new TryTimeRecord();
        tryTimeRecord.setId(GenericPo.createUID());
        tryTimeRecord.setUserId(WebUtil.getLoginUser().getId());
        tryTimeRecord.setTryTimes(tryTimes);
        tryTimeRecord.setCreateTime(new Date());
        tryTimeRecordService.insert(tryTimeRecord);
        return ok(tryTimeRecord.getId());
    }
    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "试衣次数调用微信支付")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", dataType = "Long", name = "tryTimesId", value = "试衣次数缴费记录id", required = true),
    })
    @RequestMapping(value = "/TryTimesByWx", method = RequestMethod.POST)
    public ResponseMessage TryTimesByWx(Long tryTimesId, HttpServletRequest request) {
        TryTimeRecord tryTimeRecord = tryTimeRecordService.selectByPk(tryTimesId);
        return WxPayCommon.wxPay(tryTimesId.toString(),
                new BigDecimal(tryTimeRecord.getTryTimes() * TryTimeRecord.getUNITPRICE()), "试衣次数充值", request);
    }
    //--------------------------------------------------------------------------------------

    @ApiOperation(value = "支付成功，更新试衣次数")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "tryTimes", value = "更新试衣次数", required = true),
    })
    @RequestMapping(value = "/updateTryTimes", method = RequestMethod.POST)
    @Transactional
    public ResponseMessage updateTryTimes(Long tryTimesId) {
        TryTimeRecord tryTimeRecord = tryTimeRecordService.selectByPk(tryTimesId);
        tryTimeRecord.setCreateTime(new Date());
        tryTimeRecordService.update(tryTimeRecord);
        TUser tUser = tUserService.selectByPk(WebUtil.getLoginUser().getId());
        tUser.setResidueDegree(tUser.getResidueDegree() + tryTimeRecord.getTryTimes());
        tUserService.update(tUser);
        return ok(tryTimeRecord.getId());
    }


    /**
     * 解密小程序数据，获取手机号
     *
     * @param encryptData
     * @param iv
     * @param sessionKey
     * @return JSONObject
     * @throws Exception
     */
    public static JSONObject decrypt(String encryptData, String iv, String sessionKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidParameterSpecException,
            InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException,
            IllegalBlockSizeException, UnsupportedEncodingException {
        // 被加密的数据
        byte[] dataByte = Base64.decodeBase64(encryptData);
        // 加密秘钥
        byte[] keyByte = Base64.decodeBase64(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decodeBase64(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            //这个地方调用BouncyCastleProvider让java支持PKCS7Padding
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init(Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSONObject.parseObject(result);
            }
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    //-------------------------------------------------------------------------------------------------

    @ApiOperation(value = "代言商品数量", notes = "代言商品数量" +
            "返回map类型数据" +
            "<br>{" +
            "<br>int total 代言商品数量 ;" +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "userAgentGoodsNum", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage userAgentGoodsNum() {
        List<UserAgent> userAgentList = userAgentService.createQuery()
                .where(UserAgent.Property.userId, WebUtil.getLoginUser().getId())
                .list();
        Integer total;
        if (userAgentList.size() > 0) {
            total = userAgentList.size();
        } else {
            total = 0;
        }
        return ResponseMessage.ok(total);
    }


    /**
     * 更新用户信息
     *
     * @param tUser
     * @return
     */
    @ApiOperation(value = "更新用户信息")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ResponseMessage updateUserInfo(TUser tUser) {
        tUser.setId(WebUtil.getLoginUser().getId());
        tUserService.update(tUser);
        return ok("更新成功");
    }

    /**
     * 交易记录
     *
     * @return
     */
    @ApiOperation(value = "我的钱包->交易记录", notes = "返回map类型数据" +
            "<br>{" +
            "<br>id ID ;" +
            "<br>type 交易类型(0: 付款，1：退款，2：收益，3：返现，4：提现) ;" +
            "<br>goodsName 商品名称 ;" +
            "<br>goodsId  商品id ;" +
            "<br>createTime 创建日期;" +
            "<br>price 金额" +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/transactionRecord", method = RequestMethod.GET)
    public ResponseMessage transactionRecord(QueryParam param) {
        param.getParam().put("userId", WebUtil.getLoginUser().getId());
        return ok(orderProfitService.transactionRecord(param));
    }


    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "goodsId", value = "商品id", required = true),
            @ApiImplicitParam(paramType = "path", dataType = "int", name = "type", value = "标签切换值（0，会员列表，1 获得分润，2 其他）", required = true)

    })
    @ApiOperation(value = "用户代言情况", notes = "代言情况" +
            "返回map类型数据" +
            "<br>{" +
            "<br>用户列表 list 数据字段标识" +
            "<br>long userId 用户ID ;" +
            "<br>long userId 用户ID ;" +
            "<br>string  avatar 用户头像地址 ;" +
            "<br>string name 用户昵称 ;" +
            "<br>date createTime 邀请时间;" +

            "<br>用户列表 list 数据字段标识" +
            "<br>long userId 用户ID ;" +
            "<br>long userId 用户ID ;" +
            "<br>string  avatar 用户头像地址 ;" +
            "<br>string name 用户昵称 ;" +
            "<br>date createTime 购买时间;" +
            "<br>long   goodsId 商品ID ;" +
            "<br>string type 订单类型 (0:试衣购买，1：平台购买);" +
            "<br>double money 获得分润;" +
            "<br>string goodsName 商品名称;" +
            "<br>}")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/userAgentSituation/{goodsId}/{type}", method = RequestMethod.GET)
    public ResponseMessage userAgentSituation(@PathVariable("goodsId") Long goodsId, @PathVariable("type") Integer type) {
//        标签切换值（0，会员列表，1 获得分润，2 其他）
        TUser tUser = tUserService.createQuery().where(TUser.Property.id, WebUtil.getLoginUser().getId()).single();
        if (type == 0) {
            return ResponseMessage.ok(userShareService.userList(goodsId, tUser.getId()));
        } else if (type == 1) {
            return ResponseMessage.ok(userShareService.buyList(goodsId, tUser.getId()));
        } else {

        }

        return ResponseMessage.ok("");
    }

    @ApiOperation(value = "获取access_token")
    @RequestMapping(value = "/getAccessToken", method = RequestMethod.GET)
    //TODO 两小时刷新一次，不是每次获取
    public ResponseMessage getAccessToken() {
        String html = null;
        try {
            html = new SimpleRequestBuilder()
                    .http("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                            + TUser.getAPPID() + "&secret=" + TUser.getSECRET())
                    .resultAsJsonString() //  ->  header("Accept", "application/json");
                    .get()
                    .asString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (html != null) {
            return ok(JSON.parseObject(html));
        }
        return ResponseMessage.error("获取失败");
    }
}

