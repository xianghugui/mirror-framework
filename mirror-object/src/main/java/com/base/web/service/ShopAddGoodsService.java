package com.base.web.service;

import com.base.web.bean.ShopAddGoods;
import com.base.web.bean.ShopGoodsPush;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;


public interface ShopAddGoodsService extends GenericService<ShopAddGoods, Long>{

    // 商家端插入商品
    int insertShopAddGoods(ShopAddGoods data);
    int editShopAddGoods(ShopAddGoods data);

    PagerResult<Map> pagerShopGoods(QueryParam param);

    Map selectShopGoodsById(Long uId);

    /**
     * 根据ID查询信息
     * @param goodsId
     * @return Map
     */
    Map queryById(Long goodsId);

    /**
     * 根据店铺ID查询店铺下所有服装类别
     * @param shopId
     * @return
     */
    List<Map> selectClassIdByShopId(Long shopId);
    /**
     * 根据店铺ID查询店铺下所有适用场合类别
     * @param shopId
     * @return
     */
    List<Map> selectApplicationIdByShopId(Long shopId);

    /**
     * 修改状态，0：在售，1：下架，2：删除
     * @param id
     * @param status
     * @return int
     */
    int modifyStatus(Long id, Integer status);

    /**
     * 根据用户提供的类别ID，性别，适合场合查询商品列表
     * @param shopGoodsPush
     * @return List<Map>
     */
    List<Map> selectByShopGoodsPush(ShopGoodsPush shopGoodsPush);

    /**
     * 查询10条活动商品
     * @param shopId
     * @return
     */
    List<Map> queryEventGoods(Long shopId);
}
