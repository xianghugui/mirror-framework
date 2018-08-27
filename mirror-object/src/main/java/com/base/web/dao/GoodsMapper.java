package com.base.web.dao;

import com.base.web.bean.Goods;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsMapper extends GenericMapper<Goods,Long>{
    List<Goods> queryGoodsByClassId(QueryParam param);

    //查询当前类别下商品总数
    Integer queryGoodsTotalByClassId(QueryParam param);

    Goods queryGoodsById(Long id);
    int insertGoods(QueryParam queryParam);

    /**
     * 分页查询所有商品数据
     * @param queryParam
     * @return
     */
    List<Map> allGoods(QueryParam queryParam);
    Map queryGoodsImgSrcById(String goodsId);

    // >>根据类别查询商品和分别按最新 0，销量 1，价格 2排序

    List<Map> queryGoods(QueryParam param);

    /**
     * 分页查询所有有秀的商品
     * @param param
     * @return
     */
    List<Map> queryfittingShow(QueryParam param);

    /**
     * 查询所有有秀的商品总条数
     * @param param
     * @return
     */
    Integer queryfittingShowTotal(QueryParam param);

    // >>根据id查询商品，扩展销售量
    Map queryGoodsAndSales(Long uId);

    Integer queryAllGoodsByGoodsClassId(QueryParam param);

    Integer queryAgent(@Param("goodsId") Long goodsId, @Param("userId")Long userId);

    /**
     * 分页查询视频关联店铺商品
     * @param queryParam
     * @return
     */
    List<Map> queryVideoAssociationGoods(QueryParam queryParam);

    /**
     * 查询视频关联店铺商品总数
     * @param queryParam
     * @return
     */
    Integer queryVideoAssociationGoodsTotal(QueryParam queryParam);

}
