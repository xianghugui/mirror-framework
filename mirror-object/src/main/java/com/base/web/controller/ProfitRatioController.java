package com.base.web.controller;

import com.base.web.bean.ProfitRatio;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.ProfitRatioService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/ProfitRatio")
@AccessLogger("区域分配")
public class ProfitRatioController extends GenericController<ProfitRatio, Long>{

    @Resource
    private ProfitRatioService ProfitRatioService;

    @Override
    protected ProfitRatioService getService() {
        return this.ProfitRatioService;
    }

}
