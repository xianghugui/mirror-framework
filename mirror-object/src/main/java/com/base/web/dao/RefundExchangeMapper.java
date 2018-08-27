package com.base.web.dao;

import com.base.web.bean.RefundExchange;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface RefundExchangeMapper extends GenericMapper<RefundExchange, Long> {
    List<Map> showRefunds(QueryParam param);

    int showRefundsTotal(QueryParam param);

    List<Map> showOrderRefunds(QueryParam param);

    int showOrderRefundsTotal(QueryParam param);

    List<Map> showTryGoodsRefunds(QueryParam param);

    int showTryGoodsRefundsTotal(QueryParam param);

    List<Map> showVideoOrderRefunds(QueryParam param);

    int showVideoOrderRefundsTotal(QueryParam param);

    /**
     * 平台退款
     *
     * @return
     */
    List<Map> refund();

    /**
     * 购买订单退款详情
     * @return
     */
    Map showOrderRefundsInfo(String refundId);

    /**
     * 询价订单退款详情
     * @return
     */
    Map showVideoRefundsInfo(String refundId);

    int agreeRefunds(@Param("dealTime") Date dealTime, @Param("refundId") String refundId);

    int refuseRefunds(@Param("dealTime") Date dealTime, @Param("refundId") String refundId,
                      @Param("imageId") Long imageId, @Param("refuseReason") String refuseReason);

    //购物退款
    List<Map> clientShowOrderRefunds(QueryParam param);

    int clientShowOrderRefundsTotal(QueryParam param);

    //询价退款
    List<Map> clientShowVideoOrderRefunds(QueryParam param);

    int clientShowVideoOrderRefundsTotal(QueryParam param);

    //查询退货地址
    Map showClientRefundAddress(Long orderId);

    //上传快递信息
    int delivery(@Param("orderId") Long orderId, @Param("expressId") String expressId, @Param("expressNumber") String expressNumber);
}
