package com.base.web.controller;

import com.base.web.bean.Specification;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.SpecificationService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;

@RestController
@RequestMapping(value = "/Specification")
@AccessLogger("规格")
public class SpecificationController extends GenericController<Specification,Integer>{

    @Resource
    private SpecificationService specificationService;

    @Override
    public SpecificationService getService(){
        return this.specificationService;
    }

    @RequestMapping(value = "/queryAllSpec", method = RequestMethod.GET)
    @Authorize(action = "R")
    public ResponseMessage queryAllSpec(){
        return ok(getService().queryAllSpec());
    }
}
