package com.base.web.service.impl;

import com.base.web.bean.Shop;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.ShopMapper;
import com.base.web.service.ShopService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.ResourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("ShopService")
public class ShopServiceImpl extends AbstractServiceImpl<Shop, Long> implements ShopService{

    @Resource
    private ShopMapper shopMapper;

    @Resource
    FileRefService fileRefService;

    //根据区域ID查询店铺信息
    @Override
    public List<Map> selectByAreaId(Integer areaId) {
        return getMapper().selectByAreaId(areaId);
    }

    @Override
    public Map shopInfo(String shopId) {
        return getMapper().shopInfo(shopId);
    }

    @Override
    public int changeStatus(String status, String uId) {
        return getMapper().changeStatus(status,uId);
    }

    @Override
    public List<Map> queryShopBylngLat(String longtitude, String latitude, String goodsId){
        return getMapper().queryShopBylngLat(longtitude,latitude,goodsId);
    }

    @Override
    public List<Map> queryAroundShop(String longtitude, String latitude, String goodsId,HttpServletRequest req) {
        List<Map> shopList = getMapper().queryShopBylngLat(longtitude, latitude, goodsId);
        for(Map map:shopList){
            FileRef fileRef = fileRefService.createQuery().where("ref_id",map.get("businessId")).and("data_type",4).single();
            map.put("imageUrl",ResourceUtil.resourceBuildPath(req,fileRef.getResourceId().toString().trim()));
            map.put("distance",getDistance(latitude,longtitude,
                    map.get("latitude").toString(),map.get("longtitude").toString()));
        }
        return shopList;
    }

    //两点经纬度计算距离（KM）
    public String getDistance(String la1, String lo1, String la2, String lo2) {
        double La1 = Double.parseDouble(la1) * Math.PI / 180.0;
        double La2 = Double.parseDouble(la2) * Math.PI / 180.0;
        double La3 = La1 - La2;
        double Lb3 = Double.parseDouble(lo1) * Math.PI / 180.0 - Double.parseDouble(lo2) * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(La3 / 2), 2) + Math.cos(La1) * Math.cos(La2) * Math.pow(Math.sin(Lb3 / 2), 2)));
        s = s * 6378.137;//地球半径
        DecimalFormat df = new DecimalFormat("0.00");
        String distance = df.format(s);
        return distance;
    }

    @Override
    public Long queryShopIdByUserId(Long userId) {
        return getMapper().queryShopIdByUserId(userId);
    }

    @Override
    protected ShopMapper getMapper() {
        return this.shopMapper;
    }

    @Override
    public Map showShopUserInfo(Long shopId, HttpServletRequest req) {
        Map map = getMapper().showShopUserInfo(shopId);
        StringBuffer sb = new StringBuffer();
        sb.append(WebUtil.getBasePath(req)).append("file/image/").append(map.get("imageUrl")).append(".jpg");
        map.put("imageUrl", sb);
        return map;
    }

    @Override
    public List<Map> queryAllShopElements(Integer brandId){
        return getMapper().queryAllShopElements(brandId);
    }

}
