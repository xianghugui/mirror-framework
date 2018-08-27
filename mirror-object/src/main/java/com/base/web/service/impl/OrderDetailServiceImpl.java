package com.base.web.service.impl;

import com.base.web.bean.Goods;
import com.base.web.bean.OrderDetail;
import com.base.web.bean.OrderProfit;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.GoodsSpecificationMapper;
import com.base.web.dao.OrderDetailMapper;
import com.base.web.service.GoodsService;
import com.base.web.service.OrderDetailService;
import com.base.web.service.OrderProfitService;
import com.base.web.service.TUserService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.CancelOrderJob;
import com.base.web.util.ResourceUtil;
import com.base.web.util.SchedulerUtil;
import org.quartz.DateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.quartz.DateBuilder.futureDate;


@Service("OrderDetailService")
public class OrderDetailServiceImpl extends AbstractServiceImpl<OrderDetail, Long> implements OrderDetailService {

    @Resource
    private OrderDetailMapper orderDetailMapper;


    @Resource
    private FileRefService fileRefService;

    @Resource
    private GoodsSpecificationMapper goodsSpecificationMapper;

    @Resource
    private SchedulerUtil schedulerUtil;

    @Resource
    private GoodsService goodsService;

    @Resource
    private TUserService tUserService;

    @Resource
    private OrderProfitService orderProfitService;

    @Override
    protected OrderDetailMapper getMapper() {
        return this.orderDetailMapper;
    }

    @Override
    public PagerResult showOrders(QueryParam param, HttpServletRequest req) {
        List<Map> list = fileRefService.addImages(getMapper().showOrders(param), 2, req);
        int total = getMapper().queryOrdersTotal(param);
        return new PagerResult(total, list);
    }

    @Override
    public Map showOrderInfo(String orderId) {
        return getMapper().showOrderInfo(orderId);
    }

    @Override
    public Map showAddress(String orderId) {
        return getMapper().showAddress(orderId);
    }

    //插入订单详情
    @Override
    @Transactional
    public void insertOrderDetail(OrderDetail orderDetail, Map map) {
        //插入订单时减少商品数量
        Long goodsSpecId = orderDetail.getGoodsSpecId();
        if (goodsSpecificationMapper.queryGoodsSpecQuality(goodsSpecId) >= orderDetail.getNum()) {
            goodsSpecificationMapper.updateGoodsSpecQuality(goodsSpecId, ~orderDetail.getNum() + 1);
            orderDetail.setId(GenericPo.createUID());
            orderDetail.setStatus(0);
            orderDetail.setCreateTime(new Date());
            orderDetail.setUserId(WebUtil.getLoginUser().getId());
            getMapper().insert(InsertParam.build(orderDetail));
            //两小时后自动取消订单
            map.put("id", orderDetail.getId());
            schedulerUtil.addJob(orderDetail.getId().toString(), SchedulerUtil.ORDER,
                    map, CancelOrderJob.class, futureDate(3, DateBuilder.IntervalUnit.MINUTE));
        }
    }

    @Override
    public PagerResult showTrading(QueryParam param) {
        return new PagerResult(getMapper().showTradingTotal(param), getMapper().showTrading(param));
    }


    @Override
    public PagerResult showClientOrders(QueryParam param, HttpServletRequest req) {
        List<Map> data = getMapper().showClientOrders(param);
        if (data.size() > 0) {
            for (Map map : data) {
                map.put("imageSrc", ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("imageSrc")).trim()));
            }
        }
        int total = getMapper().showClientOrdersTotal(param);
        return new PagerResult(total, data);
    }

    @Override
    public PagerResult showClientTrading(QueryParam param, HttpServletRequest req) {
        List<Map> data = getMapper().showClientTrading(param);
        data = fileRefService.addImages(data, 2, req);
        int total = getMapper().showClientTradingTotal(param);
        return new PagerResult(total, data);
    }

    @Override
    public int changeStatus(Long orderId, Integer status) {
        return getMapper().changeStatus(orderId, status);
    }

    @Override
    public Map queryShopIdByOrderId(Long orderId) {
        return getMapper().queryShopIdByOrderId(orderId);
    }


    //>>查询用户消费中的订单详情
    @Override
    public PagerResult<Map> queryOrderConsume(QueryParam queryParam) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Long userId = WebUtil.getLoginUser().getId();
        Integer total = getMapper().queryOrderConsumeTotal(userId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryOrderConsume(userId, queryParam));
        }
        return pagerResult;
    }

    @Override
    public int updateStatusByOrderId(OrderDetail orderDetail) {
        return getMapper().updateStatusByOrderId(orderDetail);
    }


    @Override
    public void orderProfit(Long orderId) {
        OrderDetail orderDetail = selectByPk(orderId);
        Goods goods = goodsService.selectByPk(orderDetail.getGoodsId());
        OrderProfit orderProfit = new OrderProfit();
        orderProfit.setParentId(orderDetail.getOrderId());
        orderProfit.setChildId(orderId);
        orderProfit.setGoodsId(orderDetail.getGoodsId());
        orderProfit.setCreateTime(new Date());
        //订单返现
        if (goods.getCashBach() != null && goods.getCashBach().compareTo(BigDecimal.ZERO) != 0) {
            //判断是否重复添加
            if(orderProfitService.createQuery().where(OrderProfit.Property.childId, orderId)
                    .and(OrderProfit.Property.userId, orderDetail.getUserId())
                    .and(OrderProfit.Property.type,3).single() != null){
                return;
            }
            orderProfit.setType(3);
            orderProfit.setUserId(orderDetail.getUserId());
            orderProfit.setPrice(goods.getCashBach().multiply(new BigDecimal(orderDetail.getNum().toString())));
            orderProfitService.insert(orderProfit);
            tUserService.updateEarn(goods.getCashBach(), orderDetail.getUserId());
        }

        //订单完成分佣
        if (orderDetail.getShowUserId() != null && goods.getCommission().compareTo(BigDecimal.ZERO) != 0) {
            //判断是否重复添加
            if(orderProfitService.createQuery().where(OrderProfit.Property.childId, orderId)
                    .and(OrderProfit.Property.userId, orderDetail.getUserId())
                    .and(OrderProfit.Property.type,2).single() != null){
                return;
            }
            orderProfit.setId(GenericPo.createUID());
            orderProfit.setType(2);
            orderProfit.setUserId(orderDetail.getShowUserId());
            orderProfit.setPrice(goods.getCommission().multiply(new BigDecimal(orderDetail.getNum().toString())));
            orderProfitService.insert(orderProfit);
            tUserService.updateEarn(goods.getCommission(), orderDetail.getShowUserId());
        }
    }
}
