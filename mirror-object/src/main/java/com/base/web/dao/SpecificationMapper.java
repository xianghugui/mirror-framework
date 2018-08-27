package com.base.web.dao;

import com.base.web.bean.Specification;

import java.util.List;

public interface SpecificationMapper extends GenericMapper<Specification,Integer>{

    List<Specification> queryAllSpec();

}
