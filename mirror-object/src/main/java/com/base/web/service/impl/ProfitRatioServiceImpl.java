package com.base.web.service.impl;

import com.base.web.bean.ProfitRatio;
import com.base.web.dao.ProfitRatioMapper;
import com.base.web.service.ProfitRatioService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("ProfitRatioService")
public class ProfitRatioServiceImpl extends AbstractServiceImpl<ProfitRatio, Long> implements ProfitRatioService {

    @Resource
    private ProfitRatioMapper ProfitRatioMapper;


    @Override
    protected ProfitRatioMapper getMapper() {
        return this.ProfitRatioMapper;
    }


}
