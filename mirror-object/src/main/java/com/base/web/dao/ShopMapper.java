package com.base.web.dao;

import com.base.web.bean.Area;
import com.base.web.bean.Shop;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface ShopMapper extends GenericMapper<Shop, Long> {
    //根据区域ID查询店铺信息
    List<Map> selectByAreaId(@Param("areaId") Integer areaId);

    //根据shopId查询店铺信息
    Map shopInfo(@Param("shopId") String shopId);

    //启用和禁用店铺
    int changeStatus(@Param("status") String status, @Param("uId") String uId);

    List<Map> queryShopBylngLat(@Param("longtitude") String longtitude, @Param("latitude") String latitude,
                                @Param("goodsId") String goodsId);

    List<Area> queryShopByAreaId();

    List<Long> queryShopByParentId(Long parentId);

    Long queryShopIdByUserId(Long userId);
    Map showShopUserInfo(Long shopId);

    /***
     * 查询全部店铺位置和该店铺下设备数量
     * @return
     */
    List<Map> queryAllShopElements(@Param("brandId")Integer brandId);
}
