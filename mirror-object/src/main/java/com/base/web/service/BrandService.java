package com.base.web.service;

import com.base.web.bean.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService extends GenericService<Brand, Integer>{
    List<Map> queryAllBrand();

}
