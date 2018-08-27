package com.base.web.service;

import com.base.web.bean.ShopAd;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface ShopAdService extends GenericService<ShopAd,Long> {
    Long insertShopAd(ShopAd area);

    int cancelPush(Long adId);

    PagerResult<Map> queryShopAd(QueryParam param);

    PagerResult<Map> queryUserAd(QueryParam param);

    Map queryShopAdInfo(Long shopAdId);
}
