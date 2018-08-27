package com.base.web.controller;

import com.base.web.bean.Message;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.MessageService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


@RestController
@RequestMapping(value = "/message")
@AccessLogger("消息")
public class MessageController extends GenericController<Message, Long>{
    @Resource
    private MessageService messageService;

    @Override
    protected MessageService getService() {
        return this.messageService;
    }



    @RequestMapping(value = "/queryMessage",method = RequestMethod.GET)
    @AccessLogger("查询全部消息")
    @Authorize(action = "R")
    public ResponseMessage queryMessage(QueryParam param){
        return ResponseMessage.ok(messageService.queryMessage(param)).include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }

    @RequestMapping(value = "{id}/queryMessageById",method = RequestMethod.GET)
    @AccessLogger("根据ID查询消息")
    @Authorize(action = "R")
    public ResponseMessage queryMessageById(@PathVariable Long id){
        return ResponseMessage.ok(messageService.selectByPk(id));
    }

    @RequestMapping(value = "/addMessage",method = RequestMethod.POST)
    @AccessLogger("新增")
    @Authorize(action = "C")
    public ResponseMessage addMessage(@RequestBody Message message){
        message.setCreateTime(new Date());
        return ResponseMessage.ok(messageService.insert(message));
    }

    @RequestMapping(value = "/updateMessage",method = RequestMethod.PUT)
    @AccessLogger("修改")
    @Authorize(action = "U")
    public ResponseMessage updateMessage(@RequestBody Message message){
        message.setCreateTime(new Date());
        return ResponseMessage.ok(messageService.update(message));
    }

    @RequestMapping(value = "{id}/deleteMessage",method = RequestMethod.DELETE)
    @AccessLogger("删除")
    @Authorize(action = "U")
    public ResponseMessage deleteMessage(@PathVariable("id") Long id){
        return ResponseMessage.ok(messageService.delete(id));
    }


}
