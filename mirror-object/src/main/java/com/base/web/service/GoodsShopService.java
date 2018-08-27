package com.base.web.service;

import com.base.web.bean.GoodsShop;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;


import java.util.List;
import java.util.Map;

public interface GoodsShopService extends GenericService<GoodsShop, Long>{


    PagerResult<Map> selectAllShopGoods(QueryParam param);

    /**
     * 根据店铺id查询品牌
     * @param shopId
     * @return
     */
    List<Map> queryBrandByShopId(Long shopId);

    /**
     * 根据店铺id查询类别
     * @param shopId
     * @return
     */
    List<Map> queryClassByShopId(Long shopId);

}
