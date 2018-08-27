package com.base.web.service;

import com.base.web.bean.StatisticalMain;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface StatisticalMainService extends GenericService<StatisticalMain, Long>{

    public List<Map> queryWeek(QueryParam param);
}
