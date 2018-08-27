package com.base.web.dao;

import com.base.web.bean.GoodsShop;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface GoodsShopMapper extends GenericMapper<GoodsShop,Long>{
    /**
     * 根据店铺id分页查询当前店铺在平台有卖的商品
     * @param param
     * @return
     */
    List<Map> selectAllShopGoods(QueryParam param);

    Long selectShopIdByGoodsId(Long goodsId);

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
