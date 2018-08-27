package com.base.web.service;


import com.alibaba.fastjson.JSONObject;
import com.base.web.bean.UserAgent;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:07  2018/3/27
 */
public interface UserAgentService extends GenericService<UserAgent, Long> {

    PagerResult<Map> userAgentGoodsListPagerByUserId(QueryParam queryParam , HttpServletRequest req);

    Map userAgentGoodsSpread (Long goodsId, QueryParam queryParam , HttpServletRequest req);

    // 插入平台服装代理表
    int insertAgent(JSONObject goods);


}
