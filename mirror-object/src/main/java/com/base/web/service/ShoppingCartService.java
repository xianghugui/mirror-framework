package com.base.web.service;

import com.base.web.bean.ShoppingCart;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface ShoppingCartService extends GenericService<ShoppingCart,Long>{

    //查询购物车订单
    List<Map> queryShoppingCart();

    int updateGoodsNum(QueryParam param);
}
