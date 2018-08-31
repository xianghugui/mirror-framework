package com.base.web.controller;

import com.base.web.bean.GoodsShop;
import com.base.web.bean.Shop;
import com.base.web.bean.ShopDevice;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsShopService;
import com.base.web.service.ShopBrandService;
import com.base.web.service.ShopDeviceService;
import com.base.web.service.ShopService;
import com.base.web.service.resource.FileRefService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;

/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/shop")
@AccessLogger("店铺管理")
@Authorize(module = "shop")
public class ShopController extends GenericController<Shop, Long> {

    @Resource
    private ShopService shopService;

    @Resource
    private FileRefService fileRefService;
    @Resource
    private GoodsShopService goodsShopService;

    @Resource
    private ShopDeviceService shopDeviceService;

    @Resource
    private ShopBrandService shopBrandService;

    @Override
    protected ShopService getService() {
        return this.shopService;
    }

    @RequestMapping(value = "/queryshopbyareaid/{areaId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryshopbyareaid(@PathVariable("areaId") Integer areaId){
        return ok(getService().selectByAreaId(areaId));
    }

    /**
     * 请求添加数据，请求必须以POST方式
     *
     * @param map 请求添加的对象
     * @return 被添加数据的主键值
     * @throws javax.validation.ValidationException 验证数据格式错误
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @AccessLogger("新增")
    @Authorize(action = "C")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseMessage addshop(@RequestBody Map<String, Object> map) {
        if (map.get("logo") == null || "".equals(map.get("logo"))) {
            return ResponseMessage.error("店铺logo不能为空", 500);
        }
        if (map.get("businessUrl") == null || "".equals(map.get("businessUrl"))) {
            return ResponseMessage.error("店铺营业执照图片不能为空", 500);
        }
        if (map.get("img") == null || "".equals(map.get("img"))) {
            return ResponseMessage.error("店铺图片不能为空", 500);
        }
        Shop shop = new Shop();
        //设置创建用户id
        shop.setId(GenericPo.createUID());
        shop.setName((String)map.get("shopName"));
        shop.setAddress((String)map.get("address"));
        shop.setLongtitude((String)map.get("longitude"));
        shop.setLatitude((String)map.get("latitude"));
        shop.setAreaId((Integer) map.get("areaId"));
        shop.setUserId(Long.parseLong(map.get("userId").toString()));
        shop.setBusinessId(GenericPo.createUID());
        shop.setContent(map.get("content").toString());
        shop.setStatus(0);
        shop.setCreateTime(new Date());

        Long id = getService().insert(shop);
        //插入资源管理表
        //logo
        FileRef tFileRef = new FileRef();
        tFileRef.setRefId(shop.getBusinessId());
        tFileRef.setType(0);

        tFileRef.setDataType(4);
        tFileRef.setResourceId(Long.parseLong(map.get("logo").toString()));
        tFileRef.setId(GenericPo.createUID());
        fileRefService.insert(tFileRef);

        //店铺图片
        tFileRef.setDataType(5);
        String[] imgs = map.get("img").toString().split(" ");
        for (int i = 0; i < imgs.length ; i++) {
            tFileRef.setResourceId(new Long(imgs[i]));
            tFileRef.setId(GenericPo.createUID());
            fileRefService.insert(tFileRef);
        }
        //营业执照
        tFileRef.setDataType(1);
        tFileRef.setResourceId(new Long(map.get("businessUrl").toString()));
        tFileRef.setId(GenericPo.createUID());
        fileRefService.insert(tFileRef);
        //插入店铺表
        return ok(id);
    }
    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    @AccessLogger("修改")
    @Authorize(action = "U")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseMessage updateShop(@RequestBody Map<String, Object> map) {
        if (map.get("logo") == null || "".equals(map.get("logo"))) {
            return ResponseMessage.error("店铺logo不能为空", 500);
        }
        if (map.get("businessUrl") == null || "".equals(map.get("businessUrl"))) {
            return ResponseMessage.error("店铺营业执照图片不能为空", 500);
        }
        if (map.get("img") == null || "".equals(map.get("img"))) {
            return ResponseMessage.error("店铺图片不能为空", 500);
        }
        Shop shop = new Shop();
        shop.setId(Long.parseLong(map.get("shopId").toString()));
        shop.setName((String)map.get("shopName"));
        shop.setAddress((String)map.get("address"));
        shop.setLongtitude((String)map.get("longitude"));
        shop.setLatitude((String)map.get("latitude"));
        shop.setContent(map.get("content").toString());
        shop.setUserId(Long.parseLong(map.get("userId").toString()));

        int num = getService().update(shop);
        //资源管理表

        FileRef tFileRef = new FileRef();
        tFileRef.setRefId(Long.parseLong(map.get("businessId").toString()));
        //logo
        tFileRef.setResourceId(Long.parseLong(map.get("logo").toString()));
        tFileRef.setId(Long.parseLong(map.get("logoId").toString()));
        fileRefService.shopUpdateFileRef(tFileRef);
        //营业执照
        tFileRef.setResourceId(Long.parseLong(map.get("businessUrl").toString()));
        tFileRef.setId(Long.parseLong(map.get("businessId").toString()));
        fileRefService.shopUpdateFileRef(tFileRef);
        //店铺图片
        String[] imgs =  map.get("img").toString().split(" ");
        String[] imgsId = map.get("imgsId").toString().split(" ");
        for (int i = 0; i < imgs.length; i++) {
            tFileRef.setResourceId(Long.parseLong(imgs[i]));
            if(i < imgsId.length){
                tFileRef.setId(Long.parseLong(imgsId[i]));
                fileRefService.shopUpdateFileRef(tFileRef);
            }else{
                tFileRef.setType(0);
                tFileRef.setDataType(5);
                tFileRef.setRefId(Long.parseLong(map.get("refId").toString()));
                tFileRef.setId(GenericPo.createUID());
                fileRefService.insert(tFileRef);
            }

        }
        //删除图片
        for(int i = imgsId.length; i > imgs.length; i--){
            fileRefService.delete(new Long(imgsId[i - 1]));
        }
        return ok(num);
    }

    @RequestMapping(value = "/shopInfo/{shopId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage shopInfo(@PathVariable("shopId")String shopId, HttpServletRequest req){
        //查询店铺信息
        Map map = getService().shopInfo(shopId);
        String str = map.get("businessId").toString();
        QueryParam queryParam = new QueryParam();
        queryParam.getParam().put("refId",str);
        queryParam.getParam().put("dataType",5);
        List list = fileRefService.queryByRefId(queryParam);
        map.put("imgs", list);
        queryParam.getParam().put("dataType",1);
        map.put("Businessimgs", fileRefService.queryByRefId(queryParam));
        queryParam.getParam().put("dataType",4);
        map.put("Logoimgs", fileRefService.queryByRefId(queryParam));
        return ok(map);
    }

    @RequestMapping(value = "/img/delete",method = RequestMethod.POST)
    @Authorize(action = "D")
    public ResponseMessage deleteImg(){
        return ok();
    }

    @RequestMapping(value = "/{uId}/enable",method = RequestMethod.PUT)
    @Authorize(action = "C")
    public ResponseMessage shopEnable(@PathVariable("uId")String uId){
        return ok(getService().changeStatus("1",uId));
    }

    @RequestMapping(value = "/{uId}/disable",method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage shopDisable(@PathVariable("uId")String uId){
        return ok(getService().changeStatus("0",uId));
    }
    @RequestMapping(value = "/{deviceId}/deldevice",method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage deldevice(@PathVariable("deviceId")String deviceId){
        return ok(shopDeviceService.deldevice(deviceId));
    }

    @RequestMapping(value = "/adddevice",method = RequestMethod.POST)
    @Authorize(action = "C")
    public ResponseMessage addDevice(@RequestBody ShopDevice shopDevice){
        shopDevice.setId(GenericPo.createUID());
        shopDevice.setCreateTime(new Date());
        shopDevice.setStatus(0);
        return ok(shopDeviceService.insert(shopDevice));
    }
    @RequestMapping(value = "/{shopId}/querybrand",method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryBrand(@PathVariable("shopId") String shopId){
        return ResponseMessage.ok(shopBrandService.queryBrand(shopId));
    }

    /**
     * 添加店铺关联商品
     * @param goodsShopArray
     * @return
     */
    @org.springframework.transaction.annotation.Transactional
    @RequestMapping(value = "/addGoodsShop",method = RequestMethod.POST)
    @Authorize(action = "C")
    public ResponseMessage addGoodsShop(@RequestBody Long [] goodsShopArray){
        GoodsShop goodsShop = new GoodsShop();
        goodsShop.setShopId(goodsShopArray[0]);
        goodsShop.setStatus(0);
        for(int i = 1 ; i < goodsShopArray.length; i ++) {
            //商品店铺关联
            GoodsShop shopGoods = goodsShopService
                    .createQuery()
                    .where(GoodsShop.Property.shopId, goodsShopArray[0])
                    .and(GoodsShop.Property.goodsId, goodsShopArray[i])
                    .single();
            if (shopGoods ==null){
                goodsShop.setId(GenericPo.createUID());
                goodsShop.setGoodsId(goodsShopArray[i]);
                goodsShopService.insert(goodsShop);
            }
        }
        return ok();
    }

