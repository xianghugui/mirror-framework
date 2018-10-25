package com.base.web.service.impl;

import com.base.web.bean.RefundExchange;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.resource.FileRef;
import com.base.web.dao.RefundExchangeMapper;
import com.base.web.service.RefundExchangeService;
import com.base.web.service.resource.FileRefService;
import com.base.web.util.OSSUtils;
import com.base.web.util.ResourceUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("RefundExchangeService")
public class RefundExchangeServiceImpl extends AbstractServiceImpl<RefundExchange, Long> implements RefundExchangeService {

    @Resource
    private RefundExchangeMapper refundExchangeMapper;

    @Resource
    private FileRefService fileRefService;

    @Resource
    private OSSUtils ossUtils;

    @Override
    public PagerResult showRefunds(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().showRefunds(param);
        String[] array = null;
        for (int i = 0; i < list.size(); i++) {
            judgeResourceType(param, list, i);
            array = list.get(i).get("array").toString().split(",");
            param.getParam().put("recordId",array[1]);
            list.get(i).put("imageSrc", fileRefService.queryResourceByRecordId(param, req));
            list.get(i).put("orderId", array[0]);
            list.get(i).put("userName", array[2]);
//            list = addImageSrcAndVideoSrc(list, param, req, i, array[1]);
        }
        return new PagerResult(getMapper().showRefundsTotal(param), list);
    }

    //判断查询的资源类型
    private void judgeResourceType(QueryParam param, List<Map> list, int i) {
        if ("2".equals(list.get(i).get("type").toString())) {
            param.getParam().put("type", 1);
            param.getParam().put("dataType", null);
        } else {
            param.getParam().put("dataType", 2);
            param.getParam().put("type", null);
        }
    }

    @Override
    public PagerResult showOrderRefunds(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().showOrderRefunds(param);
        return new PagerResult(getMapper().showOrderRefundsTotal(param), fileRefService.addImages(list, 2, req));
    }

    @Override
    public PagerResult showTryGoodsRefunds(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().showTryGoodsRefunds(param);
        return new PagerResult(getMapper().showTryGoodsRefundsTotal(param), fileRefService.addImages(list, 2, req));
    }

    @Override
    public PagerResult showVideoOrderRefunds(QueryParam param, HttpServletRequest req) {
        List<Map> list = getMapper().showVideoOrderRefunds(param);
        return new PagerResult(getMapper().showVideoOrderRefundsTotal(param), ossUtils.jointUrl(list));
    }

    @Override
    public Map showRefundsInfo(QueryParam param) {
        Map RefundsInfo;
        if("0".equals(param.getParam().get("orderType").toString())){
            RefundsInfo = getMapper().showOrderRefundsInfo(param.getParam().get("refundId").toString());
        }
        else{
            RefundsInfo = getMapper().showVideoRefundsInfo(param.getParam().get("refundId").toString());
        }
        return RefundsInfo;
    }

    @Override
    public int agreeRefunds(String refundId) {
        return getMapper().agreeRefunds(new Date(), refundId);
    }

    @Override
    public int refuseRefunds(String refundId, Long imageId, String refuseReason) {
        return getMapper().refuseRefunds(new Date(), refundId, imageId, refuseReason);
    }

    @Override
    public int addRefundExchange(RefundExchange refundExchange) {
        return getMapper().insert(InsertParam.build(refundExchange));
    }

    @Override
    public PagerResult clientShowRefunds(QueryParam param, HttpServletRequest req) {
//        购物单
        List<Map> list;
        int total;
        if("1".equals(param.getParam().get("type").toString())){
            list = getMapper().clientShowOrderRefunds(param);
            total = getMapper().clientShowOrderRefundsTotal(param);
            if(list.size() > 0){
                for (Map map : list){
                    map.put("imageSrc",ResourceUtil.resourceBuildPath(req, String.valueOf(map.get("compressId")).trim()));
                }
            }
//            询价单
        }else {
            list = getMapper().clientShowVideoOrderRefunds(param);
            total = getMapper().clientShowVideoOrderRefundsTotal(param);
            if(list.size() > 0){
                for (Map map : list){
                    FileRef fileRef = fileRefService.createQuery()
                            .where(FileRef.Property.refId,map.get("videoSrc"))
                            .and(FileRef.Property.type, 0).single();
                    map.put("imageSrc",ResourceUtil.resourceBuildPath(req, String.valueOf(fileRef.getResourceId()).trim()));
                    FileRef fileRef1 = fileRefService.createQuery()
                            .where(FileRef.Property.refId,map.get("videoSrc"))
                            .and(FileRef.Property.type, 1).single();
                    map.put("videoSrc",ResourceUtil.resourceBuildPath(req, String.valueOf(fileRef1.getResourceId()).trim(), ".MP4"));
                }
            }
        }

        return new PagerResult(total, list);
    }

    @Override
    public Map showClientRefundAddress(Long orderId) {
        return getMapper().showClientRefundAddress(orderId);
    }

    @Override
    public int delivery(Long orderId, String expressId, String expressNumber) {
        return getMapper().delivery(orderId, expressId, expressNumber);
    }

    @Override
    public List<Map> refund() {
        return getMapper().refund();
    }


    @Override
    protected RefundExchangeMapper getMapper() {
        return this.refundExchangeMapper;
    }

    public List<Map> addImageSrcAndVideoSrc(List<Map> list, QueryParam param, HttpServletRequest req, int i, String s) {
        if ("2".equals(list.get(i).get("type").toString())) {
            list = fileRefService.addVideos(list, req);
        } else {
            param.getParam().put("recordId", s);
            List<Map> imgs = fileRefService.queryResourceByRecordId(param, req);
            if (imgs != null && imgs.size() > 0) {
                list.get(i).put("imageSrc", imgs);
            }
        }
        list.get(i).remove("array");
        return list;
    }
}
