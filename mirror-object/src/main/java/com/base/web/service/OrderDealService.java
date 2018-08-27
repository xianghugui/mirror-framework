package com.base.web.service;

import com.base.web.bean.OrderDeal;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 14:08  2018/4/10
 */
public interface OrderDealService extends GenericService<OrderDeal, Long> {
    Long queryIdByOrderId(String orderId);
}
