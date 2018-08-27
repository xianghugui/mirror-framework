package com.base.web.dao;

import com.base.web.bean.PageViewStatisticalVice;

import java.util.List;
import java.util.Map;

public interface PageViewStatisticalViceMapper extends GenericMapper<PageViewStatisticalVice, Long>{

    /**
     * 查询某品牌每周各门店的商品销量和浏览量
     * @param brandId
     * @return
     */
    List<Map> queryPageViewAndSales(Long brandId);

    /**
     * 查询某品牌各门店上月的商品销量
     * @return
     */
    List<Map> queryMonthPageViewAndSales();

    /**
     * 查询某品牌各门店上季度的商品销量
     * @return
     */
    List<Map> queryQuarterPageViewAndSales();

    /**
     * 查询某品牌各门店上季度的商品销量
     * @return
     */
    List<Map> queryYearPageViewAndSales();

    int insertPageViewForShop(List<Map> insertList);
}
