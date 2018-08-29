package com.base.web.controller.api;

import com.alibaba.fastjson.JSONObject;
import com.base.web.bean.*;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.ShoppingCartMapper;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.base.web.service.DeleteService.createDelete;

/**
 * 商品类别树查询接口类
 * 2018-04-16
 *
 * @Author: FQ
 * @Date: 2018/4/17 10:07
 */

@Api(value = "GoodsApiController", description = "客户端商品操作接口类")
@RequestMapping("/api/goods")
@RestController
public class GoodsApiController {
    @Resource
    private GoodsClassService goodsClassService;

    @Resource
    private GoodsService goodsService;

    @Resource
    private FileRefService fileRefService;


    @Resource
    private GoodsSpeService goodsSpeService;
    @Resource
    private RecommendGoodsService recommendGoodsService;
    @Resource
    private BrandService brandService;

    @Resource
    private GoodsCommentService goodsCommentService;

    @Resource
    private TUserService tUserService;

    @Resource
    private ShopService shopService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private UserAgentService userAgentService;

    @Resource
    private GoodsTryService goodsTryService;

    @Resource
    private OrderService orderService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private GoodsShopService goodsShopService;


    @ApiOperation(value = "商品类别树节点查询接口", notes = "商品类别树节点查询接口List<Map>中Map数据:" +
            "<br>className : 类别名称 String" +
            "<br>id : 类别id Long" +
            "<br>level : 类别等级 Integer" +
            "<br>parentId : 父id Long", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {@ApiImplicitParam(paramType = "path", dataType = "Integer",
                    name = "parentId", value = "父id,如果父id为0,则默认为查询根节点", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "Integer",
                            name = "level", value = "节点等级", required = true)
            })
    @RequestMapping(value = "/queryGoodsClass/{parentId}/{level}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage GoodsClassShow(@PathVariable("parentId") Integer parentId, @PathVariable("level") Integer level, HttpServletRequest req) {
        List<Map> goodsClassList = goodsClassService.queryGoodsClassByParentId(parentId, level);
        for (Map goodsClass : goodsClassList) {
            goodsClass.put("imageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(goodsClass.get("resourceId"))));
        }
        return ResponseMessage.ok(goodsClassList);
    }

// ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "商品品牌查询接口", notes = "商品查询品牌接口List<Map>中Map数据:" +
            "<br>id : 品牌id Long" +
            "<br>name : 品牌名称 String" +
            "<br>status : 品牌是否有在品台出售（0没有，1有) Integer", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryAllBrand", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoodsBrand() {
        List<Map> brandList = brandService.queryAllBrand();
        return ResponseMessage.ok(brandList);
    }

// ------------------------------------------------------------------------------------------------------------------------


