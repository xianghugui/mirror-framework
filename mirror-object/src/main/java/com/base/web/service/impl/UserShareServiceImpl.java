package com.base.web.service.impl;

import com.base.web.bean.UserShare;
import com.base.web.dao.UserShareMapper;
import com.base.web.service.UserShareService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("UserShareService")
public class UserShareServiceImpl extends AbstractServiceImpl<UserShare, Long> implements UserShareService{

    @Resource
    private UserShareMapper UserShareMapper;

    @Override
    protected UserShareMapper getMapper() {
        return this.UserShareMapper;
    }


    @Override
    public List<Map> buyList(Long goodsId, Long parentId) {
        List<Map> buyList = getMapper().buyList(goodsId, parentId);
        if(buyList.size() > 0){
            buyList.add(buyList.size() ,getMapper().totalMoney(goodsId, parentId));
        }
        return buyList;
    }

    @Override
    public List<Map> userList(Long goodsId, Long parentId) {
        return getMapper().userList(goodsId, parentId);
    }

}
