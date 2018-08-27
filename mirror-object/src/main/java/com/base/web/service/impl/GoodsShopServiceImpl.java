package com.base.web.service.impl;

import com.base.web.bean.GoodsShop;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.GoodsShopMapper;
import com.base.web.service.GoodsShopService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("GoodsShopService")
public class GoodsShopServiceImpl extends AbstractServiceImpl<GoodsShop,Long> implements GoodsShopService {
    @Resource
    private GoodsShopMapper GoodsShopMapper;

    @Override
    protected GoodsShopMapper getMapper(){
        return this.GoodsShopMapper;
    }


    @Override
    public PagerResult<Map> selectAllShopGoods(QueryParam param) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(false);
        int total = getMapper().total(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
            param.rePaging(total);
            pagerResult.setData(getMapper().selectAllShopGoods(param));
        }
        return pagerResult;
    }

    @Override
    public List<Map> queryBrandByShopId(Long shopId) {
        return getMapper().queryBrandByShopId(shopId);
    }

    @Override
    public List<Map> queryClassByShopId(Long shopId) {
        return getMapper().queryClassByShopId(shopId);
    }

}
