package com.base.web.service.impl;

import com.base.web.bean.StatisticalMain;
import com.base.web.bean.StatisticalVice;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.StatisticalMainMapper;
import com.base.web.service.BrandService;
import com.base.web.service.StatisticalMainService;
import com.base.web.service.StatisticalViceService;
import com.base.web.util.AddForNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @Author: FQ
 * @Date: Created in 11:18  2018/8/6
 */
@Configuration
@EnableScheduling
@Service("StatisticalMainService")
public class StatisticalMainServiceImpl extends AbstractServiceImpl<StatisticalMain, Long> implements StatisticalMainService {
    @Resource
    private StatisticalMainMapper statisticalMainMapper;

    @Override
    protected StatisticalMainMapper getMapper() {
        return this.statisticalMainMapper;
    }

    @Resource
    private StatisticalViceService statisticalViceService;

    @Resource
    private BrandService brandService;


    /**
     * 按周统计品牌销量
     * 执行时间每周的星期六
     */
    @Scheduled(cron = "0 10 10 ? * SAT")
    public void addWeekJob() {
        StatisticalVice statisticalVice = new StatisticalVice();
        StatisticalMain statisticalMain = new StatisticalMain();
        Long sales = Long.valueOf(0);
        List<Map> brandList = brandService.queryAllBrand();
        for (Map brand : brandList) {
            if (statisticalViceService.queryWeekShopSalce((Long) brand.get("u_id")).size() > 0) {
                statisticalMain.setId(GenericPo.createUID());
                //添加各门店的销量
                for (Map item : statisticalViceService.queryWeekShopSalce((Long) brand.get("u_id"))) {
                    sales = insertStatistical(statisticalVice, statisticalMain, item, sales);
                }
                statisticalMain.setTimeFrame(0);
                statisticalMain.setSales(sales);
                statisticalMain.setBrandId((Long) brand.get("u_id"));
                statisticalMain.setCreateTime(new Date());
                insert(statisticalMain);
            }
        }
        System.out.println("按周统计品牌销量");
    }

    /**
     * 按月统计品牌销量
     * 执行时间每月的最后一天
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void addMonthJob() {

        StatisticalVice statisticalVice = new StatisticalVice();
        StatisticalMain statisticalMain = new StatisticalMain();
        Long sales = Long.valueOf(0);
        List<Map> brandList = brandService.queryAllBrand();
        for (Map brand : brandList) {
            if (statisticalViceService.queryMonthShopSalce((Long) brand.get("u_id")).size() > 0) {
                statisticalMain.setId(GenericPo.createUID());
                //添加各门店的销量
                for (Map item : statisticalViceService.queryMonthShopSalce((Long) brand.get("u_id"))) {
                    sales = insertStatistical(statisticalVice, statisticalMain, item, sales);
                }
                statisticalMain.setTimeFrame(1);
                statisticalMain.setSales(sales);
                statisticalMain.setBrandId((Long) brand.get("u_id"));
                statisticalMain.setCreateTime(new Date());
                insert(statisticalMain);
            }
        }

        System.out.println("按月统计品牌销量");
    }

    /**
     * 按季度统计品牌销量
     * 执行时间每季度的最后一个月的最后一天
     */
    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
    public void addQuarterJob() {
        StatisticalVice statisticalVice = new StatisticalVice();
        StatisticalMain statisticalMain = new StatisticalMain();
        Long sales = Long.valueOf(0);
        List<Map> brandList = brandService.queryAllBrand();
        for (Map brand : brandList) {
            if (statisticalViceService.queryQuarterShopSalce((Long) brand.get("u_id")).size() > 0) {
                statisticalMain.setId(GenericPo.createUID());
                //添加各门店的销量
                for (Map item : statisticalViceService.queryQuarterShopSalce((Long) brand.get("u_id"))) {
                    sales = insertStatistical(statisticalVice, statisticalMain, item, sales);
                }
                statisticalMain.setTimeFrame(2);
                statisticalMain.setSales(sales);
                statisticalMain.setBrandId((Long) brand.get("u_id"));
                statisticalMain.setCreateTime(new Date());
                insert(statisticalMain);
            }
        }
        System.out.println("按季度统计品牌销量");
    }

    /**
     * 按年统计品牌销量
     * 执行时间每年的一月1号
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
    public void addYearJob() {
        StatisticalVice statisticalVice = new StatisticalVice();
        StatisticalMain statisticalMain = new StatisticalMain();
        Long sales = Long.valueOf(0);
        List<Map> brandList = brandService.queryAllBrand();
        for (Map brand : brandList) {
            if (statisticalViceService.queryQuarterShopSalce((Long) brand.get("u_id")).size() > 0) {
                statisticalMain.setId(GenericPo.createUID());
                //添加各门店的销量
                for (Map item : statisticalViceService.queryYearSales((Long) brand.get("u_id"))) {
                    sales = insertStatistical(statisticalVice, statisticalMain, item, sales);
                }
                statisticalMain.setTimeFrame(3);
                statisticalMain.setSales(sales);
                statisticalMain.setBrandId((Long) brand.get("u_id"));
                statisticalMain.setCreateTime(new Date());
                insert(statisticalMain);
            }
        }
        System.out.println("按年统计品牌销量");
    }


    public Long insertStatistical(StatisticalVice statisticalVice, StatisticalMain statisticalMain, Map item, Long sales) {
        statisticalVice.setShopId((Long) item.get("shopId"));
        statisticalVice.setSales(Long.valueOf(item.get("sales").toString()));
        statisticalVice.setId(GenericPo.createUID());
        statisticalVice.setViceId(statisticalMain.getId());
        statisticalViceService.insert(statisticalVice);
        sales += Long.parseLong(item.get("sales").toString());
        return sales;
    }

    /**
     * 按周查询某月全部品牌或某品牌下全部门店的销量
     *
     * @param param
     * @return
     */
    @Override
    public List<Map> queryWeek(QueryParam param) {

        AddForNull addForNull = new AddForNull();
        List<Map> queryList;
        String selectType = (String) param.getParam().get("selectType");
        //根据选择条件判断统计类型: 0,按周 1,按月 2,按季 3,按年 4,按年显示全部周
        if ("0".equals(selectType)) {
            param.getParam().put("showTime", "%Y-%m");
        }
        if ("1".equals(selectType) || "2".equals(selectType) || "4".equals(selectType)) {
            param.getParam().put("showTime", "%Y");
        }
        if ("".equals(param.getParam().get("brandId"))) {
            queryList = getMapper().queryWeek(param);
        } else {
            if("".equals(param.getParam().get("shopId"))){
                //查询某品牌下某服装某类别销量
                queryList = getMapper().queryWeekByBrandId(param);
            } else{
                if("0".equals(selectType)){
                    Calendar selectTimeCal = Calendar.getInstance();
                    DateFormat format = new SimpleDateFormat("yyyy-MM");
                    Date selectTime = null;
                    try {
                        selectTime = format.parse(String.valueOf(param.getParam().get("selectTime")));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    selectTimeCal.setTime(selectTime);
                    int selectWeekForMonth = selectTimeCal.get(Calendar.WEEK_OF_YEAR) - 1;
                    int weekLength = selectTimeCal.getActualMaximum(Calendar.WEEK_OF_MONTH) - 2;
                    param.getParam().put("start",selectWeekForMonth);
                    param.getParam().put("end",selectWeekForMonth +weekLength);
                }
                queryList = getMapper().statisticalByShopId(param);
            }
        }
        return addForNull.addNull(queryList, (String) param.getParam().get("selectTime"),selectType);
    }


}
