package com.base.web.service;

import com.base.web.bean.ShopGoodsPush;

import java.util.List;
import java.util.Map;

public interface ShopGoodsPushService extends GenericService<ShopGoodsPush, Long> {
    Map selectOrderByTimePickOne(Long userId, Long shopId);

    List<Map> queryShopUser(Long shopId);
    List<Map> queryRecommendUserList( Long phone,  Long shopId);
}
