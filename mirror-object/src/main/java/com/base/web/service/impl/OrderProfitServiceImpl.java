package com.base.web.service.impl;

import com.base.web.bean.OrderProfit;
import com.base.web.bean.TUser;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.OrderProfitMapper;
import com.base.web.dao.TUserMapper;
import com.base.web.service.OrderProfitService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
* @Author: FQ
* @Date: 2018/4/24 10:45
*/

@Service("OrderProfitService")
public class OrderProfitServiceImpl extends AbstractServiceImpl<OrderProfit, Long> implements OrderProfitService {

    @Resource
    private TUserMapper tUserMapper;

    @Resource
    private OrderProfitMapper orderProfitMapper;

    @Override
    protected OrderProfitMapper getMapper() {
        return this.orderProfitMapper;
    }

    @Override
    //查询用户试穿记录
    public PagerResult<Map> queryOrderProfit(Integer type, QueryParam queryParam){
        Long userId = WebUtil.getLoginUser().getId();
        PagerResult<Map> pagerResult = new PagerResult<>();
        int total = getMapper().total(queryParam);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {

            TUser tUser;
            List<Map> queryOrderProfit = getMapper().queryOrderProfit(userId,type,queryParam);
            for(Map item:queryOrderProfit){
                tUser = tUserMapper.selectByPk((Long) item.get("orderUserId"));
                if (tUser!=null){
                    item.put("userName", tUser.getName());
                    item.put("avatar", tUser.getAvatar());
                }
            }
            pagerResult.setData(queryOrderProfit);

        }
        return pagerResult;
    }

    @Override
    public List<OrderProfit> queryOrderProfitListByUserId(Long userId, Long orderUserId) {
        return getMapper().queryOrderProfitListByUserId(userId, orderUserId);
    }

    @Override
    public PagerResult<Map> transactionRecord(QueryParam queryParam) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        int total = getMapper().transactionRecordTotal(queryParam);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().transactionRecord(queryParam));

        }
        return pagerResult;
    }

}
