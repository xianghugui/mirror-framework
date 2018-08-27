package com.base.web.service.impl;

import com.base.web.bean.ShoppingCart;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.GoodsMapper;
import com.base.web.dao.ShoppingCartMapper;
import com.base.web.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("ShoppingCartService")
public class ShoppingCartServiceImpl extends AbstractServiceImpl<ShoppingCart, Long> implements ShoppingCartService{
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    protected ShoppingCartMapper getMapper() {
        return this.shoppingCartMapper;
    }

    //查询购物车订单
    public List<Map> queryShoppingCart(){
        Long userId = WebUtil.getLoginUser().getId();
        List<Map> shoppingCartList = getMapper().queryShoppingCart(userId);
        for(Map item:shoppingCartList){
            item.put("agentStatus",goodsMapper.queryAgent(Long.valueOf(item.get("goodsId").toString()),userId));
        }
        return  shoppingCartList;
    }

    @Override
    public Map queryShoppingNum(QueryParam param){
        return  getMapper().queryShoppingNum(param);
    }

    @Override
    public int updateGoodsNum(QueryParam param){
        return getMapper().updateGoodsNum(param);
    }

}
