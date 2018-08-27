package com.base.web.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.base.web.bean.UserAgent;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.UserAgentMapper;
import com.base.web.dao.resource.FileRefMapper;
import com.base.web.service.UserAgentService;
import com.base.web.util.ResourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("UserAgentService")
public class UserAgentServiceImpl extends AbstractServiceImpl<UserAgent, Long> implements UserAgentService {

    @Resource
    private UserAgentMapper userAgentMapper;


    @Resource
    private FileRefMapper fileRefMapper;


    @Override
    protected UserAgentMapper getMapper() {
        return this.userAgentMapper;
    }


    @Override
    public PagerResult<Map> userAgentGoodsListPagerByUserId(QueryParam queryParam, HttpServletRequest req) {
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().totalUserAgentGoods(queryParam);
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
            queryParam.rePaging(total);
            List<Map> mapList = getMapper().userAgentGoodsListPagerByUserId(queryParam);
            if(mapList.size() > 0){
                for(Map map : mapList){
                    Long recordId = (Long) map.get("refImageId");

                    queryParam.getParam().put("recordId",recordId);//关联id
                    queryParam.getParam().put("dataType",2);//资源类型  <!--1营业执照id，2，商品详情图片id，3商品轮播id,4：店铺LOGO，5：店铺图片-->

                    List<Map> mapList1 = fileRefMapper.queryResourceByRecordId(queryParam);
                    if (mapList1.size() > 0){
                        for (Map imageMap : mapList1){
                            map.put("goodsImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(imageMap.get("resourceId")).trim()));
                            break;
                        }
                    }
                }
            }
            pagerResult.setData(mapList);
        }
        return pagerResult;
    }

    @Override
    public Map userAgentGoodsSpread(Long goodsId, QueryParam queryParam, HttpServletRequest req) {
        Map map = getMapper().userAgentGoodsSpread(goodsId);

        queryParam.getParam().put("recordId",map.get("goodsImageId"));
        queryParam.getParam().put("dataType",2);
        //商品图片地址列表
        List<Map> goodsImageList =  fileRefMapper.queryResourceByRecordId(queryParam);
         if (goodsImageList.size() > 0){
             for (Map goodsImage: goodsImageList) {
                 goodsImage.put("goodsImageUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(goodsImage.get("md5")).trim()));
             }
         }

        queryParam.getParam().put("recordId",map.get("carouselId"));
        queryParam.getParam().put("dataType",3);
        //商品轮播地址列表
        List<Map> goodsCarouseList =  fileRefMapper.queryResourceByRecordId(queryParam);
        if (goodsImageList.size() > 0){
            for (Map goodsCarouse: goodsCarouseList) {
                goodsCarouse.put("goodsCarouseUrl", ResourceUtil.resourceBuildPath(req, String.valueOf(goodsCarouse.get("md5")).trim()));
            }
        }

        map.put("goodsImageList",goodsImageList);
        map.put("goodsCarouseList",goodsCarouseList);

        return map;
    }

    // 插入平台服装代理表
    @Override
    public int insertAgent(JSONObject goods){
        UserAgent agent = new UserAgent();
        agent.setUserId(WebUtil.getLoginUser().getId());
        agent.setDealTime(new Date());
        agent.setStatus(0);
        agent.setGoodsId(Long.valueOf(goods.get("goodsId").toString()));
            agent.setId(GenericPo.createUID());
            return getMapper().insert(InsertParam.build(agent));
    }
}
