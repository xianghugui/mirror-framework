package com.base.web.service;

import com.base.web.bean.StatisticalVice;

import java.util.List;
import java.util.Map;

public interface StatisticalViceService extends GenericService<StatisticalVice, Long>{
    List<Map> queryWeekShopSalce(Long brandId);

    List<Map> queryMonthShopSalce(Long brandId);

    List<Map> queryQuarterShopSalce(Long brandId);

    List<Map> queryYearSales(Long brandId);
}
