package com.base.web.controller;

import com.base.web.bean.Goods;
import com.base.web.bean.RecommendGoods;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsService;
import com.base.web.service.RecommendGoodsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;


@RestController
@RequestMapping(value = "/recommendGoods")
@AccessLogger("服装推荐")
public class RecommendGoodsController extends GenericController<RecommendGoods, Long> {

    @Resource
    private RecommendGoodsService RecommendGoodsService;

    @Resource
    private GoodsService goodsService;

    @Override
    protected RecommendGoodsService getService() {
        return this.RecommendGoodsService;
    }

    @AccessLogger("商品推荐")
    @RequestMapping(value = "/{id}/recommend", method = RequestMethod.PUT)
    public ResponseMessage recommend(@PathVariable("id") Long id) {
        RecommendGoods recommendGoods = getService().selectByPk(id);
        Goods goods = goodsService.selectByPk(recommendGoods.getGoodsId());
        if(recommendGoods.getStatus() == 0){
            goods.setRecommendStatus(1);
            getService().changeStatus(id,1);
        }
        else{
            goods.setRecommendStatus(0);
            getService().changeStatus(id,0);
        }
        return ResponseMessage.ok(goodsService.update(goods));
    }

    @RequestMapping(value = "/queryGoodsByClassId/{goodsClassId}/{level}", method = RequestMethod.GET)
    @AccessLogger("根据商品类别查询商品推荐")
    @Authorize(action = "R")
    public ResponseMessage queryGoods(@PathVariable("goodsClassId") String goodsClassId, @PathVariable("level") int level,
                                      QueryParam param){
        String goodsClassPrefixId=goodsClassId.substring(0,level*2);
        param.getParam().put("goodsClassIdPrefix", goodsClassPrefixId);
        param.getParam().put("level", level*2);
        Object data = getService().queryByGoodsClassId(param);
        return ok(data)
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }
}
