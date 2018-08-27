package com.base.web.controller;

import com.base.web.bean.Video;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.VideoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/Video")
@AccessLogger("店铺管理")
@Authorize(module = "Video")
public class VideoController extends GenericController<Video, Long> {

    @Resource
    private VideoService VideoService;

    @Override
    protected VideoService getService() {
        return this.VideoService;
    }

}
