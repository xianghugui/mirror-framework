package com.base.web.service;

import com.base.web.bean.Property;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 17:32  2018/5/3
 */
public interface PropertyService extends GenericService<Property,Long>{
    //根据type查询信息，1：年龄段，2：适合场合
    List<Map> selectByType(Integer type);
}
