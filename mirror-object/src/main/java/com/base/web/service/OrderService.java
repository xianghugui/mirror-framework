package com.base.web.service;

import com.base.web.bean.Order;
import com.base.web.bean.OrderDetail;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:11  2018/4/9
 */
public interface OrderService extends GenericService<Order, Long> {
    List<Map> showOrder();
   Map showOrderInfo(String orderId);

    //插入订单
    Long insertOrder(OrderDetail[] orderDetail,int insertType);

    Long imageUrl (Long goodsId);

}
