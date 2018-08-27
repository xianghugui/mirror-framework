package com.base.web.service.impl;

import com.base.web.bean.Brand;
import com.base.web.dao.BrandMapper;
import com.base.web.service.BrandService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("BrandService")
public class BrandServiceImpl extends AbstractServiceImpl<Brand, Integer> implements BrandService {
    @Resource
    private BrandMapper brandMapper;

    @Override
    public BrandMapper getMapper(){
        return this.brandMapper;
    }

    @Override
    public List<Map> queryAllBrand(){
        return getMapper().queryAllBrand();
    }


}
