package com.base.web.service;

import com.base.web.bean.PageViewStatisticalMain;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface PageViewStatisticalMainService extends GenericService<PageViewStatisticalMain, Long>{
    public List<Map> queryPageView(QueryParam param);
}
