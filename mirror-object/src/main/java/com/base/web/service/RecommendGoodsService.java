package com.base.web.service;

import com.base.web.bean.RecommendGoods;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface RecommendGoodsService extends GenericService<RecommendGoods,Long> {

    List<Map> queryRecommendGoodsByGoodsClassId ( Integer level,Integer goodsClassIdPrefix);

    int changeStatus(Long uId,Integer status);

    Long queryByGoodsId(Long goodsId);

    PagerResult<Map> queryByGoodsClassId (QueryParam param);
}
