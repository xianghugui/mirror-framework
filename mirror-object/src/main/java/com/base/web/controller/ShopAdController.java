package com.base.web.controller;

import com.base.web.bean.*;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.AdService;
import com.base.web.service.ShopAdService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;


/**
 * @Author: Geek、
 * @Date: Created in 10:24  2018/3/28
 */
@RestController
@RequestMapping(value = "/ShopAd")
@AccessLogger("店铺广告")
@Authorize(module = "otherad")
public class ShopAdController extends GenericController<ShopAd, Long> {

    @Resource
    private ShopAdService ShopAdService;
    @Resource
    private AdService adService;
    @Override
    protected ShopAdService getService() {
        return this.ShopAdService;
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @Authorize(action = "U")
    @Transactional
    public ResponseMessage add(@RequestBody ShopAd area){
        Ad ad = adService.selectByPk(area.getAdId());
//        平台广告
        if (ad.getPushType() == 2){
            area.setType(2);
            area.setCreateTime(new Date());
            ShopAdService.insert(area);// 发布平台广告  所有用户均可浏览
            ad.setStatus(1);
            adService.update(ad);//更新ad表 该广告已经发布
        }else{
            getService().insertShopAd(area);
        }

        return ResponseMessage.ok();
    }

    @RequestMapping(value = "/cancelPush/{adId}", method = RequestMethod.PUT)
    @Authorize(action = "D")
    @Transactional
    public ResponseMessage cancelPush(@PathVariable("adId") Long adId){
        return ok(ShopAdService.cancelPush(adId));
    }
    
}
