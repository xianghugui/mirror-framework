package com.base.web.dao;

import com.base.web.bean.UserAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 13:59  2018/3/27
 */
public interface UserAddressMapper extends GenericMapper<UserAddress,Long>{

    //用户收货地址
    Map queryUserAddress(Long userId);

    //根据用户id查询地址，用户名，电话
    List<Map> queryUserAddressByUserId(Long userId);

    int updateStatus(Long userId);
}
