package com.base.web.controller;

import com.base.web.bean.GoodsClass;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.GoodsClassService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;

/**
 * 区域分配控制器
 * Created by generator
 */
@RestController
@RequestMapping(value = "/goodsclass")
@AccessLogger("商品类别")
@Authorize
public class GoodsClassController extends GenericController<GoodsClass, Integer>{

    @Resource
    private GoodsClassService goodsClassService;

    @Override
    public GoodsClassService getService()
    {
        return this.goodsClassService;
    }

    @RequestMapping(value = "/currentNodeTree", method = RequestMethod.GET)
    @AccessLogger("查询所有可分配组织用户关系ID")
    @Authorize(action = "R")
    public ResponseMessage queryCurrentNodeAndAllChildNodeTree(){
        List<Map> goodsClassList=goodsClassService.queryGoodsAllNodeAndAllChildNodeTree();
        return ResponseMessage.ok(goodsClassList);
    }

    @RequestMapping(value = "/goodsClassAdd", method = RequestMethod.POST)
    @AccessLogger("添加商品类别顶级类别")
    @Authorize(action = "C")
    public ResponseMessage goodsClassAdd(@RequestBody GoodsClass goodsClass){
        GoodsClass object = new GoodsClass();
        Integer id = getService().queryGoodsClassIdByLevel();
        if(id ==null || id ==0){
            Integer  newId = 100000;
            object.setId(newId);
        }else {
            Integer newId = id/10000;
            object.setId((newId + 1) * 10000);
        }
        object.setLevel(1);
        object.setClassName(goodsClass.getClassName());
        object.setParentId(0);
        object.setResourceId(goodsClass.getResourceId());
        object.setStatus(0);
        getService().insert(object);
        return ResponseMessage.ok();
    }

    @RequestMapping(value = "/goodsClassAddChild", method = RequestMethod.POST)
    @AccessLogger("添加商品类别子类别")
    @Authorize(action = "C")
    public ResponseMessage goodsClassAddChild(@RequestBody GoodsClass goodsClass){

        Integer id = goodsClass.getId();
        Integer idPrefix = 0;
        if(goodsClass.getLevel() ==1){
            idPrefix = id/10000;
        }
        if(goodsClass.getLevel() ==2){
            idPrefix = id/100;
        }
        goodsClass.setIdPrefix(idPrefix);
        Integer maxChildId = getService().queryGoodsClassIdByLevelAndParent(goodsClass.getLevel(),idPrefix);
        GoodsClass object = new GoodsClass();
        Integer newId =0;
        if(goodsClass.getLevel() ==1){
            if (maxChildId ==null){
                newId = (idPrefix * 100 + 1) * 100;
            }else {
                newId = (maxChildId/100 + 1) * 100;
            }
            object.setLevel(2);
        }
        if(goodsClass.getLevel() ==2){

            if(maxChildId ==null){
                newId = goodsClass.getId() + 1;
            }else {
                newId = maxChildId + 1;
            }

            object.setLevel(3);
        }
        object.setId(newId);
        object.setClassName(goodsClass.getClassName());
        object.setParentId(goodsClass.getId());
        object.setResourceId(goodsClass.getResourceId());
        object.setStatus(0);
        getService().insert(object);
        return ResponseMessage.ok();
    }

    @RequestMapping(value = "/deleteGoodsClass/{id}", method = RequestMethod.PUT)
    @AccessLogger("修改")
    @Authorize(action = "U")
    public ResponseMessage updateGoodsClassStatus(@PathVariable("id") Integer id, @RequestBody GoodsClass object) {
        GoodsClass old = getService().createQuery().where(GoodsClass.Property.id,id).single();
        assertFound(old, "data is not found!");
        old.setStatus(1);
        int number = getService().update(old);
        return ok(number);
    }
    @RequestMapping(value = "/queryGoodsClassById/{id}", method = RequestMethod.GET)
    @AccessLogger("查询")
    @Authorize(action = "R")
    public ResponseMessage queryGoodsClassById(@PathVariable("id") Integer id) {
        GoodsClass old = getService().createQuery().where(GoodsClass.Property.id,id).single();

        return ResponseMessage.ok(old);
    }

    @RequestMapping(value = "/updateGoodsClass/{id}", method = RequestMethod.PUT)
    @AccessLogger("修改")
    @Authorize(action = "U")
    public ResponseMessage updateGoodsClass(@PathVariable("id") Integer id, @RequestBody GoodsClass object) {
        GoodsClass old = getService().selectByPk(id);
        assertFound(old, "data is not found!");
        if (object instanceof GenericPo) {
            ((GenericPo) object).setId(id);
        }
        int number = getService().update(object);
        return ok(number);
    }
}
