package com.base.web.service;

import com.base.web.bean.GoodsTry;
import com.base.web.bean.TryInfo;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:48  2018/4/8
 */
public interface GoodsTryService extends GenericService<GoodsTry, Long> {
    //显示试衣订单列表
    List<Map> showTryOrder();

    //插入试衣订单记录
    Long insertGoodsTry(TryInfo[] tryInfo);

    //查询用户试穿记录
    PagerResult<Map> queryGoodsTry(QueryParam queryParam);
}
