package com.base.web.controller;

import com.base.web.bean.TUser;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.TUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @Author: Geek、
 * @Date: Created in 12:53  2018/3/28
 */
@RestController
@RequestMapping(value = "/tuser")
@AccessLogger("会员")
@Authorize(module = "tuser")
public class TUserController extends GenericController<TUser, Long> {

    @Resource
    private TUserService tUserService;

    @Override
    protected TUserService getService() {
        return this.tUserService;
    }

    @RequestMapping(value = "/addshopquerytuser/{shopId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryall(@PathVariable("shopId") String shopId){
        return ResponseMessage.ok(getService().addshopquerytuser(shopId));
    }

    @RequestMapping(value = "/querytuser", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryTuser(){
        return ResponseMessage.ok(getService().queryTuser());
    }

    @RequestMapping(value = "/{tuserId}/enable", method = RequestMethod.PUT)
    @Authorize(action = "U")
    public ResponseMessage tuserEnable(@PathVariable("tuserId") String tuserId){
        return ResponseMessage.ok(getService().changeStatus("0",tuserId));
    }

    @RequestMapping(value = "/{tuserId}/disable", method = RequestMethod.PUT)
    @Authorize(action = "U")
    public ResponseMessage tuserDisable(@PathVariable("tuserId") String tuserId){
        return ResponseMessage.ok(getService().changeStatus("1",tuserId));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Authorize(action = "C")
    public ResponseMessage addTuser(@RequestBody TUser tUser){
        TUser old2 = getService().createQuery()
                .where(TUser.Property.phone,tUser.getPhone())
                .and(TUser.Property.roleId,10012)
                .single();
        if(old2 != null){
            return   ResponseMessage.error("手机号已存在，请重新填写",201);
        }
        tUser.setId(GenericPo.createUID());
        tUser.setStatus(0);
        tUser.setCreateTime(new Date());
        tUser.setRoleId(Long.valueOf(10012));
        return ResponseMessage.ok(getService().insert(tUser));
    }

    @RequestMapping(value = "/{tuserId}/info", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage tuserInfo(@PathVariable("tuserId") String tuserId){
        TUser tUser = getService().selectByPk(Long.parseLong(tuserId));
        tUser.setPassword("12345678");
        return ResponseMessage.ok(tUser);
    }
    @RequestMapping(value = "/{tuserId}/update", method = RequestMethod.PUT)
    @Authorize(action = "U")
    public ResponseMessage tuserUpdate(@RequestBody TUser tUser){
        return ResponseMessage.ok(getService().update(tUser));
    }

}
