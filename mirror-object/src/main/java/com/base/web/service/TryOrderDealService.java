package com.base.web.service;

import com.base.web.bean.TryOrderDeal;
import org.springframework.stereotype.Service;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:53  2018/4/9
 */
public interface TryOrderDealService extends GenericService<TryOrderDeal, Long> {

    Long queryIdByOrderId(String orderId);
}
