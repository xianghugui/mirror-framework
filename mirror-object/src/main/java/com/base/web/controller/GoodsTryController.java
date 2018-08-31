package com.base.web.controller;

import com.base.web.bean.GoodsTry;
import com.base.web.bean.TryInfo;
import com.base.web.bean.TryOrderDeal;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.GoodsTryService;
import com.base.web.service.ShopService;
import com.base.web.service.TryInfoService;
import com.base.web.service.TryOrderDealService;
import com.base.web.service.resource.FileRefService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:52  2018/4/8
 */
@RestController
@RequestMapping(value = "/tryorder")
@AccessLogger("试衣订单")
@Authorize(module = "tryorder")
public class GoodsTryController extends GenericController<GoodsTry, Long> {

    @Resource
    private GoodsTryService goodsTryService;

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private ShopService shopService;

    @Resource
    private TryOrderDealService tryOrderDealService;

    @Resource
    private FileRefService fileRefService;

    @Override
    protected GoodsTryService getService() {
        return this.goodsTryService;
    }

    @RequestMapping(value = "/showtryorder", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage showTryOrder() {
        List<Map> data = getService().showTryOrder();
        return ResponseMessage.ok(data);
    }

    @RequestMapping(value = "{tryId}/showtryinfo", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage showTryInfo(@PathVariable(value = "tryId") String tryId,
                                       @RequestParam("longtitude") String longtitude,
                                       @RequestParam("laltitude") String laltitude,
                                       HttpServletRequest req) {
        List<Map> list = tryInfoService.showTryInfo(tryId);
        list = fileRefService.addImages(list, 2, req);
        for(int i = 0; i < list.size(); i++){
            List<Map> data = shopService.queryShopBylngLat(longtitude, laltitude, list.get(i).get("goodsId").toString());
            list.get(i).put("shops", data);
        }
        return ResponseMessage.ok(list);
    }


    @RequestMapping(value = "/selectshop",method = RequestMethod.POST)
    @Authorize(action = "U")
    @Transactional
    public ResponseMessage selectShop(@RequestBody Map map){
        if (map.get("shopId").toString() == "-1"){
            return ResponseMessage.error("未选择店铺");
        }
        // 商品状态（0待派单，1待发货，2待接收，3确认接收，4试衣购买，5试衣退回，6完成试衣,7取消订单）
        TryInfo data = new TryInfo();
        data.setId((Long) map.get("tryinfoId"));
        data.setStatus(1);
        data.setShopId(Long.valueOf(map.get("shopId").toString()));
        data.setPaidanTime(new Date());
        int i = tryInfoService.update(data);
        Long tryOrderDealId = tryOrderDealService.queryIdByOrderId(map.get("tryorderId").toString());
        TryOrderDeal tryOrderDeal = new TryOrderDeal();
        if (tryOrderDealId != null){
            tryOrderDeal.setId(tryOrderDealId);
            tryOrderDeal.setDealUserId(WebUtil.getLoginUser().getId());
            tryOrderDeal.setDealTime(new Date());
            tryOrderDealService.update(tryOrderDeal);
        }else {
            tryOrderDeal.setId(GenericPo.createUID());
            tryOrderDeal.setOrderId(Long.valueOf(map.get("tryorderId").toString()));
            tryOrderDeal.setCreatorId(WebUtil.getLoginUser().getId());
            tryOrderDeal.setCreateTime(new Date());
            tryOrderDealService.insert(tryOrderDeal);
        }
        return ResponseMessage.ok(i);
    }

}
