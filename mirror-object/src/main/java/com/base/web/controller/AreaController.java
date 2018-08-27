package com.base.web.controller;

import com.base.web.bean.Area;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.AreaService;
import com.base.web.service.GenericService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;

/**
 * @Author: Geek、
 * @Date: Created in 14:27  2018/3/27
 */
@RestController
@RequestMapping(value = "/area")
@AccessLogger("区域分配")
public class AreaController extends GenericController<Area, String>{

    @Resource
    private AreaService areaService;

    @Override
    protected AreaService getService() {
        return this.areaService;
    }

    //店铺管理页面区域树的显示
    @RequestMapping(value = "/queryallviewtree", method = RequestMethod.GET)
    public ResponseMessage queryAllviewTree(){
        return  ok(areaService.queryAllviewTree());
    }

    //删除区域
    @RequestMapping(value = "/{uId}/deletearea", method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage deleteArea(@PathVariable("uId") String uId){
        return ok(areaService.changestatus(uId,"0"));
    }
    //添加区域
    @RequestMapping(value = "/{uId}/addarea", method = RequestMethod.PUT)
    @Authorize(action = "D")
    public ResponseMessage addArea(@PathVariable("uId") Integer uId){
        areaService.changestatus((uId/10000*10000) + "","1");
        areaService.changestatus((uId/100*100) + "","1");
        return ok(areaService.changestatus(uId.toString(),"1"));
    }

    //查询省
    @RequestMapping(value = "/queryprovince", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryProvince(){
        return ok(areaService.queryProvince());
    }
    //查询市
    @RequestMapping(value = "/querycity/{uId}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryCity(@PathVariable("uId") String uId){
        return ok(areaService.queryCity(uId));
    }
    //查询区
    @RequestMapping(value = "/{uId}/queryarea/{status}", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryArea(@PathVariable("uId") String uId, @PathVariable("status") Boolean status){
        return ok(areaService.queryArea(uId, status));
    }

    @RequestMapping(value = "/queryall", method = RequestMethod.GET)
    public ResponseMessage queryAll(){
        return  ok(areaService.queryAll());
    }



}
