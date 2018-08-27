package com.base.web.service;

import com.base.web.bean.ShopBrand;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:22  2018/4/4
 */
public interface ShopBrandService extends GenericService<ShopBrand, Long> {
    List<Map> queryBrand(String shopId);

    List<Map> queryAllShopByBrandId(String brandId);

    List<Map> queryShopBrand();

}
