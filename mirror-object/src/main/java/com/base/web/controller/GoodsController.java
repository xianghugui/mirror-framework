package com.base.web.controller;

import com.base.web.bean.Goods;
import com.base.web.bean.RecommendGoods;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsService;
import com.base.web.service.RecommendGoodsService;
import com.base.web.service.resource.FileRefService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;

@RestController
@RequestMapping(value = "/goodsinfo")
@AccessLogger("区域分配")
@Authorize(module = "goodsinfo")
public class GoodsController extends GenericController<Goods, Long>{
    @Resource
    private GoodsService goodsService;

    @Resource
    private RecommendGoodsService recommendGoodsService;

    @Resource
    private FileRefService fileRefService;

    @Override
    public GoodsService getService()
    {
        return this.goodsService;
    }

    @RequestMapping(value = "/queryGoodsByClassId/{id}/{level}", method = RequestMethod.GET)
    @AccessLogger("查询所有可分配组织用户关系ID")
    @Authorize(action = "R")
    public ResponseMessage queryGoods( @PathVariable("id") String id,@PathVariable("level") int level,QueryParam param){
        String goodsId=id.substring(0,level*2);
        param.getParam().put("goodsId", goodsId);
        param.getParam().put("level", level*2);
        return ok(getService().queryGoodsByClassId(param))
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }

    @RequestMapping(value = "/queryGoodsById/{id}", method = RequestMethod.GET)
    @AccessLogger("根据id查询商品")
    @Authorize(action = "R")
    public ResponseMessage queryGoodsById( @PathVariable("id") Long id){
        return ok(getService().queryGoodsById(id));
    }

    @AccessLogger("禁用")
    @RequestMapping(value = "/{id}/disable", method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage disable(@PathVariable("id") Long id) {
        getService().disableUser(id);
        return ResponseMessage.ok();
    }

    @AccessLogger("商品推荐")
    @RequestMapping(value = "/{id}/recommend", method = RequestMethod.PUT)
    @Authorize(action = "C")
    public ResponseMessage recommend(@PathVariable("id") Long id) {
        Goods goods = goodsService.selectByPk(id);
        Long recommendId = recommendGoodsService.queryByGoodsId(id);

        if(goods.getRecommendStatus() == null){
            goods.setRecommendStatus(1);
            RecommendGoods recommendGoods = new RecommendGoods();
            recommendGoods.setGoodsClassId(goods.getGoodsClassId());
            recommendGoods.setGoodsId(goods.getId());
            recommendGoods.setRecommendTime(new Date());
            recommendGoods.setId(GenericPo.createUID());
            recommendGoods.setStatus(1);
            recommendGoodsService.insert(recommendGoods);
        }
        else {
            if (goods.getRecommendStatus() == 0) {
                goods.setRecommendStatus(1);
                recommendGoodsService.changeStatus(recommendId, 1);
            }
            else {
                goods.setRecommendStatus(0);
                recommendGoodsService.changeStatus(recommendId, 0);
            }
        }
        return ResponseMessage.ok(goodsService.update(goods));
    }

    @AccessLogger("启用")
    @RequestMapping(value = "/{id}/enable", method = RequestMethod.PUT)
    @Authorize(action = "C")
    public ResponseMessage enable(@PathVariable("id") Long id) {
        getService().enableUser(id);
        return ResponseMessage.ok();
    }

    @RequestMapping(value = "/pic", method = RequestMethod.POST)
    @AccessLogger("查询图片")
    @Authorize(action = "R")
    public ResponseMessage pic(@RequestBody FileRef fileRef, HttpServletRequest req) {
        QueryParam queryParam = new QueryParam();
        Map map = new HashMap();
        map.put("recordId",fileRef.getRefId());
        map.put("dataType",fileRef.getDataType());
        queryParam.setParam(map);
        List<Map> list = fileRefService.queryResourceByRecordId(queryParam, req);
        return ok(list);
    }

    @RequestMapping(value = "/allGoods/{shopId}", method = RequestMethod.GET)
    @AccessLogger("查询列表")
    @Authorize(action = "R")
    public ResponseMessage allGoods(QueryParam param, @PathVariable("shopId") Long shopId ) {
        // 获取条件查询
        param.getParam().put("shopId",shopId);
        Object data;
            data = getService().allGoods(param);
        return ok(data)
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }

    @RequestMapping(value = "/insertGoods", method = RequestMethod.POST)
    @AccessLogger("插入商品")
    @Authorize(action = "C")
    public ResponseMessage insertGoods(@RequestBody Goods goods) {
        // 获取条件查询
        if(getService().insertGoods(goods) == 0){
            return ResponseMessage.error("价格不能为负");
        }
        return ok();
    }

    @RequestMapping(value = "/updateGoods/{goodsId}", method = RequestMethod.PUT)
    @AccessLogger("修改商品信息")
    @Authorize(action = "U")
    public ResponseMessage updateGoods(@RequestBody Goods goods,@PathVariable("goodsId") Long goodsId) {
        // 获取条件查询
        goods.setId(goodsId);
        if(getService().updateGoods(goods) == 0){
            return ResponseMessage.error("数据更新失败");
        }
        return ok();
    }

}
