package com.base.web.controller;

import com.base.web.bean.GoodsComment;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsCommentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;


@RestController
@RequestMapping(value = "/goodscomment")
@AccessLogger("商品评论")
public class GoodsCommentController extends GenericController<GoodsComment, Long>{
    @Resource
    private GoodsCommentService goodsCommentService;

    @Override
    public GoodsCommentService getService(){
        return this.goodsCommentService;
    }

    @RequestMapping(value = "/queryGoodsComment/{goodsId}", method = RequestMethod.GET)
    @Authorize(action = "D")
    public ResponseMessage closeStatus(@PathVariable("goodsId") Long goodsId){
        return ok(goodsCommentService.queryGoodsCommentByGoodsId(goodsId));
    }
}
