package com.base.web.controller;

import com.base.web.bean.PageViewStatisticalMain;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.PageViewStatisticalMainService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.base.web.core.message.ResponseMessage.ok;

@RestController
@RequestMapping(value = "/pageViewStatisticalMain")
@AccessLogger("浏览量统计")
public class PageViewStatisticalMainController extends GenericController<PageViewStatisticalMain,Long>{
    @Resource
    private PageViewStatisticalMainService pageViewStatisticalMainService;

    @Override
    public PageViewStatisticalMainService getService(){
        return this.pageViewStatisticalMainService;
    }

    @RequestMapping(value = "/queryPageView", method = RequestMethod.GET)
    @AccessLogger("按筛选条件统计销量")
    @Authorize(action = "R")
    //selectType 查询类型: 0,按周  1,按月  2,按季 3, 按年
    public ResponseMessage queryPageView(String selectTime,String brandId,
                                     String selectType,String sort,String shopId) {
        QueryParam param = new QueryParam();
        param.getParam().put("selectTime",selectTime);
        param.getParam().put("selectType",selectType);
        param.getParam().put("brandId",brandId);
        param.getParam().put("sort",sort);
        param.getParam().put("shopId",shopId);
        return ok(getService().queryPageView(param));
    }


}
