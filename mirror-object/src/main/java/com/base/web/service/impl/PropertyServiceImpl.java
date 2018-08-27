package com.base.web.service.impl;

import com.base.web.bean.Property;
import com.base.web.dao.PropertyMapper;
import com.base.web.service.PropertyService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 17:33  2018/5/3
 */
@Service("PropertyService")
public class PropertyServiceImpl extends AbstractServiceImpl<Property, Long> implements PropertyService {

    @Resource
    private PropertyMapper propertyMapper;
    @Override
    protected PropertyMapper getMapper() {
        return this.propertyMapper;
    }

    @Override
    public List<Map> selectByType(Integer type) {
        return getMapper().selectByType(type);
    }
}
