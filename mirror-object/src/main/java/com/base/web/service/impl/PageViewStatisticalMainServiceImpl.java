package com.base.web.service.impl;

import com.base.web.bean.PageViewStatisticalMain;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.PageViewStatisticalMainMapper;
import com.base.web.dao.PageViewStatisticalViceMapper;
import com.base.web.dao.ShopBrandMapper;
import com.base.web.service.PageViewStatisticalMainService;
import com.base.web.util.AddForNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: FQ
 * @Date: Created in 11:18  2018/8/6
 */
@Configuration
@EnableScheduling
@EnableAsync
@Service("PageViewStatisticalMainService")
public class PageViewStatisticalMainServiceImpl extends AbstractServiceImpl<PageViewStatisticalMain, Long> implements PageViewStatisticalMainService {

    @Resource
    private PageViewStatisticalMainMapper pageViewStatisticalMainMapper;

    @Resource
    private ShopBrandMapper ShopBrandMapper;

    @Resource
    private PageViewStatisticalViceMapper pageViewStatisticalViceMapper;

    @Override
    protected PageViewStatisticalMainMapper getMapper() {
        return this.pageViewStatisticalMainMapper;
    }

    /**
     * 按周统计品牌销量
     * 执行时间每周的星期六
     */
    @Scheduled(cron = "0 35 23 * * SAT")
//    @Scheduled(cron = "10 * * * * ?")
    @Async
    public void addWeekJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> brandList = ShopBrandMapper.queryShopBrand();
        List<Map> queryList;
        Long sales = 0L;
        Long pageView = 0L;
        if(brandList != null) {
            for (Map brand : brandList) {
                queryList = pageViewStatisticalViceMapper.queryPageViewAndSales((Long) brand.get("id"));
                if (queryList.size() > 0) {
                    pageViewStatisticalMain.setId(GenericPo.createUID());
                    //添加各门店的销量
                    for (Map item : queryList) {
                        item.put("id", GenericPo.createUID());
                        item.put("viceId", pageViewStatisticalMain.getId());
                        sales += Long.parseLong(item.get("sales").toString());
                        pageView += Long.parseLong(item.get("pageView").toString());
                    }
                    pageViewStatisticalViceMapper.insertPageViewForShop(queryList);
                    pageViewStatisticalMain.setSales(sales);
                    pageViewStatisticalMain.setPageView(pageView);
                    pageViewStatisticalMain.setTimeFrame(0);
                    pageViewStatisticalMain.setBrandId((Long) brand.get("id"));
                    pageViewStatisticalMain.setCreateTime(new Date());
                    insert(pageViewStatisticalMain);
                }
            }
        getMapper().pageViewClear();
        }
        System.out.println("定时按周统计浏览量");
    }

    /**
     * 按月统计品牌销量
     * 执行时间每月的1号
     */
    @Scheduled(cron = "0 0 0 1 * ?")
//    @Scheduled(cron = "10 * * * * ?")
    @Async
    public void addMonthJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryMonthPageViewAndSales();
        if(queryList != null) {
            for (Map item : queryList) {
                pageViewStatisticalMain.setId(GenericPo.createUID());
                pageViewStatisticalMain.setSales(Long.valueOf(item.get("sales").toString()));
                pageViewStatisticalMain.setPageView(Long.valueOf( item.get("pageView").toString()));
                pageViewStatisticalMain.setTimeFrame(1);
                pageViewStatisticalMain.setBrandId(Long.valueOf( item.get("brandId").toString()));
                pageViewStatisticalMain.setCreateTime(new Date());
                insert(pageViewStatisticalMain);
            }
        }
        System.out.println("定时按月统计浏览量");
    }

    /**
     * 按季度统计品牌销量
     * 执行时间季度的1号
     */
    @Scheduled(cron = "0 0 0 1 3,6,9,12 ?")
//    @Scheduled(cron = "10 * * * * ?")
    @Async
    public void addQuarterJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryQuarterPageViewAndSales();
        if(queryList != null) {
            for (Map item : queryList) {
                pageViewStatisticalMain.setId(GenericPo.createUID());
                pageViewStatisticalMain.setSales(Long.valueOf(item.get("sales").toString()));
                pageViewStatisticalMain.setPageView(Long.valueOf( item.get("pageView").toString()));
                pageViewStatisticalMain.setTimeFrame(2);
                pageViewStatisticalMain.setBrandId(Long.valueOf( item.get("brandId").toString()));
                pageViewStatisticalMain.setCreateTime(new Date());
                insert(pageViewStatisticalMain);
            }
        }
        System.out.println("定时按季统计浏览量");
    }

    /**
     * 按年统计品牌销量
     * 执行时间每年的一月1号
     */
    @Scheduled(cron = "0 0 0 1 1 ?")
//    @Scheduled(cron = "10 * * * * ?")
    @Async
    public void addYearJob() {
        PageViewStatisticalMain pageViewStatisticalMain = new PageViewStatisticalMain();
        List<Map> queryList = pageViewStatisticalViceMapper.queryYearPageViewAndSales();
        if(queryList != null) {
            for (Map item : queryList) {
                pageViewStatisticalMain.setId(GenericPo.createUID());
                pageViewStatisticalMain.setSales(Long.valueOf(item.get("sales").toString()));
                pageViewStatisticalMain.setPageView(Long.valueOf( item.get("pageView").toString()));
                pageViewStatisticalMain.setTimeFrame(3);
                pageViewStatisticalMain.setBrandId(Long.valueOf( item.get("brandId").toString()));
                pageViewStatisticalMain.setCreateTime(new Date());
                insert(pageViewStatisticalMain);
            }
        }
        System.out.println("定时按年统计浏览量");
    }

    /**
     *
     * 根据筛选条件查询数据
     * @param param
     * @return
     */
    @Override
    public List<Map> queryPageView(QueryParam param) {
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
            if ("".equals(param.getParam().get("shopId"))) {
                //查询某品牌下某服装某类别销量
                queryList = getMapper().queryPageViewForShop(param);
            } else {
                queryList = getMapper().queryPageViewForGoods(param);
            }
        }
        return addForNull.addNull(queryList, (String) param.getParam().get("selectTime"), selectType);
    }

}
