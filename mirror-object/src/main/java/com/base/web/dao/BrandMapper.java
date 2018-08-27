package com.base.web.dao;

import com.base.web.bean.Brand;

import java.util.List;
import java.util.Map;

public interface BrandMapper extends GenericMapper<Brand,Integer>{
    List<Map> queryAllBrand();
}
