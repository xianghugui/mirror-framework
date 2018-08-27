package com.base.web.dao;

import com.base.web.bean.StatisticalVice;

import java.util.List;
import java.util.Map;

public interface StatisticalViceMapper extends GenericMapper<StatisticalVice, Long>{

    /**
     * 查询某品牌各门店上周的商品销量
     * @param brandId
     * @return
     */
    List<Map> queryWeekShopSalce(Long brandId);

    /**
     * 查询某品牌各门店上月的商品销量
     * @param brandId
     * @return
     */
    List<Map> queryMonthShopSalce(Long brandId);

    /**
     * 查询某品牌各门店上季度的商品销量
     * @param brandId
     * @return
     */
    List<Map> queryQuarterShopSalce(Long brandId);

    /**
     * 查询某品牌各门店上季度的商品销量
     * @param brandId
     * @return
     */
    List<Map> queryYearSales(Long brandId);
}
