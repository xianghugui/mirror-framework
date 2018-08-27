package com.base.web.service.impl;

import com.base.web.bean.RecommendGoods;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.RecommendGoodsMapper;
import com.base.web.service.RecommendGoodsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("RecommendGoodsService")
public class RecommendGoodsServiceImpl extends AbstractServiceImpl<RecommendGoods,Long> implements RecommendGoodsService{

    @Resource
    private RecommendGoodsMapper RecommendGoodsMapper;

    @Override
    protected RecommendGoodsMapper getMapper() {
        return this.RecommendGoodsMapper;
    }

    @Override
    public List<Map> queryRecommendGoodsByGoodsClassId(Integer level,Integer goodsClassIdPrefix) {
        return getMapper().queryRecommendGoodsByGoodsClassId(level,goodsClassIdPrefix);
    }

    @Override
    public int changeStatus(Long uId,Integer status){
        return getMapper().changeStatus(uId,status);
    }

    @Override
    public Long queryByGoodsId(Long goodsId){
        return getMapper().queryByGoodsId(goodsId);
    }

    @Override
    public PagerResult<Map> queryByGoodsClassId(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(false);
        int total = getMapper().total(param);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryByGoodsClassId(param));
        }
        return pagerResult;
    }
}
