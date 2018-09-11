package com.base.web.controller;

import com.base.web.bean.GetMoney;
import com.base.web.bean.OrderProfit;
import com.base.web.bean.TUser;
import com.base.web.bean.wxPay.wxUtil.CommonUtil;
import com.base.web.controller.wxPay.WXRefund;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.core.utils.WebUtil;
import com.base.web.service.GetMoneyService;
import com.base.web.service.OrderProfitService;
import com.base.web.service.TUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.Date;


/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/GetMoney")
@Authorize(module = "getmoney")
public class GetMoneyController extends GenericController<GetMoney, Long>{

    @Resource
    private GetMoneyService GetMoneyService;

    @Resource
    private TUserService tUserService;

    @Autowired
    private OrderProfitService orderProfitService;

    @Override
    protected GetMoneyService getService() {
        return this.GetMoneyService;
    }

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    @AccessLogger("查询列表")
    @Authorize(action = "R")
    public ResponseMessage showGetMoney(){
        return ResponseMessage.ok(getService().showGetMoney());
    }

    @RequestMapping(value = "/transfers", method = RequestMethod.GET)
    public ResponseMessage transfers(GetMoney getMoney, HttpServletRequest req){
        getMoney = getService().selectByPk(getMoney.getId());
        if(getMoney.getDealUserId() == null){
            TUser tUser = tUserService.selectByPk(getMoney.getUserId());
            String return_msg = "";
            try {
                return_msg = WXRefund.doTransfers(getMoney.getId(), tUser.getOpenId(), getMoney.getMoney(), "提现", CommonUtil.getClientIp(req));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if("OK".equals(return_msg)){
                Long userId = WebUtil.getLoginUser().getId();
                getMoney.setStatus(2);
                getMoney.setBank("微信零钱");
                getMoney.setDealUserId(userId);
                getMoney.setDealTime(new Date());
                getService().update(getMoney);
                OrderProfit orderProfit = new OrderProfit();
                orderProfit.setUserId(userId);
                orderProfit.setType(4);
                orderProfit.setPrice(getMoney.getMoney());
                orderProfit.setCreateTime(new Date());
                orderProfitService.insert(orderProfit);
                return ResponseMessage.ok("提现成功");
            }
            return ResponseMessage.ok(return_msg);
        }
        return ResponseMessage.error("已处理过提现请求。");
    }
}
