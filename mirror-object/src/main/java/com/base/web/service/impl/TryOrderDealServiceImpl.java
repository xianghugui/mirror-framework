package com.base.web.service.impl;

import com.base.web.bean.TryOrderDeal;
import com.base.web.dao.TryOrderDealMapper;
import com.base.web.service.TryOrderDealService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:53  2018/4/9
 */
@Service("TryOrderDealService")
public class TryOrderDealServiceImpl extends AbstractServiceImpl<TryOrderDeal, Long> implements TryOrderDealService {

    @Resource
    private TryOrderDealMapper tryOrderDealMapper;

    @Override
    protected TryOrderDealMapper getMapper() {
        return this.tryOrderDealMapper;
    }

    @Override
    public Long queryIdByOrderId(String orderId) {
        return getMapper().queryIdByOrderId(orderId);
    }
}
