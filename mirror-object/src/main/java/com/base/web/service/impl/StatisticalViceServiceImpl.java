package com.base.web.service.impl;

import com.base.web.bean.StatisticalVice;
import com.base.web.dao.StatisticalViceMapper;
import com.base.web.service.StatisticalViceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * @Author: FQ
 * @Date: Created in 11:18  2018/8/6
 */
@Service("StatisticalViceService")
public class StatisticalViceServiceImpl extends AbstractServiceImpl<StatisticalVice,Long> implements StatisticalViceService {
    @Resource
    private StatisticalViceMapper statisticalViceMapper;

    @Override
    protected StatisticalViceMapper getMapper(){
        return this.statisticalViceMapper;
    }

    @Override
    public List<Map> queryWeekShopSalce(Long brandId){
        return getMapper().queryWeekShopSalce(brandId);
    }

    @Override
    public List<Map> queryMonthShopSalce(Long brandId){
        return getMapper().queryMonthShopSalce(brandId);
    }

    @Override
    public List<Map> queryQuarterShopSalce(Long brandId){
        return getMapper().queryQuarterShopSalce(brandId);
    }

    @Override
    public List<Map> queryYearSales(Long brandId){
        return getMapper().queryYearSales(brandId);
    }


}