    @ApiOperation(value = "某类别全部商品查询接口", notes = "类别商品查询接口List<Map>中Map数据:" +
            "<br>carouselId: 轮播图片关联id Long" +
            "<br>imageId: 商品图片关联id Long" +
            "<br>imagePath: 商品图片路径 String" +
            "<br>num: 商品库存 Integer" +
            "<br>goodsClassId: 商品类别id String" +
            "<br>sales: 商品销售量 Integer" +
            "<br>createTime: 创建时间 Date" +
            "<br>price: 商品价格 BigDecimal" +
            "<br>brandId: 品牌id Long" +
            "<br>id: 商品id Long" +
            "<br>describe: 商品详情 String" +
            "<br>goodsName: 商品名称 String" +
            "<br>status: 商品状态（1 在售，0 下架，2售罄）Integer", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "String", name = "goodsClassId", value = "根据类别id查询商品", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "statusId", value = "排序参数(0 最新，1 销量，2 价格排序(从大到小) 3（从小到大）)", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "String", name = "searchStr", value = "搜索条件"),
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
            })
    @RequestMapping(value = "/queryGoods", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoods(@RequestParam Map map, HttpServletRequest req, QueryParam queryParam , SearchBean searchBean) {
        queryParam.getParam().put("statusId", map.get("statusId"));
        queryParam.getParam().put("searchStr", searchBean.getSearchStr());
        //        服装类别
        if(searchBean.getLevel() == 1){//查全部  classId === 1
            queryParam.getParam().put("classId",1);
        }else {
            queryParam.getParam().put("classId",searchBean.getClassId().toString().substring(0,searchBean.getLevel() * 2));
        }
//        品牌
        if(searchBean.getBrandId() ==1){//查全部  brandId === 1
            queryParam.getParam().put("brandId",1);
        }else {
            queryParam.getParam().put("brandId",searchBean.getBrandId());
        }
//        价格
        if(searchBean.getStartPrice() != null && searchBean.getEndPrice() != null && searchBean.getEndPrice().compareTo(searchBean.getStartPrice()) != -1  ){
            queryParam.getParam().put("startPrice",searchBean.getStartPrice());

            queryParam.getParam().put("endPrice",searchBean.getEndPrice());
        }else {
            if(searchBean.getStartPrice() == null){
                searchBean.setStartPrice(BigDecimal.valueOf(0));
            }
            queryParam.getParam().put("endPrice",-1);
            queryParam.getParam().put("startPrice",searchBean.getStartPrice());
        }

        PagerResult<Map> goodsList = goodsService.queryGoods(queryParam);
        for (Map aGoodsList : goodsList.getData()) {
         aGoodsList.put("imagePath", ResourceUtil.resourceBuildPath(req, String.valueOf(aGoodsList.get("compressId")).trim()));
        }
        return ResponseMessage.ok(goodsList).setCode(Integer.valueOf(map.get("statusId").toString()));
    }

// ------------------------------------------------------------------------------------------------------------------------


    @ApiOperation(value = "单个商品详情查询接口", notes = "单个商品详情查询接口List<Map>中Map数据:" +
            "<br>GoodsSpec:商品规格List<Map>[" +
            "<br>&nbsp;&nbsp;color: 颜色 String" +
            "<br>&nbsp;&nbsp;goodsId: 商品id Long" +
            "<br>&nbsp;&nbsp;id: 规格id Long" +
            "<br>&nbsp;&nbsp;quality: 数量 Integer" +
            "<br>&nbsp;&nbsp;size: 尺寸 String" +
            "<br>]" +
            "<br>carouselImage:轮播图片List<Map>[" +
            "<br>&nbsp;&nbsp;resourceId: 资源主键 Long" +
            "<br>&nbsp;&nbsp;re\n" +
            "            @ApiResponse(code = 404, message = \"服务不存在\"),sourceUrl: 轮播图片路径 String" +
            "<br>&nbsp;&nbsp;type: 类型： 0 视频图片，1 视频，2是广告视频，3是广告图片 Integer" +
            "<br>&nbsp;&nbsp;md5: md5 String" +
            "<br>]" +
            "<br>carouselId: 轮播图片关联id Long" +
            "<br>num: 商品库存 Integer" +
            "<br>sales: 商品销售量 Integer" +
            "<br>createTime: 创建时间 Date" +
            "<br>price: 商品价格 BigDecimal" +
            "<br>id: 商品id Long" +
            "<br>describe: 商品详情 String" +
            "<br>goodsName: 商品名称 String" +
            "<br>status: 商品状态（1 在售，0 下架，2售罄）Integer", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "商品id", required = true),
            })
    @RequestMapping(value = "/queryGoods/{goodsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoodsDescribe(@PathVariable("goodsId") Long goodsId, Long goodsSpecId, HttpServletRequest req) {
        Map goods = goodsService.queryGoodsAndSales(goodsId, WebUtil.getLoginUser().getId());
        List<GoodsSpecification> goodsSpec = goodsSpeService.queryGoodsSpecification(goodsId);
        goods.put("GoodsSpec", goodsSpec);
        QueryParam queryParam = new QueryParam();
        queryParam.getParam().put("recordId", goods.get("carouselId"));
        queryParam.getParam().put("dataType", 3);
        List<Map> carouselList = fileRefService.queryResourceByRecordId(queryParam, req);
        goods.put("carouselImage", carouselList);
        if (goodsSpecId != null) {
            goods.put("choseGoodsSpec", goodsSpeService.selectByPk(goodsSpecId));
        }
        goods.put("userId", WebUtil.getLoginUser().getId());
        return ResponseMessage.ok(goods);
    }

