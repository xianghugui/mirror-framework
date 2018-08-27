package com.base.web.dao;

import com.base.web.bean.OrderDetail;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:10  2018/4/9
 */
public interface OrderDetailMapper extends GenericMapper<OrderDetail, Long> {
    List<Map> showOrders(QueryParam param);
    int queryOrdersTotal(QueryParam param);
    Map showOrderInfo(String orderId);
    Map showAddress(String orderId);
    List<Map> showTrading(QueryParam param);
    int showTradingTotal(QueryParam param);

    //客户端我的购物
    List<Map> showClientOrders(QueryParam param);
    int showClientOrdersTotal(QueryParam param);
    List<Map> showClientTrading(QueryParam param);
    int showClientTradingTotal(QueryParam param);
    int changeStatus(@Param("orderId") Long orderId,@Param("status") Integer status);
    Map queryShopIdByOrderId(Long orderId);

    //>>查询用户订单消费
    List<Map> queryOrderConsume(@Param("userId")Long userId,@Param("queryParam")QueryParam queryParam);

    //>>查询用户订单消费total
    Integer queryOrderConsumeTotal(Long userId);

    /**
     * 根据主订单id修改状态
     * @param orderDetail
     * @return
     */
    int updateStatusByOrderId(OrderDetail orderDetail);
}
