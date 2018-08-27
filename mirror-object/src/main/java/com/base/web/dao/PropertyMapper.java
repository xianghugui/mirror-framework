package com.base.web.dao;

import com.base.web.bean.Property;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 17:34  2018/5/3
 */
public interface PropertyMapper extends GenericMapper<Property, Long> {
    List<Map> selectByType(Integer type);
}
