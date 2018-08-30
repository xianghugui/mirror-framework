package com.base.web.service.impl;

import com.base.web.bean.UserAddress;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.UserAddressMapper;
import com.base.web.service.UserAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("UserAddressService")
public class UserAddressServiceImpl extends AbstractServiceImpl<UserAddress, Long> implements UserAddressService {

    @Resource
    private UserAddressMapper UserAddressMapper;


    @Override
    protected UserAddressMapper getMapper() {
        return this.UserAddressMapper;
    }

    @Override
    public Map queryUserAddress(Long userId){
        return getMapper().queryUserAddress(userId);
    }

    @Override
    public List<Map> queryUserAddressByUserId(){
        Long uId = WebUtil.getLoginUser().getId();
        return getMapper().queryUserAddressByUserId(uId);
    }


    @Override
    public int updateStatus(Long userId){
       return getMapper().updateStatus(userId);
    }
}
