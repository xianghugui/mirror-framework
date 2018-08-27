package com.base.web.dao;

import com.base.web.bean.ShopBrand;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:21  2018/4/4
 */
public interface ShopBrandMapper extends GenericMapper<ShopBrand, Long> {

    List<Map> queryBrand(String shopId);

    /**
     * 查询某品牌全部门店
     * @param brandId
     * @return
     */
    List<Map> queryAllShopByBrandId(String brandId);

    /**
     * 查询有关联店铺的品牌
     * @return
     */
    List<Map> queryShopBrand();
}
