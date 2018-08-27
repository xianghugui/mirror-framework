package com.base.web.service;

import com.base.web.bean.Goods;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.Map;

public interface GoodsService extends GenericService<Goods, Long>{

    //根据类别分页查询商品
    PagerResult<Goods> queryGoodsByClassId(QueryParam param);

    //根据id查询商品
    Goods queryGoodsById(Long id);

    void enableUser(Long id) ;

    void disableUser(Long id) ;

    PagerResult<Map> allGoods(QueryParam param);

    Map queryGoodsImgSrcById(String goodsId);

    // >> 插入商品
    int insertGoods(Goods data);

    // >> 更新商品
    int updateGoods(Goods data);

    //根据类别查询商品和分别按最新 0，销量 1，价格排序 2
    PagerResult<Map> queryGoods(QueryParam param);

    //根据id查询商品，扩展销售量
    Map queryGoodsAndSales(Long uId,Long userId);

    PagerResult<Map> queryVideoAssociationGoods(QueryParam param);

}
