package com.base.web.controller.api;

import com.base.web.bean.*;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;

@Api(value = "ShopAddGoodsApiController",description = "商家端服装操作类接口文档")
@RestController
@RequestMapping(value = "/api/shopAddGoods")
public class ShopAddGoodsApiController{
    @Resource
    private ShopAddGoodsService shopAddGoodsService;
    @Resource
    private FileRefService fileRefService;
    @Resource
    protected ShopService shopService;
    @Resource
    private BrandService brandService;
    @Resource
    private SpecificationService specificationService;
    @Resource
    private GoodsClassService goodsClassService;
    @Resource
    private PropertyService propertyService;

    @Resource
    protected GoodsSpeService goodsSpeService;

    @Resource
    private DeviceService deviceService;

    @Resource
    private ShopDeviceService shopDeviceService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value="商家端上传商品接口",notes = "上传数据除了文件之外其他的以json的格式上传" +
            "{" +
            "String name：商品名称<br>" +
            "Integer brandId：品牌id<br>" +
            "Integer classId：类别id<br>" +
            "String sex：适合性别<br>" +
            "Integer ageGrade：适合年龄段<br>" +
            "BigDecimal price：价格<br>" +
            "String [] sizes：尺码数组<br>" +
            "Integer occasionId：适合场合id<br>" +
            "}<br>"
    )
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imagesId", value = "图片ID数组",
                    required = true, dataType = "String []", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "商品名称",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "brandId", value = "品牌id",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "classId", value = "类别id",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "sex", value = "适合性别",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "ageGrade", value = "适合年龄段",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "price", value = "价格",
                    required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "sizes", value = "尺码数组",
                    required = true, dataType = "String []", paramType = "form"),
            @ApiImplicitParam(name = "occasionId", value = "适合场合",
                    required = true, dataType = "Integer", paramType = "form"),
    })
    public ResponseMessage add( ShopAddGoods data){
        shopAddGoodsService.insertShopAddGoods(data);
        return ResponseMessage.ok("添加成功");
    }

    // --------------------------------------------------------------------------------

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value="商家端上传商品接口",notes = "上传数据除了文件之外其他的以json的格式上传" +
            "{" +
            "String name：商品名称<br>" +
            "Integer brandId：品牌id<br>" +
            "Integer classId：类别id<br>" +
            "String sex：适合性别<br>" +
            "Integer ageGrade：适合年龄段<br>" +
            "BigDecimal price：价格<br>" +
            "String [] sizes：尺码数组<br>" +
            "Integer occasionId：适合场合id<br>" +
            "}<br>"
    )
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "imagesId", value = "图片ID数组",
                    required = true, dataType = "String []", paramType = "form"),
            @ApiImplicitParam(name = "name", value = "商品名称",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "brandId", value = "品牌id",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "classId", value = "类别id",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "sex", value = "适合性别",
                    required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "ageGrade", value = "适合年龄段",
                    required = true, dataType = "Integer", paramType = "form"),
            @ApiImplicitParam(name = "price", value = "价格",
                    required = true, dataType = "BigDecimal", paramType = "form"),
            @ApiImplicitParam(name = "sizes", value = "尺码数组",
                    required = true, dataType = "String []", paramType = "form"),
            @ApiImplicitParam(name = "occasionId", value = "适合场合",
                    required = true, dataType = "Integer", paramType = "form"),
    })
    public ResponseMessage edit( ShopAddGoods data){
        shopAddGoodsService.editShopAddGoods(data);
        return ResponseMessage.ok("更新成功");
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="根据当前登录商家查询商品列表", notes="List<Map>中Map数据：" +
            "<br>{" +
            "<br>Long goodsId 商品id ;" +
            "<br>String name 商品名称 ;" +
            "<br>BigDecimal price 商品价格 ;" +
            "<br>String sex 适合性别 ;" +
            "<br>String ageGrade 适合年龄段 ;" +
            "<br>Date createTime 创建时间;" +
            "<br>String occasion 适宜场合 ;" +
            "<br>String className 类别名称 ;" +
            "<br>String resourceId 资源id " +
            "<br>String imageUrl 资源路径 " +
            "<br>}",response =HashMap.class,
            responseContainer = "List")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "/queryPagerShop", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryUserVideo(QueryParam queryParam , HttpServletRequest req) {
        Shop shop = shopService.createQuery().where(Shop.Property.userId,WebUtil.getLoginUser().getId()).single();
        queryParam.getParam().put("shopId",shop.getId());
        PagerResult<Map> object = shopAddGoodsService.pagerShopGoods(queryParam);
        for(Map map : object.getData()){
            map.put("imageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("resourceId")).trim()));
        }
        return ok(object);
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="根据商品ID查询商品信息/编辑商品", notes="Map数据：" +
            "<br>goodsClass{" +
            "<br>classId 类别ID ;" +
            "<br>className 类别名称 ;" +
            "<br>}" +
            "<br>property{" +
            "<br>propertyId 适合场景ID ;" +
            "<br>propertyName 年龄段 ;" +
            "<br>}" +
            "<br>specification{" +
            "<br>id 规格ID ;" +
            "<br>name 规格名称 ;" +
            "<br>type 0：尺寸，1：颜色 ;" +
            "<br>}" +
            "<br>brand{" +
            "<br>id 品牌ID ;" +
            "<br>name 品牌名称 ;" +
            "<br>}" +
            "<br>goodsInfo{" +
            "<br>classId 类别ID ;" +
            "<br>goodsId 商品ID ;" +
            "<br>price 价格 ;" +
            "<br>brandId 品牌名称 ;" +
            "<br>sex 性别 ;" +
            "<br>imageSrc 图片对应资源ID，RecordId ;" +
            "<br>propertyId 适应年龄段ID ;" +
            "<br>name 商品名称 ;" +
            "<br>color 颜色 ;" +
            "<br>size 尺寸 ;" +
            "<br>imags 图片列表{" +
            "<br>resourceId 图片ID ;" +
            "<br>resourceUrl 图片路径 ;" +
            "<br>}" +
            "<br>}" ,response =HashMap.class,
            responseContainer = "List")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "/editGoodInfo/{goodsId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage editGoodInfo(@PathVariable("goodsId") Long goodsId , HttpServletRequest req) {
        Map shopAddGoods = shopAddGoodsService.queryById(goodsId);
        QueryParam param = new QueryParam();
        param.getParam().put("recordId", shopAddGoods.get("imageSrc"));
        param.getParam().put("dataType", 10);
        shopAddGoods.put("imags",fileRefService.queryResourceByRecordId(param, req));
        if(shopAddGoods.get("videoId") != null){
            shopAddGoods.put("videoSrc", new StringBuffer(WebUtil.getBasePath(req))
                    .append("file/download/").append(shopAddGoods.get("videoId")).append(".MP4").toString());
        }else{
            shopAddGoods.put("videoSrc", "none");
        }
        Map map = editGoodsShowInfo();
        map.put("goodsInfo", shopAddGoods);
        return ok(map);
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="添加商品页面显示选择信息", notes="Map数据：" +
            "<br>goodsClass{" +
            "<br>classId 类别ID ;" +
            "<br>className 类别名称 ;" +
            "<br>}" +
            "<br>property{" +
            "<br>propertyId 适合场景ID ;" +
            "<br>propertyName 年龄段 ;" +
            "<br>}" +
            "<br>specification{" +
            "<br>id 规格ID ;" +
            "<br>name 规格名称 ;" +
            "<br>type 0：尺寸，1：颜色 ;" +
            "<br>}" +
            "<br>brand{" +
            "<br>id 品牌ID ;" +
            "<br>name 品牌名称 ;" +
            "<br>}" ,response =HashMap.class,
            responseContainer = "List")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "/addGoodsShowInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage addGoodsShowInfo() {
        return ok(editGoodsShowInfo());
    }

