package com.base.web.dao;

import com.base.web.bean.TryInfo;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:15  2018/4/8
 */
public interface TryInfoMapper extends GenericMapper<TryInfo, Long> {
    List<Map> showTryInfo(String tryId);
    List<Map> showTryOrders(QueryParam param);
    int queryTryOrdersTotal(QueryParam param);
    Map showTryOrderInfo(String tryInfoId);
    Map showAddress(String tryInfoId);
    int delivery(@Param("tryInfoId") String tryInfoId, @Param("expressId") String expressId,
                 @Param("expressNumber") String expressNumber);
    List<Map> showTrading(QueryParam param);
    int showTradingTotal(QueryParam param);

    //客户端主页我的试衣
    List<Map> clientShowOrders(QueryParam param);
    int clientShowOrdersTotal(QueryParam param);
    int changeStatus(@Param("orderId") Long orderId,@Param("status") Integer status);
    int submitOrder(@Param("orderId")Long orderId, @Param("price")BigDecimal price);
    BigDecimal queryPriceByTryOrderId(Long orderId);

    //>>根据试穿订单ID查询订单详情
    List<Map> queryTryByTryId(Long tryId);

    //>>查询用户消费中的订单详情
    List<Map> queryTryInfo(@Param("userId") Long userId,@Param("queryParam")QueryParam queryParam);

    //>>查询用户试穿订单消费total
    Integer queryTryInfoTotal(Long userId);
}
