package com.base.web.dao;

import com.base.web.bean.OrderProfit;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/** 
* @Author: FQ
* @Date: 2018/4/24 10:45
*/ 
public interface OrderProfitMapper extends GenericMapper<OrderProfit, Long>{

    //>>查询用户分润 0:试衣购买，1：平台购买
    List<Map> queryOrderProfit(@Param("userId") Long userId, @Param("type") Integer type, @Param("queryParam")QueryParam queryParam);

    /**
     * 根据当前用户ID，和邀请用户ID，查询分润列表
     * @param userId
     * @param orderUserId
     * @return
     */
    List<OrderProfit> queryOrderProfitListByUserId(@Param("userId")Long userId, @Param("orderUserId") Long orderUserId);

    /**
     * 查询交易记录（userId）
     * @param queryParam
     * @return
     */
    List<Map> transactionRecord(QueryParam queryParam);

    /**
     * 查询交易记录条数（userId）
     * @param queryParam
     * @return
     */
    int transactionRecordTotal(QueryParam queryParam);
}
