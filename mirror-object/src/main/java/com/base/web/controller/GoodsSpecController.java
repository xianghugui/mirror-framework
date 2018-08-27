package com.base.web.controller;


import com.base.web.bean.GoodsSpecification;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsSpeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}
