package com.base.web.controller;

import com.base.web.bean.VideoUser;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.VideoUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/VideoUser")
@AccessLogger("店铺管理")
@Authorize(module = "VideoUser")
public class VideoUserController extends GenericController<VideoUser, Long> {

    @Resource
    private VideoUserService VideoUserService;

    @Override
    protected VideoUserService getService() {
        return this.VideoUserService;
    }

}