// ------------------------------------------------------------------------------------------------------------------------


    @ApiOperation(value = "商品评论查询接口", notes = "商品评论查询接口List<Map>中Map数据:" +
            "<br>uId : 评论id Long" +
            "<br>imageId : 评论图片关联id Long" +
            "<br>star : 评论星级 Integer" +
            "<br>color : 商品颜色 String" +
            "<br>size : 商品尺寸 String" +
            "<br>createTime : 评论时间 Date" +
            "<br>avatar : 用户头像 Long" +
            "<br>userName : 用户名 String" +
            "<br>userId : 用户id Long" +
            "<br>content : 评论内容 String", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "商品id", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageIndex", value = "数据查询起始索引", required = true),
                    @ApiImplicitParam(paramType = "query", dataType = "Integer", name = "pageSize", value = "一页查询几条数据", required = true)
            })
    @RequestMapping(value = "/queryGoodsComment/{goodsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryGoodsComment(@PathVariable("goodsId") Long goodsId, QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> goodsCommentList = goodsCommentService.queryGoodsComment(goodsId, queryParam);
        List<Map> imageList;
        Map tUser;
        for (Map aGoodsCommentList : goodsCommentList.getData()) {
            tUser = tUserService.queryTUserById(aGoodsCommentList.get("userId"));
            queryParam.getParam().put("recordId", aGoodsCommentList.get("imageId"));
            queryParam.getParam().put("dataType", 6);
            imageList = fileRefService.queryResourceByRecordId(queryParam, req);
            aGoodsCommentList.put("imageList", imageList);
            if (tUser.size() > 0) {
                aGoodsCommentList.put("userName", tUser.get("name"));
                aGoodsCommentList.put("avatar", tUser.get("avatar").toString().trim());
            }
        }
        return ResponseMessage.ok(goodsCommentList);
    }


// ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "商品周围店铺查询接口", notes = "商品周围店铺查询接口List<Map>中Map数据:" +
            "<br>distance : 店铺距离(千米) String" +
            "<br>address : 店铺地址 String" +
            "<br>shopName : 店铺名称 String" +
            "<br>shopId : 店铺id Long", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Long", name = "goodsId", value = "商品id", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "String", name = "laltitude", value = "用户位置经度", required = true),
                    @ApiImplicitParam(paramType = "path", dataType = "String", name = "longtitude", value = "用户位置纬度", required = true)
            })
    @RequestMapping(value = "/queryAroundShop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryAroundShop(@RequestBody UserAddress address, HttpServletRequest req) {
        Long userId = WebUtil.getLoginUser().getId();
        List<Map> shopList;
        if (address.getLaltitude() == null && address.getLongtitude() == null) {
            Map userAddress = userAddressService.queryUserAddress(userId);
            shopList = shopService.queryAroundShop(userAddress.get("longtitude").toString(), userAddress.get("laltitude").toString(), String.valueOf(address.getGoodsId()), req);
        } else {
            shopList = shopService.queryAroundShop(address.getLongtitude(), address.getLaltitude(), String.valueOf(address.getGoodsId()), req);
        }

        return ResponseMessage.ok(shopList);
    }


// ------------------------------------------------------------------------------------------------------------------------


    @RequestMapping(value = "/insertAgent", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "平台服装代言记录接口", notes = "需传入的是一个数组:" +
            "<br>goodsId : 商品id Long")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage insertAgent(@RequestBody List<JSONObject> goodsList) {
        for (int i = 0; i < goodsList.size(); i++) {
            UserAgent userAgent = userAgentService.createQuery()
                    .where(UserAgent.Property.goodsId, goodsList.get(i).get("goodsId"))
                    .and(UserAgent.Property.userId,WebUtil.getLoginUser().getId())
                    .single();
            if(userAgent != null){
                userAgent.setStatus(0);
                userAgentService.update(userAgent);
            }else {
                userAgentService.insertAgent(goodsList.get(i));
            }
        }
        return ResponseMessage.ok();
    }

// ------------------------------------------------------------------------------------------------------------------------


    @RequestMapping(value = "/insertGoodsTry/{shoppingStatus}", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "试衣订单记录接口", notes = "需传入对象List<>的值:" +
            "<br>goodsId: 商品ID Long" +
            "<br>goodsSpecId: 规格ID Long" +
            "<br>num: 商品数量 Integer" +
            "<br>addressId : 收货地址ID Long" +
            "<br>shoppingStatus :判断是否是在购物车中（0 否，1 是）Integer")
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shoppingStatus", value = "判断是否是在购物车中", required = true),
            })
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    public ResponseMessage insertGoodsTry(@RequestBody TryInfo[] tryInfos, @PathVariable("shoppingStatus") int shoppingStatus) {
        Long flag = goodsTryService.insertGoodsTry(tryInfos);
        //删除购物车中的记录
        if (shoppingStatus != 0) {
            for (int i = 0; i < tryInfos.length; i++) {
                if (tryInfos[i].getShoppingCartId() != null) {
                    shoppingCartService.delete(tryInfos[i].getShoppingCartId());
                }
            }
        }

        if (flag == 0) {
            return ResponseMessage.error("试穿次数不足");
        }
        if (flag == -1) {
            return ResponseMessage.error("商品库存不够");
        }
        return ResponseMessage.ok(flag);
    }

