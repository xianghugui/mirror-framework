package com.base.web.dao;

import com.base.web.bean.StatisticalMain;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface StatisticalMainMapper  extends GenericMapper<StatisticalMain, Long>{

    /**
     * 按周查询全部品牌销量
     * @param param
     * @return
     */
    List<Map> queryWeek(QueryParam param);

    /**
     * 按周查询某品牌下全部门店销量
     * @param param
     * @return
     */
    List<Map> queryWeekByBrandId(QueryParam param);

    /**
     * 查询某门店下服装销量
     * @param param
     * @return
     */
    List<Map> statisticalByShopId(QueryParam param);
}
