package com.base.web.service.impl;

import com.base.web.bean.Order;
import com.base.web.bean.OrderDetail;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.OrderMapper;
import com.base.web.service.OrderDetailService;
import com.base.web.service.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("OrderService")
public class OrderServiceImpl extends AbstractServiceImpl<Order, Long> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private OrderDetailService orderDetailService;

    @Override
    protected OrderMapper getMapper() {
        return this.orderMapper;
    }

    @Override
    public List<Map> showOrder() {
        return getMapper().showOrder();
    }

    @Override
    public Map showOrderInfo(String orderId) {
        return getMapper().showOrderInfo(orderId);
    }

    @Override
    public Long insertOrder(OrderDetail[] orderDetail, int insertType) {
        Map map = new HashMap();
        map.put("type", 0);
        //直接支付
        if (insertType == 0) {
            Order order = new Order();
            order.setId(GenericPo.createUID());
            order.setUserId(WebUtil.getLoginUser().getId());
            order.setAddressId(orderDetail[0].getAddressId());
            order.setNum(orderDetail.length);
            order.setOrderStatus(0);
            order.setTotalPrice(orderDetail[0].getPriceSum());
            order.setCreateTime(new Date());
            getMapper().insert(InsertParam.build(order));
            for (int i = 0; i < orderDetail.length; i++) {
                orderDetail[i].setOrderId(order.getId());
                orderDetailService.insertOrderDetail(orderDetail[i], map);
            }
            return order.getId();
        }
        //取消支付
        for (int i = 0; i < orderDetail.length; i++) {
            orderDetailService.insertOrderDetail(orderDetail[i], map);
        }

        return 0L;
    }

    @Override
    public Long imageUrl(Long goodsId) {
        return getMapper().imageUrl(goodsId);
    }


}
