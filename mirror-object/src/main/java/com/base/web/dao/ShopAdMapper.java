package com.base.web.dao;

import com.base.web.bean.ShopAd;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 9:51  2018/3/28
 */
public interface ShopAdMapper extends GenericMapper<ShopAd, Long> {
  int cancelPush(Long adId);

  /**
   * 商家端分页查询资讯
   * @param
   * @return
   */
  List<Map> queryShopAd(QueryParam param);

  Integer queryShopAdTotal(QueryParam param);

  /**
   * 用户端分页查询资讯
   * @param
   * @return
   */
  List<Map> queryUserAd(QueryParam param);

  Integer queryUserAdTotal(QueryParam param);

  /**
   * 查询广告详情
   * @param shopAdId
   * @return
   */
  Map queryShopAdInfo(Long shopAdId);
//  查询用户端咨询是查询当前用户是否在平台购买过商品
  List<Map> queryUserOrdersTotal(QueryParam param);

  List<Map> queryUserAdNotBuy(QueryParam param);

  Integer queryUserAdNotBuyTotal();

}
