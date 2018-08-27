package com.base.web.service;

import com.base.web.bean.OrderProfit;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

/**
* @Author: FQ
* @Date: 2018/4/24 10:44
*/
public interface OrderProfitService  extends GenericService<OrderProfit, Long>{

    //>>查询用户分润 0:试衣购买，1：平台购买
    PagerResult<Map> queryOrderProfit(Integer type, QueryParam queryParam);
    /**
     * 根据当前用户ID，和邀请用户ID，查询分润列表
     * @param userId
     * @param orderUserId
     * @return
     */
    List<OrderProfit> queryOrderProfitListByUserId(Long userId, Long orderUserId);

    /**
     * 查询交易记录（userId）
     * @param queryParam
     * @return
     */
    PagerResult<Map> transactionRecord(QueryParam queryParam);
}
