package com.base.web.controller;

import com.base.web.bean.StatisticalMain;
import com.base.web.bean.common.QueryParam;
import com.base.web.core.authorize.annotation.Authorize;
import com.base.web.core.logger.annotation.AccessLogger;
import com.base.web.core.message.ResponseMessage;
import com.base.web.service.ShopBrandService;
import com.base.web.service.StatisticalMainService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.base.web.core.message.ResponseMessage.ok;

@RestController
@RequestMapping(value = "/StatisticalMain")
@AccessLogger("统计")
public class StatisticalMainController extends GenericController<StatisticalMain,Long>{
    @Resource
    private StatisticalMainService statisticalMainService;

    @Resource
    private ShopBrandService shopBrandService;

    @Override
    public StatisticalMainService getService(){
        return this.statisticalMainService;
    }

    @RequestMapping(value = "/queryWeek", method = RequestMethod.GET)
    @AccessLogger("按筛选条件统计销量")
    @Authorize(action = "R")
    //selectType 查询类型: 0,按周  1,按月  2,按季 3, 按年
    public ResponseMessage queryWeek(String selectTime,String brandId,
                                     String selectType,String sort,String shopId) {
        QueryParam param = new QueryParam();
        param.getParam().put("selectTime",selectTime);
        param.getParam().put("selectType",selectType);
        param.getParam().put("brandId",brandId);
        param.getParam().put("sort",sort);
        param.getParam().put("shopId",shopId);
        return ok(getService().queryWeek(param));
    }

    @RequestMapping(value = "/queryAllShop", method = RequestMethod.GET)
    @AccessLogger("查询某品牌下的全部门店")
    @Authorize(action = "R")
    public ResponseMessage queryAllShop(@RequestParam("brandId") String brandId) {
        if(brandId != null && !"".equals(brandId)) {
            List<Map> shopList = shopBrandService.queryAllShopByBrandId(brandId);
            return ok(shopList);
        }
        return ok("数据为空");
    }
}
