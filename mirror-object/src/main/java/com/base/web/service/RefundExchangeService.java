package com.base.web.service;

import com.base.web.bean.RefundExchange;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface RefundExchangeService extends GenericService<RefundExchange, Long> {
    PagerResult showRefunds(QueryParam param, HttpServletRequest req);
    PagerResult showOrderRefunds(QueryParam param, HttpServletRequest req);
    PagerResult showTryGoodsRefunds(QueryParam param, HttpServletRequest req);
    PagerResult showVideoOrderRefunds(QueryParam param, HttpServletRequest req);
    Map showRefundsInfo(QueryParam param);
    int agreeRefunds(String refundId);
    int refuseRefunds(String refundId, Long imageId, String refuseReason);
    int addRefundExchange(RefundExchange refundExchange);
    //客户端退货退款
    PagerResult clientShowRefunds(QueryParam param, HttpServletRequest req);
    //查询退货地址
    Map showClientRefundAddress(Long orderId);
    //上传快递信息
    int delivery(Long orderId, String expressId, String expressNumber);
    List<Map> refund ();
}
