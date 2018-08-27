package com.base.web.dao;

import com.base.web.bean.RecommendGoods;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RecommendGoodsMapper extends GenericMapper<RecommendGoods, Long> {
    /**
     * 查询推荐商品列表
     * @param  level
     * @param goodsClassIdPrefix
     * @return
     */
    List<Map> queryRecommendGoodsByGoodsClassId ( @Param("level") Integer level,@Param("goodsClassIdPrefix") Integer goodsClassIdPrefix );

    /**
     * 更改商品推荐表的状态
     * @param  uId
     * @param status
     * @return
     */
    int changeStatus(@Param("uId")Long uId,@Param("status")Integer status);

    Long queryByGoodsId(Long goodsId);

    List<Map> queryByGoodsClassId(QueryParam param);

}
