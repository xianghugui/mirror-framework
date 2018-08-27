package com.base.web.service;

import com.base.web.bean.GoodsComment;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface GoodsCommentService extends GenericService<GoodsComment,Long>{
    PagerResult<Map> queryGoodsComment(Long goodsId, QueryParam queryParam);
    Map queryGoodsCommentByOrderId( HttpServletRequest req, String orderId, String status);

    PagerResult<Map> queryCommentByUserId(QueryParam queryParam);
    int addGoodsComment(GoodsComment goodsComment);

    List<Map> queryGoodsCommentByGoodsId(Long goodsId);
}
