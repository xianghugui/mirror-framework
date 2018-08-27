package com.base.web.service;

import com.base.web.bean.VideoOrder;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:21  2018/4/10
 */
public interface VideoOrderService extends GenericService<VideoOrder, Long> {
    List<Map> showVideoOrder(HttpServletRequest req);
    PagerResult showVideoOrders(QueryParam param, HttpServletRequest req);
    Map showVideoOrderInfo(String videoOrderId);
    Map showAddress(String videoOrderId);
    int delivery(String videoOrderId, String expressId, String expressNumber);
    int offer(String videoOrderId, String price);
    int lack(String videoOrderId, String lackReason);
    int changeStatus(Long videoOrderId, Integer status);
    PagerResult showTrading(QueryParam param);
    //客户端主页我的询价
    PagerResult clientShowOrders(QueryParam param, HttpServletRequest req);
}
