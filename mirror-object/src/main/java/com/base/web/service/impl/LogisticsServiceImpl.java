package com.base.web.service.impl;

import com.base.web.bean.Logistics;
import com.base.web.dao.LogisticsMapper;
import com.base.web.service.LogisticsService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("LogisticsService")
public class LogisticsServiceImpl extends AbstractServiceImpl<Logistics, Integer> implements LogisticsService{

    @Resource
    private LogisticsMapper LogisticsMapper;

    @Override
    protected LogisticsMapper getMapper() {
        return this.LogisticsMapper;
    }


    @Override
    public Integer insertLogistics(String name) {
        Integer  id = getMapper().queryLogisticsByName(name);
        if( id == null){
            Logistics logistics = new Logistics();
            logistics.setName(name);
            getMapper().insertLogistics(logistics);
            id = logistics.getId();
        }
        return id;
    }

}
