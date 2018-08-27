package com.base.web.service;


import com.base.web.bean.UserAddress;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:07  2018/3/27
 */
public interface UserAddressService extends GenericService<UserAddress, Long> {

    Map queryUserAddress(Long userId);

    List<Map> queryUserAddressByUserId(Integer status);

    int updateStatus(Long userId);

}
