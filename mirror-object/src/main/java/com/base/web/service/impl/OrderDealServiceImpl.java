package com.base.web.service.impl;

import com.base.web.bean.OrderDeal;
import com.base.web.dao.OrderDealMapper;
import com.base.web.service.OrderDealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 14:09  2018/4/10
 */
@Service("OrderDealService")
public class OrderDealServiceImpl extends AbstractServiceImpl<OrderDeal, Long> implements OrderDealService {

    @Resource
    private OrderDealMapper orderDealMapper;


    @Override
    public Long queryIdByOrderId(String orderId) {
        return getMapper().queryIdByOrderId(orderId);
    }

    @Override
    protected OrderDealMapper getMapper() {
        return this.orderDealMapper;
    }
}
