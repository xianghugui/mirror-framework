package com.base.web.dao;

import com.base.web.bean.PageViewGoods;

import java.util.List;
import java.util.Map;

public interface PageViewGoodsMapper extends GenericMapper<PageViewGoods, Long>{

    /**
     * 查询每周试衣秀服装的商品销量和浏览量
     * @return
     */
    List<Map> queryWeeklyPageView();

    /**
     * 查询每月
     * @return
     */
    List<Map> queryMonthlyPageView();

    /**
     * 查询每季度
     * @return
     */
    List<Map> queryQuarterlyPageView();

    /**
     * 查询每年
     * @return
     */
    List<Map> queryYearlyPageView();

    /**
     * 批量插入每周试衣秀服装的商品销量和浏览量
     * @param insertList
     * @return
     */
    int insertPageViewForGoods(List<Map> insertList);
}
