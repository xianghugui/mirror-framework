package com.base.web.dao;

import com.base.web.bean.GoodsComment;
import com.base.web.bean.common.QueryParam;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GoodsCommentMapper extends GenericMapper<GoodsComment,Long>{
    List<Map> queryGoodsComment(@Param("goodsId") Long goodsId,@Param("queryParam")QueryParam queryParam);

    Integer queryAllByGoodsId(Long goodsId);

    Map queryGoodsCommentByOrderId(@Param("orderId")String orderId, @Param("status")String status);

    List<Map> queryCommentByUserId(QueryParam param);

    List<Map> queryGoodsCommentByGoodsId(Long goodsId);

    Integer queryCommentByUserIdTotal(Long userId);
}
