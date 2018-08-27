package com.base.web.service;

import com.base.web.bean.Shop;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface ShopService extends GenericService<Shop,Long> {
    //根据区域ID查询店铺信息
    List<Map> selectByAreaId(Integer areaId);

    //根据shopId查询店铺信息
    Map shopInfo(String shopId);

    //启用和禁用店铺
    int changeStatus(String status, String uId);

    //根据经纬度查询最近的五个店铺
    List<Map> queryShopBylngLat(String longtitude, String latitude, String goodsId);

    List<Map> queryAroundShop(String longtitude, String latitude, String goodsId,HttpServletRequest req);

    //根据用户ID查询店铺ID
    Long queryShopIdByUserId(Long userId);

    //商家端 我的页面 显示信息
    Map showShopUserInfo(Long shopId, HttpServletRequest req);

    List<Map> queryAllShopElements(Integer brandId);

}
