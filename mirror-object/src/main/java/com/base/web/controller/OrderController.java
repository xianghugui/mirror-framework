package com.base.web.controller;

import com.base.web.bean.*;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.*;
import com.base.web.util.ResourceUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:15  2018/4/9
 */
@RestController
@RequestMapping(value = "/ordermgt")
@Authorize(module = "ordermgt")
public class OrderController extends GenericController<Order, Long> {

    @Resource
    private OrderService orderService;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private ShopService shopService;

    @Resource
    private OrderDealService orderDealService;
    @Resource
    private GoodsService goodsService;
    @Resource
    private RefundExchangeService refundExchangeService;
    @Override
    protected OrderService getService() {
        return this.orderService;
    }

    @RequestMapping(value = "/showorder", method = RequestMethod.GET)
    public ResponseMessage showOrder(){
        return ResponseMessage.ok(getService().showOrder());
    }

    @RequestMapping(value = "{orderId}/showorderinfo", method = RequestMethod.GET)
    public ResponseMessage showOrderInfo(@PathVariable("orderId") String orderId,
                                         @RequestParam("longtitude") String longtitude,
                                         @RequestParam("laltitude") String laltitude,
                                         HttpServletRequest req){
       Map list = getService().showOrderInfo(orderId);
        Goods goods =   goodsService.createQuery().where(Goods.Property.id,list.get("goodsId")).single();
       list.put("imageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(orderService.imageUrl(Long.valueOf(list.get("goodsId").toString())))));
       List<Map> data = shopService.queryShopBylngLat(longtitude, laltitude, list.get("goodsId").toString());
       list.put("shops", data);
       list.put("goodsName",goods.getGoodsName());
        return ResponseMessage.ok(list);
    }

    @RequestMapping(value = "/selectshop",method = RequestMethod.POST)
    @Authorize(action = "U")
    @Transactional
    public ResponseMessage selectShop(@RequestBody Map map){
        if (map.get("shopId").toString() == "-1"){
            return ResponseMessage.error("未选择店铺");
        }
        OrderDetail data = new OrderDetail();
        data.setStatus(2);
        data.setId(Long.valueOf(map.get("orderDetailId").toString()));
        data.setShopId(Long.valueOf(map.get("shopId").toString()));
        data.setPaidanTime(new Date());
        int i = orderDetailService.update(data);
        Long orderDealId = orderDealService.queryIdByOrderId(map.get("orderId").toString());
        OrderDeal orderDeal = new OrderDeal();
        if (orderDealId != null){
            orderDeal.setId(orderDealId);
            orderDeal.setDealUserId(WebUtil.getLoginUser().getId().longValue());
            orderDeal.setDealTime(new Date());
            orderDealService.update(orderDeal);
        }else {
            orderDeal.setId(GenericPo.createUID().longValue());
            orderDeal.setOrderId(Long.valueOf(map.get("orderId").toString()));
            orderDeal.setCreatorId(WebUtil.getLoginUser().getId().longValue());
            orderDeal.setCreateTime(new Date());
            orderDealService.insert(orderDeal);
        }
        return ResponseMessage.ok(i);
    }

    @RequestMapping(value = "/refund",method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage refund(){

        return ResponseMessage.ok(refundExchangeService.refund());
    }

    @RequestMapping(value = "/refundOrderTreated/{refundId}",method = RequestMethod.PUT)
    @Authorize(action = "U")
    public ResponseMessage refundOrderTreated(@PathVariable("refundId") Long refundId){
        RefundExchange refundExchange = new RefundExchange();
        refundExchange.setId(refundId);
        refundExchange.setStatus(6);
        refundExchangeService.update(refundExchange);
        return ResponseMessage.ok("处理成功");
    }



}
