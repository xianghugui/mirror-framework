package com.base.web.controller;

import com.base.web.bean.UserAddress;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.service.UserAddressService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;



/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/UserAddress")
@AccessLogger("区域分配")
public class UserAddressController extends GenericController<UserAddress, Long>{

    @Resource
    private UserAddressService UserAddressService;

    @Override
    protected UserAddressService getService() {
        return this.UserAddressService;
    }

}
