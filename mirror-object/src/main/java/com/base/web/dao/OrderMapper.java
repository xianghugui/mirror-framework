package com.base.web.dao;

import com.base.web.bean.Order;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:09  2018/4/9
 */
public interface OrderMapper extends GenericMapper<Order, Long> {

    List<Map> showOrder();
    Map showOrderInfo(String orderId);

    Long imageUrl (Long goodsId);
}
