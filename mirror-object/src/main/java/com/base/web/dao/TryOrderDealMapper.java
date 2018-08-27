package com.base.web.dao;

import com.base.web.bean.TryOrderDeal;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:52  2018/4/9
 */
public interface TryOrderDealMapper extends GenericMapper<TryOrderDeal, Long> {
    Long queryIdByOrderId(String orderId);
}
