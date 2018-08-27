package com.base.web.dao;

import com.base.web.bean.ShoppingCart;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface ShoppingCartMapper extends GenericMapper<ShoppingCart, Long>{
    //查询购物车详情
    List<Map> queryShoppingCart(Long userId);

    // 查询同样的商品，返回购买数量
    Map queryShoppingNum(QueryParam param);

    int updateGoodsNum(QueryParam param);
}
