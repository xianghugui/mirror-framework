package com.base.web.service;

import com.base.web.bean.TryInfo;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:17  2018/4/8
 */
public interface TryInfoService extends GenericService<TryInfo, Long> {
    List<Map> showTryInfo(String tryId);
    PagerResult showTryOrders(QueryParam param, HttpServletRequest req);
    Map showTryOrderInfo(String tryInfoId);
    Map showAddress(String tryInfoId);
    int delivery(String tryInfoId, String expressId, String expressNumber);

    int insertTryInfo(TryInfo tryInfo, Long tryId);

    PagerResult showTrading(QueryParam param);

    //客户端主页我的试衣
    PagerResult clientShowOrders(QueryParam param, HttpServletRequest req);
    //修改状态
    int changeStatus(Long orderId, Integer status);
    //提交订单
    int submitOrder(Long orderId);

    //>>根据试穿订单ID查询订单详情
    List<Map> queryTryByTryId(Long tryId);

    //>>查询用户消费中的试穿订单详情
    PagerResult<Map> queryTryInfo(QueryParam queryParam);

}
