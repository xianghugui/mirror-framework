package com.base.web.service;

import com.base.web.bean.OrderDetail;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:11  2018/4/9
 */
public interface OrderDetailService extends GenericService<OrderDetail, Long>{
    PagerResult showOrders(QueryParam param, HttpServletRequest req);
    Map showOrderInfo(String orderId);
    Map showAddress(String orderId);

    void insertOrderDetail(OrderDetail orderDetail, Map map);

    PagerResult showTrading(QueryParam param);

    //客户端我的购物显示所有订单
    PagerResult showClientOrders(QueryParam param, HttpServletRequest req);
    //客户端我的购物根据状态显示订单
    PagerResult showClientTrading(QueryParam param, HttpServletRequest req);
    //修改订单状态
    int changeStatus(Long orderId, Integer status);
    //根据订单ID查询店铺ID
    Map queryShopIdByOrderId(Long orderId);

    //>>查询用户消费中的订单详情
    PagerResult<Map> queryOrderConsume(QueryParam queryParam);

    /**
     * 根据主订单id修改状态
     * @param orderDetail
     * @return
     */
    int updateStatusByOrderId(OrderDetail orderDetail);

    /**
     * 订单完成返现/分润
     * @param orderId
     */
    void orderProfit(Long orderId);

}
