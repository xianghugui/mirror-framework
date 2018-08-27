package com.base.web.service.impl;

import com.base.web.bean.ShopGoodsPush;
import com.base.web.dao.ShopGoodsPushMapper;
import com.base.web.service.ShopGoodsPushService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("ShopGoodsPushService")
public class ShopGoodsPushServiceImpl extends AbstractServiceImpl<ShopGoodsPush, Long> implements ShopGoodsPushService {
    @Resource
    private ShopGoodsPushMapper shopGoodsPushMapper;
    @Override
    protected ShopGoodsPushMapper getMapper() {
        return this.shopGoodsPushMapper;
    }

    @Override
    public Map selectOrderByTimePickOne(Long userId, Long shopId) {
        return getMapper().selectOrderByTimePickOne(userId, shopId);
    }

    @Override
    public List<Map> queryShopUser(Long shopId) {
        return getMapper().queryShopUser(shopId);
    }

    @Override
    public List<Map> queryRecommendUserList(Long phone, Long shopId) {
        return getMapper().queryRecommendUserList(phone, shopId);
    }
}