    @RequestMapping(value = "/selectAllShopGoods/{shopId}", method = RequestMethod.GET)
    @AccessLogger("查询列表")
    @Authorize(action = "R")
    public ResponseMessage selectAllShopGoods(@PathVariable("shopId") Long shopId, QueryParam param) {
        param.getParam().put("shopId",shopId);
        // 获取条件查询
        Object data;
        data = goodsShopService.selectAllShopGoods(param);
        return ok(data)
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }
    @RequestMapping(value = "/statusUpdate/{shopGoodsId}", method = RequestMethod.PUT)
    @AccessLogger("店铺商品下架")
    @Authorize(action = "U")
    public ResponseMessage statusUpdate(@PathVariable("shopGoodsId") Long shopGoodsId) {
        //商品店铺关联
        Long id = Long.valueOf(0);
        GoodsShop shopGood = goodsShopService.createQuery().where(GoodsShop.Property.id,shopGoodsId).single();
        if(shopGood != null){
            shopGood.setStatus(1);
             id = Long.valueOf(goodsShopService.update(shopGood));
        }
        return ok(id);
    }

    @RequestMapping(value = "/statusUpdate1/{shopGoodsId}", method = RequestMethod.PUT)
    @AccessLogger("店铺商品上架")
    @Authorize(action = "U")
    public ResponseMessage statusUpdate1(@PathVariable("shopGoodsId") Long shopGoodsId) {
        //商品店铺关联
        Long id = Long.valueOf(0);
        GoodsShop shopGood = goodsShopService.createQuery().where(GoodsShop.Property.id,shopGoodsId).single();
        if(shopGood != null){
            shopGood.setStatus(0);
            id = Long.valueOf(goodsShopService.update(shopGood));
        }
        return ok(id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @AccessLogger("删除")
    @Authorize(action = "D")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Shop old = getService().selectByPk(id);
        assertFound(old, "data is not found!");
        old.setStatus(3);
        getService().update(old);
        return ok();
    }

    @RequestMapping(value = "/queryAllShopInfo", method = RequestMethod.GET)
    @AccessLogger("查询全部店铺位置和该店铺下的设备数量")
    @Authorize(action = "R")
    public ResponseMessage queryAllShopInfo(Integer brandId) {
        return ok(getService().queryAllShopElements(brandId));
    }


}

