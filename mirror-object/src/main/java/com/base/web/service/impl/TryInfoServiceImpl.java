package com.base.web.service.impl;

import com.base.web.bean.TryInfo;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.common.UpdateParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.GoodsSpecificationMapper;
import com.base.web.dao.TryInfoMapper;
import com.base.web.service.TryInfoService;
import com.base.web.service.resource.FileRefService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 15:18  2018/4/8
 */
@Service("TryInfoService")
public class TryInfoServiceImpl extends AbstractServiceImpl<TryInfo,Long> implements TryInfoService{

    @Resource
    private TryInfoMapper tryInfoMapper;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private GoodsSpecificationMapper goodsSpecificationMapper;



    @Override
    public List<Map> showTryInfo(String tryId) {
        return getMapper().showTryInfo(tryId);
    }

    @Override
    public PagerResult showTryOrders(QueryParam param, HttpServletRequest req) {
        List<Map> list = fileRefService.addImages(getMapper().showTryOrders(param),2, req);
        int total = getMapper().queryTryOrdersTotal(param);
        return new PagerResult(total, list);
    }

    @Override
    public Map showTryOrderInfo(String tryInfoId) {
        return getMapper().showTryOrderInfo(tryInfoId);
    }

    @Override
    public Map showAddress(String tryInfoId) {
        return getMapper().showAddress(tryInfoId);
    }

    @Override
    public int delivery(String tryInfoId, String expressId, String expressNumber) {
        return getMapper().delivery(tryInfoId, expressId, expressNumber);
    }

    @Override
    protected TryInfoMapper getMapper() {
        return this.tryInfoMapper;
    }

    //插入试衣详情
    @Override
    public int insertTryInfo(TryInfo tryInfo,Long tryId){
        tryInfo.setId(GenericPo.createUID());
        tryInfo.setTryId(tryId);
        tryInfo.setStatus(0);
        tryInfo.setPrice(tryInfo.getPrice());
        tryInfo.setCreateTime(new Date());
        //插入订单时减少商品数量
        Long goodsSpecId = tryInfo.getGoodsSpecId();

        //商品库存
        Integer quality = goodsSpecificationMapper.queryGoodsSpecQuality(goodsSpecId);
        //购买数量
        Integer buyNum = tryInfo.getNum();
        if(quality >= buyNum) {
            getMapper().insert(InsertParam.build(tryInfo));
            //取相反数
            return goodsSpecificationMapper.updateGoodsSpecQuality(goodsSpecId, ~buyNum + 1);
        }
        return 3;
    }
    @Override
    public PagerResult showTrading(QueryParam param) {
        return new PagerResult(getMapper().showTradingTotal(param), getMapper().showTrading(param));
    }

    @Override
    public PagerResult clientShowOrders(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().clientShowOrders(param);
        return new PagerResult(getMapper().clientShowOrdersTotal(param), fileRefService.addImages(list,2, req));
    }

    @Override
    public int changeStatus(Long orderId, Integer status) {
        return getMapper().changeStatus(orderId, status);
    }

    @Override
    public int submitOrder(Long orderId) {
        return getMapper().submitOrder(orderId, getMapper().queryPriceByTryOrderId(orderId));
    }


    //>>根据试穿订单ID查询订单详情
    @Override
    public List<Map> queryTryByTryId(Long tryId){
        return getMapper().queryTryByTryId(tryId);
    }


    //>>查询用户消费中的订单详情
    @Override
    public PagerResult<Map> queryTryInfo(QueryParam queryParam){
        PagerResult<Map> pagerResult = new PagerResult<>();
        Long userId = WebUtil.getLoginUser().getId();
        Integer total = getMapper().queryTryInfoTotal(userId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryTryInfo(userId,queryParam));
        }
        return pagerResult;
    }

}
