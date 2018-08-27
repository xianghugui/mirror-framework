package com.base.web.service;

import com.base.web.bean.Specification;

import java.util.List;

public interface SpecificationService extends GenericService<Specification,Integer>{

    List<Specification> queryAllSpec();
}
