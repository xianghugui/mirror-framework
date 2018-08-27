package com.base.web.dao;

import com.base.web.bean.GetMoney;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 13:59  2018/3/27
 */
public interface GetMoneyMapper extends GenericMapper<GetMoney,Long>{
    List<Map> showGetMoney();

    //查询提现记录
    List<Map> queryGetMoney(@Param("userId") Long userId,@Param("queryParam") QueryParam queryParam);

    //>>提现记录总数
    Integer queryGetMoneyTotal(Long userId);
}
