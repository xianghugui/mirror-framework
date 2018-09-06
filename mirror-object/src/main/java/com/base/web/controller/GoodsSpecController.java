package com.base.web.controller;


import com.base.web.bean.GoodsSpecification;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsSpeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


import static com.base.web.core.message.ResponseMessage.ok;

@RestController
@RequestMapping(value = "/GoodsSpec")
@AccessLogger("规格列表")
public class GoodsSpecController extends GenericController<GoodsSpecification, Long>{
    @Resource
    private GoodsSpeService goodsSpeService;

    @Override
    public GoodsSpeService getService(){
        return this.goodsSpeService;
    }

    @RequestMapping(value = "/queryGoodsSpecByGoodsId/{goodsId}", method = RequestMethod.GET)
    @Authorize(action = "D")
    public ResponseMessage closeStatus(@PathVariable("goodsId") Long goodsId){
        return ok(goodsSpeService.queryGoodsSpecification(goodsId));
    }

    @RequestMapping(value = "/deleteSpeById/{id}", method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage deleteSpeById(@PathVariable("id") Long id){
        return ok(goodsSpeService.deleteSpecById(id));
    }


    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Authorize(action = "U")
    public ResponseMessage addSpeById(@RequestBody GoodsSpecification goodsSpecification){
        if(getService().createQuery()
                .where(GoodsSpecification.Property.color,goodsSpecification.getColor())
                .and(GoodsSpecification.Property.size,goodsSpecification.getSize())
                .and(GoodsSpecification.Property.goodsId,goodsSpecification.getGoodsId()).single() != null){
            return ResponseMessage.ok("该商品规格已添加");
        }
        goodsSpecification.setId(GenericPo.createUID());
        getService().insert(goodsSpecification);
        return ResponseMessage.ok("添加成功");
    }

    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @Authorize(action = "U")
    public ResponseMessage updateSpeById(@RequestBody GoodsSpecification goodsSpecification){
        getService().update(goodsSpecification);
        return ok("修改成功");
    }

}
