package com.base.web.service.impl;

import com.base.web.bean.GoodsTry;
import com.base.web.bean.TryInfo;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.GoodsTryMapper;
import com.base.web.service.GoodsTryService;
import com.base.web.service.ShoppingCartService;
import com.base.web.service.TUserService;
import com.base.web.service.TryInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 11:48  2018/4/8
 */
@Service("GoodsTryService")
public class GoodsTryServiceImpl extends AbstractServiceImpl<GoodsTry, Long> implements GoodsTryService {

    @Resource
    private GoodsTryMapper goodsTryMapper;

    @Resource
    private TryInfoService tryInfoService;

    @Resource
    private TUserService tUserService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Override
    public List<Map> showTryOrder() {
        return getMapper().showTryOrder();
    }


    @Override
    protected GoodsTryMapper getMapper() {
        return this.goodsTryMapper;
    }

    // >>插入试衣订单记录
    @Override
    @Transactional
    public Long insertGoodsTry(TryInfo[] tryInfo) {
        Long userId = WebUtil.getLoginUser().getId();
        // >>试衣订单对象
        GoodsTry goodsTry = new GoodsTry();
        goodsTry.setId(GenericPo.createUID());
        goodsTry.setCreateTime(new Date());
        goodsTry.setAddressId(tryInfo[0].getAddressId());
        goodsTry.setUserId(userId);
        goodsTry.setStatus(0);
        Integer degree = tUserService.queryResidueDegree(userId);

        // >>插入试衣详情
        for (int i = 0; i < tryInfo.length; i++) {
            // 判断试穿次数是否大于0
            if (degree != null && degree > 0) {
                if (tryInfoService.insertTryInfo(tryInfo[i], goodsTry.getId()) == 3) {
                    return -1L;
                }
                // >>修改试穿次数 -1
                tUserService.updateResidueDegree((degree - 1), userId);
            } else {
                return 0L;
            }
        }
        getMapper().insert(InsertParam.build(goodsTry));
        return goodsTry.getId();
    }

    @Override
    //查询用户试穿记录
    public PagerResult<Map> queryGoodsTry(QueryParam queryParam) {
        Long userId = WebUtil.getLoginUser().getId();
        PagerResult<Map> pagerResult = new PagerResult<>();
        int total = getMapper().queryGoodsTryTotal(userId);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryGoodsTry(userId, queryParam));
        }
        return pagerResult;
    }
}
