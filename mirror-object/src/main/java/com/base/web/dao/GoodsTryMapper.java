package com.base.web.dao;

import com.base.web.bean.GoodsTry;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:26  2018/4/8
 */
public interface GoodsTryMapper extends GenericMapper<GoodsTry, Long> {

    //显示试衣订单列表
    List<Map> showTryOrder();

    //查询用户试穿记录
    List<Map> queryGoodsTry(@Param("userId")Long userId, @Param("queryParam")QueryParam queryParam);
    int queryGoodsTryTotal(Long userId);

}
