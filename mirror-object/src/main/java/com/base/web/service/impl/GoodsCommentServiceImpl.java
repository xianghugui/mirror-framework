package com.base.web.service.impl;

import com.base.web.bean.GoodsComment;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.GoodsCommentMapper;
import com.base.web.service.GoodsCommentService;
import com.base.web.service.resource.FileRefService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("goodsCommentService")
public class GoodsCommentServiceImpl extends AbstractServiceImpl<GoodsComment, Long> implements GoodsCommentService {

    @Resource
    private GoodsCommentMapper goodsCommentMapper;

    @Resource
    private FileRefService fileRefService;

    @Override
    public GoodsCommentMapper getMapper() {
        return this.goodsCommentMapper;
    }

    @Override
    public PagerResult<Map> queryGoodsComment(Long goodsId, QueryParam queryParam) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().queryAllByGoodsId(goodsId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryGoodsComment(goodsId, queryParam));
        }
        return pagerResult;
    }

    @Override
    public Map queryGoodsCommentByOrderId(HttpServletRequest req, String orderId, String status) {
        Map map = getMapper().queryGoodsCommentByOrderId(orderId, status);
        if(map != null && map.get("imageSrc") != null) {
            QueryParam param = new QueryParam();
            param.getParam().put("dataType", 6);
            param.getParam().put("recordId", map.get("imageSrc"));
            List<Map> imgs = fileRefService.queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                for (int i = 0; i < imgs.size(); i++) {
                    imgs.get(i).remove("md5");
                    imgs.get(i).remove("type");
                    imgs.get(i).remove("priority");
                }
                map.put("imageSrc", imgs);
            }else{
                map.put("imageSrc", null);
            }
        }
        return map;
    }

    @Override
    public PagerResult<Map> queryCommentByUserId(QueryParam queryParam){
        PagerResult<Map> pagerResult = new PagerResult<>();
        int total = getMapper().queryCommentByUserIdTotal(Long.valueOf(queryParam.getParam().get("userId").toString()));
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryCommentByUserId(queryParam));
        }
        return pagerResult;
    }

    @Override
    public int addGoodsComment(GoodsComment goodsComment) {
        return getMapper().insert(InsertParam.build(goodsComment));
    }

    @Override
    public List<Map> queryGoodsCommentByGoodsId(Long goodsId){
        return getMapper().queryGoodsCommentByGoodsId(goodsId);
    }
}
