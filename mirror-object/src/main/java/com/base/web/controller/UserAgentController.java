package com.base.web.controller;

import com.base.web.bean.UserAgent;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.UserAgentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;




/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/userAgent")
@AccessLogger("区域分配")
public class UserAgentController extends GenericController<UserAgent, Long>{

    @Resource
    private UserAgentService userAgentService;

    @Override
    protected UserAgentService getService() {
        return this.userAgentService;
    }

}