// --------------------------------------------------------------------------------

    //编辑商品页面，显示品牌，类目，等选择信息
    public Map editGoodsShowInfo(){
        //品牌列表
        List<Map> brandsList = brandService.queryAllBrand();
        //类别列表，2:二级节点
        List<GoodsClass> goodsClassList = goodsClassService.createQuery().list();
        //年龄段列表 1：年龄段，2：适合场合
        List<Map> ageList = propertyService.selectByType(1);
        List<Map> applicationList = propertyService.selectByType(2);

        //规格列表
        List<Specification> specificationList = specificationService.queryAllSpec();
        Map map = new HashMap();
        map.put("brand", brandsList);
        map.put("goodsClass", goodsClassList);
        map.put("ageList", ageList);
        map.put("applicationList", applicationList);
        map.put("specification", specificationList);
        return  map;
    }


    // --------------------------------------------------------------------------------
    @ApiOperation(value="查询商品详情", notes="List<Map>中Map数据：" +
            "<br>{" +
            "<br>Long goodsId 商品ID" +
            "<br>String GoodsSpec 商品规格 ;" +
            "<br>String name 商品名称 ;" +
            "<br>BigDecimal price 商品价格 ;" +
            "<br>Long imageId 轮播图片ID ;" +
            "<br>String imageUrl 轮播图片路径;" +
            "<br>String shopName 商店名称 ;" +
            "<br>}",response =HashMap.class,
            responseContainer = "List")
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "/queryShopGoodsDeatil/{uId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage queryShopGoods(@PathVariable("uId")Long uId,HttpServletRequest req) {
        Map shopGoods = shopAddGoodsService.selectShopGoodsById(uId);
        QueryParam param = new QueryParam();
        param.getParam().put("dataType", 10);
        param.getParam().put("recordId", shopGoods.get("imageId"));
        //获取商品轮播图片
        List<Map> imageUrl = fileRefService.queryResourceByRecordId(param, req);
        shopGoods.put("imageUrl",imageUrl);
        //获取商品规格
        List<GoodsSpecification> goodsSpecificationList = goodsSpeService.queryGoodsSpecification(uId);
        String goodsSpec ="";
        for(GoodsSpecification goodsSpecification:goodsSpecificationList){
            goodsSpec+=goodsSpecification.getSize()+" ";
        }
        shopGoods.put("GoodsSpec",goodsSpec);
        return ok(shopGoods);
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="商品上架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID",
                    required = true, dataType = "Long", paramType = "path"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "{id}/shelf", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage Shelf(@PathVariable("id")Long id) {
        shopAddGoodsService.modifyStatus(id, 0);
        return ok();
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="商品下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID",
                    required = true, dataType = "Long", paramType = "path"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "{id}/dropOff", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage DropOff(@PathVariable("id")Long id) {
        shopAddGoodsService.modifyStatus(id, 1);
        return ok();
    }

    // --------------------------------------------------------------------------------
    @ApiOperation(value="商品删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID",
                    required = true, dataType = "Long", paramType = "path"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage delete(@PathVariable("id")Long id) {
        shopAddGoodsService.modifyStatus(id, 2);
        return ok();
    }



    @ApiOperation(value="显示活动商品")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceUserName", value = "设备用户名",
                    required = true, dataType = "String", paramType = "path"),
    })
    @ApiResponses({ @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 201, message = "数据已经存在，请重填"),
            @ApiResponse(code = 401, message = "没有访问权限"),
            @ApiResponse(code = 403, message = "禁止访问该数据"),
            @ApiResponse(code = 404, message = "服务不存在"),
            @ApiResponse(code = 500, message = "服务器内部异常") })
    @RequestMapping(value = "eventGoods/{deviceUserName}", method = RequestMethod.GET)
    public ResponseMessage eventGoods(@PathVariable("deviceUserName") String deviceUserName, HttpServletRequest req){
        Device device = deviceService.createQuery().where(Device.Property.username, deviceUserName).single();
        Long shopId = shopDeviceService.createQuery()
                .where(ShopDevice.Property.deviceId, device.getId()).single().getShopId();
        List<Map> list = shopAddGoodsService.queryEventGoods(shopId);
        QueryParam param = new QueryParam();
        param.getParam().put("dataType", 10);
        for (Map map : list) {
            if(map.get("videoSrc") != null){
                map.put("videoSrc", new StringBuffer(WebUtil.getBasePath(req))
                        .append("file/download/").append(map.get("videoSrc")).append(".MP4").toString());
            }else{
                map.put("videoSrc", "none");
            }
            param.getParam().put("recordId", map.get("imageSrc"));
            List<Map> imgs = fileRefService.queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                map.put("imageSrc", imgs);
            }
        }

        list = fileRefService.addImages(list, 10, req);
        return ok(list);
    }
}
