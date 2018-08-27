package com.base.web.service;

import com.base.web.bean.Logistics;

/**
 * @Author: Geek、
 * @Date: Created in 10:01  2018/3/28
 */
public interface LogisticsService extends GenericService<Logistics,Integer> {

    /**
     * 根据快递公司名字觉得是否需要插入数据 （商家选择派送快递时候调用该方法）
     * @param name
     * @return
     */
    Integer insertLogistics(String name);
}
