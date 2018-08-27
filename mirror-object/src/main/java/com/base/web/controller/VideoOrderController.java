package com.base.web.controller;

import com.base.web.bean.VideoOrder;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.VideoOrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:23  2018/4/10
 */
@RestController
@RequestMapping(value = "/videoorder")
@Authorize(module = "videoorder")
public class VideoOrderController extends GenericController<VideoOrder, Long> {

    @Resource
    private VideoOrderService videoOrderService;

    @Override
    protected VideoOrderService getService() {
        return this.videoOrderService;
    }

    @RequestMapping(value = "/showvideoorder",method = RequestMethod.GET)
    public ResponseMessage showVideoOrder(HttpServletRequest req){

        return ResponseMessage.ok(getService().showVideoOrder(req));
    }
}
