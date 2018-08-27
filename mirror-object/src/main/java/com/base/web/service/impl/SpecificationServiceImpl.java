package com.base.web.service.impl;

import com.base.web.bean.Specification;
import com.base.web.dao.SpecificationMapper;
import com.base.web.service.SpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("SpecificationService")
public class SpecificationServiceImpl extends AbstractServiceImpl<Specification, Integer> implements SpecificationService{

    @Resource
    private SpecificationMapper specificationMapper;

    @Override
    public SpecificationMapper getMapper(){
        return this.specificationMapper;
    }

    @Override
    public List<Specification> queryAllSpec(){
        return getMapper().queryAllSpec();
    }
}