// ------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/insertShoppingCart", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "购物车生成接口", notes = "需传入对象的值:" +
            "<br>goodsId : 商品ID Long" +
            "<br>goodsSpecId : 规格ID Long"
    )
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @Transactional
    public ResponseMessage insertShoppingCart(@RequestBody ShoppingCart shoppingCart) {
        shoppingCart.setUserId(WebUtil.getLoginUser().getId());
        shoppingCart.setCreateTime(new Date());

        ShoppingCart queryShoppingCart = (shoppingCartService.createQuery()
                .where(ShoppingCart.Property.goodsSpecId,shoppingCart.getGoodsSpecId())
                .and(ShoppingCart.Property.userId,shoppingCart.getUserId())
                .and(ShoppingCart.Property.goodsId,shoppingCart.getGoodsId()).single());

        if(shoppingCart.getId() != null){
            //判断相同规格，但ID不同的记录
            if(queryShoppingCart != null && !queryShoppingCart.getId().equals(shoppingCart.getId())){
                //若存在，将其数量相加
                shoppingCart.setNum(shoppingCart.getNum()+queryShoppingCart.getNum());
                shoppingCartService.delete(queryShoppingCart.getId());
                return ResponseMessage.ok(shoppingCartService.update(shoppingCart));
            }
            return ResponseMessage.ok(shoppingCartService.update(shoppingCart));
        }
        // 判断是否存在相同规格的记录
        else if(queryShoppingCart != null){
            queryShoppingCart.setNum(shoppingCart.getNum()+queryShoppingCart.getNum());
            return ResponseMessage.ok(shoppingCartService.update(queryShoppingCart));
        }
        shoppingCart.setId(GenericPo.createUID());
        return ResponseMessage.ok(shoppingCartService.insert(shoppingCart));
    }


// ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "用户地址查询接口", notes = "传入参数（status : 地址状态（0:用户全部地址,1 : 用户默认地址）Integer）" +
            "<br>用户地址查询接口List<Map>中Map数据:" +
            "<br>uId : 地址id Long" +
            "<br>address : 用户地址 String" +
            "<br>phone : 用户电话 String" +
            "<br>name : 收货人姓名 String", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryAllUserAddress/{status}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryAllUserAddress(@PathVariable("status") Integer status, String addressId) {
        if (addressId != null && addressId != "") {
            UserAddress address = userAddressService.selectByPk(Long.valueOf(addressId));
            return ResponseMessage.ok(address);
        }
        else if(status == 1){
            UserAddress address = userAddressService.createQuery()
                    .where(UserAddress.Property.status,1)
                    .and(UserAddress.Property.userId,WebUtil.getLoginUser().getId()).single();
            return ResponseMessage.ok(address);
        }
        List<Map> addressList = userAddressService.queryUserAddressByUserId();
        if (addressList == null) {
            return ResponseMessage.ok("该用户没有收货地址");
        }
        return ResponseMessage.ok(addressList);
    }

    // ------------------------------------------------------------------------------------------------------------------------

    @ApiOperation(value = "查询首页推荐商品", notes = "传入参数（ Integer goodsClassId : 商品类别" +
            "<br>List<Map>中Map数据:" +
            "<br>goodsId : 地址id Long" +
            "<br>resourceId : 用户地址 Long" +
            "<br>imageUrl : 用户电话 String" +
            "<br>", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryRecommendGoods/{goodsClassId}/{level}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryRecommendGoods(@PathVariable("goodsClassId") String goodsClassId, @PathVariable("level") Integer level, HttpServletRequest req) {

        String goodsClassPrefixId = goodsClassId.substring(0, level * 2);
        List<Map> recommendGoods = recommendGoodsService.queryRecommendGoodsByGoodsClassId(level * 2, Integer.valueOf(goodsClassPrefixId));
        if (recommendGoods.size() > 0) {
            for (Map map : recommendGoods) {
                map.put("imageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("resourceId")).trim()));
            }
        }
        return ResponseMessage.ok(recommendGoods);
    }

// ------------------------------------------------------------------------------------------------------------------------

    @RequestMapping(value = "/insertGoodsOrder/{shoppingStatus}/{insertType}", method = RequestMethod.POST, consumes = "application/json")
    @ApiOperation(value = "订单生成接口", notes = "需传入对象List<>的值:" +
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
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(paramType = "path", dataType = "Integer", name = "shoppingStatus", value = "判断是否是在购物车中", required = true),
            })
    public ResponseMessage insertGoodsOrder(@RequestBody OrderDetail[] orderDetails, @PathVariable("shoppingStatus") int shoppingStatus,@PathVariable("insertType") int insertType) {
        if(orderDetails == null || orderDetails.length == 0){
            return ResponseMessage.error("没有添加商品");
        }
        if(orderDetails[0].getAddressId() == null){
            return ResponseMessage.error("没有添加地址");
        }
        if (shoppingStatus == 1) {
            for (int i = 0; i < orderDetails.length; i++) {
                if (orderDetails[i].getShoppingCartId() != null) {
                    shoppingCartService.delete(orderDetails[i].getShoppingCartId());
                }
            }
        }
        //返回订单id
        return ResponseMessage.ok(orderService.insertOrder(orderDetails,insertType));
    }

    // ------------------------------------------------------------------------------------------------------------------------


    @ApiOperation(value = "购物车商品查询接口", notes = "购物车商品查询接口List<Map>中Map数据:" +
            "<br>uId : 主键ID Long" +
            "<br>color : 颜色 String" +
            "<br>size : 尺寸 String" +
            "<br>goodsId : 商品ID Long" +
            "<br>price : 商品价格 BigDecimal" +
            "<br>goodsSpecId : 商品规格ID Long" +
            "<br>agentStatus : 商品代言状态 Integer", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/queryShoppingCart", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryShoppingCart(HttpServletRequest req) {
        List<Map> shoppingCartList = shoppingCartService.queryShoppingCart();
        for (Map shoppingCart : shoppingCartList) {
            shoppingCart.put("imageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(shoppingCart.get("resourceId")).trim()));
        }
        return ResponseMessage.ok(shoppingCartList);
    }

    @RequestMapping(value = "/queryResidueDegree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryResidueDegree() {
        TUser tUser = tUserService.createQuery().where(TUser.Property.id, WebUtil.getLoginUser().getId()).single();

        return ResponseMessage.ok(tUser.getResidueDegree());
    }


    @ApiOperation(value = "删除购物车商品接口")
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
    @RequestMapping(value = "deleteGoods/{shoppingId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage deleteAddress(@PathVariable("shoppingId") Long shoppingId) {

        return ResponseMessage.ok(createDelete(shoppingCartMapper).where(ShoppingCart.Property.userId, WebUtil.getLoginUser().getId())
                .and(ShoppingCart.Property.id, shoppingId).exec());
    }


    @ApiOperation(value = "购物车商品数量修改接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/updateShopCarGoodsNum/{shoppingId}/{num}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage updateShopCarGoodsNum(@PathVariable("shoppingId") String shoppingId, @PathVariable("num") Integer num) {
        QueryParam param = new QueryParam();
        if (shoppingId != "" && num > 0) {
            param.getParam().put("userId", WebUtil.getLoginUser().getId());
            param.getParam().put("shoppingId", shoppingId);
            param.getParam().put("num", num);
            return ResponseMessage.ok(shoppingCartService.updateGoodsNum(param));
        }
        return ResponseMessage.error("插入失败");
    }

    @ApiOperation(value = "筛选页面条件数据查询接口", notes = "筛选页面条件数据查询接口<Map>中Map数据:" +
            "<br>clothesTabs : 全部商品类别 " +
            "<br>brandTabs : 品牌列表" +
            "<br>allclassify : 全部商品类别 ", response = HashMap.class,
            responseContainer = "List")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/filtrate", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage Filtrate(HttpServletRequest req) {
       Map filtrateList = new HashMap();
       filtrateList.put("clothesTabs",goodsClassService.createQuery()
               .where(GoodsClass.Property.level,2).list());
       filtrateList.put("brandTabs",brandService.select());
       return ResponseMessage.ok(filtrateList);
    }

    @ApiOperation(value = "筛选页面全部服装数据查询接口")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/allClassify/{classId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage FiltrateAllClassify(@PathVariable("classId") String classId,HttpServletRequest req) {

        if(classId.equals("1")){
            return ResponseMessage.ok(goodsClassService.createQuery()
                    .where(GoodsClass.Property.level,3).list());
        }
        return ResponseMessage.ok(goodsClassService.createQuery()
                .where(GoodsClass.Property.parentId,classId).list());
    }

    @ApiOperation(value = "根据店铺ID查询品牌、类别")
    @ApiResponses({@ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常")})
    @RequestMapping(value = "/shop/{shopId}/class", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryBrandAndClassByShopId(@PathVariable("shopId") Long shopId) {
        Map map = new HashMap();
        List<Map> brands = goodsShopService.queryBrandByShopId(shopId);
        List<Map> category = goodsShopService.queryClassByShopId(shopId);
        map.put("brand", brands);
        map.put("class", category);
        return ResponseMessage.ok(map);
    }
}
