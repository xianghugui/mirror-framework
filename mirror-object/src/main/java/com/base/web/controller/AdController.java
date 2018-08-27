package com.base.web.controller;

import com.base.web.bean.Ad;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.AdService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;


/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/UserPush")
@AccessLogger("广告管理")
//@Authorize(module = "Ad")
public class AdController extends GenericController<Ad, Long> {

    @Resource
    private AdService AdService;

    @Override
    protected AdService getService() {
        return this.AdService;
    }

    @RequestMapping(value = "/queryAllAd", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryAllAd(QueryParam param,Integer pushType){
        param.getParam().put("pushType",pushType);
        Object data=AdService.queryAllAd(param);
        return ok(data)
                .include(getPOType(), param.getIncludes())
                .exclude(getPOType(), param.getExcludes())
                .onlyData();
    }

    @RequestMapping(value = "/queryAd/{adId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryAd(@PathVariable("adId") Long adId){
        return ok(AdService.selectByPk(adId));
    }

}
