package com.base.web.service;


import com.base.web.bean.GetMoney;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:07  2018/3/27
 */
public interface GetMoneyService extends GenericService<GetMoney, Long> {
    List<Map> showGetMoney();

    //>>插入提现记录
    int insertGetMoney(BigDecimal money,String bank);

    PagerResult<Map> queryGetMoney(QueryParam queryParam);

}
