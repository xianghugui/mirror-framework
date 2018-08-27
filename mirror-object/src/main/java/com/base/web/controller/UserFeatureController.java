package com.base.web.controller;

import com.base.web.bean.UserFeature;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.UserFeatureService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/userFeature")
@AccessLogger("区域分配")
public class UserFeatureController extends GenericController<UserFeature, Long>{

    @Resource
    private UserFeatureService userFeatureService;

    @Override
    protected UserFeatureService getService() {
        return this.userFeatureService;
    }
    
}
