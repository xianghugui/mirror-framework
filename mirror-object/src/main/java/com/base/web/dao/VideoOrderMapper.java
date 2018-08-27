package com.base.web.dao;

import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:19  2018/4/10
 */
public interface VideoOrderMapper extends GenericMapper<VideoOrder, Long> {
    List<Map> showVideoOrder();
    List<Map> showVideoOrders(QueryParam param);
    int queryVideoOrdersTotal(QueryParam param);
    Map showVideoOrderInfo(String videoOrderId);
    Map showAddress(String videoOrderId);
    int delivery(@Param("videoOrderId") String videoOrderId, @Param("expressId") String expressId,
                 @Param("expressNumber") String expressNumber);
    int offer(@Param("videoOrderId")String videoOrderId, @Param("price")String price);
    int lack(@Param("videoOrderId")String videoOrderId, @Param("lackReason")String lackReason);
    List<Map> showTrading(QueryParam param);
    int showTradingTotal(QueryParam param);

    //客户端主页我的询价
    List<Map> clientShowOrders(QueryParam param);
    int clientShowOrdersTotal(QueryParam param);
    //修改状态
    int changeStatus(@Param("videoOrderId")Long videoOrderId, @Param("status")Integer status);
}
