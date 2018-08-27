package com.base.web.dao;

import com.base.web.bean.OrderDeal;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 14:07  2018/4/10
 */
public interface OrderDealMapper extends GenericMapper<OrderDeal, Long> {
    Long queryIdByOrderId(String orderId);
}
