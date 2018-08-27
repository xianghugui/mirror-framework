package com.base.web.service.impl;

import com.base.web.bean.GetMoney;
import com.base.web.bean.TUser;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.GetMoneyMapper;
import com.base.web.service.GetMoneyService;
import com.base.web.service.TUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("GetMoneyService")
public class GetMoneyServiceImpl extends AbstractServiceImpl<GetMoney, Long> implements GetMoneyService {

    @Resource
    private GetMoneyMapper GetMoneyMapper;

    @Resource
    private TUserService tUserService;


    @Override
    protected GetMoneyMapper getMapper() {
        return this.GetMoneyMapper;
    }


    @Override
    public List<Map> showGetMoney() {
        return getMapper().showGetMoney();
    }

    @Override
    @Transactional
    public int insertGetMoney(BigDecimal money,String bank){
        Long userId = WebUtil.getLoginUser().getId();
        //判断余额是否大于提取金额
        TUser tUser = tUserService.selectByPk(userId);
        if(tUser.getEarn().compareTo(money) >= 0 ) {
            GetMoney getMoney = new GetMoney();
            getMoney.setId(GenericPo.createUID());
            getMoney.setUserId(userId);
            getMoney.setMoney(money);
            getMoney.setBank(bank);
            getMoney.setCreateTime(new Date());
            getMoney.setStatus(0);
            getMapper().insert(InsertParam.build(getMoney));
            tUser.setEarn(tUser.getEarn().subtract(money));
            return tUserService.update(tUser);
        }
        return 0;
    }

    @Override
    public PagerResult<Map> queryGetMoney(QueryParam param){
        Long userId = WebUtil.getLoginUser().getId();
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().queryGetMoneyTotal(userId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryGetMoney(userId,param));
        }
        return pagerResult;
    }


}
