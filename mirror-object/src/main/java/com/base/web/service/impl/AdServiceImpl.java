package com.base.web.service.impl;

import com.base.web.bean.Ad;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.po.GenericPo;
import com.base.web.dao.AdMapper;
import com.base.web.service.AdService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("AdService")
public class AdServiceImpl extends AbstractServiceImpl<Ad, Long> implements AdService{

    @Resource
    private AdMapper adMapper;

    @Override
    protected AdMapper getMapper() {
        return this.adMapper;
    }

    //插入推送广告表
    @Override
    public Long insert(Ad data){
        if(data.getId() == null){
            data.setId(GenericPo.createUID());
        }
        data.setStatus(0);
        data.setCreateTime(new Date());
        tryValidPo(data);
        int flag=adMapper.insert(InsertParam.build(data));
        if (flag == 1){
            return data.getId();
        }
        return null;
    }

    //修改推送广告表
    @Override
    public int update(Ad data){
        tryValidPo(data);
        int flag = createUpdate().fromBean(data).where(GenericPo.Property.id).exec();
        return flag;
    }

    //查询推送广告表
    @Override
    public List<Ad> select(QueryParam param){
        return adMapper.select(param);
    }

    //根据id查询推送广告表
    @Override
    public Ad selectByPk(Long id){
        return adMapper.selectByPk(id);
    }

    @Override
    public int updateStatusByUId(Long uId,Integer status){
        return adMapper.updateStatusByUId(uId,status);
    }

    @Override
    public PagerResult<Map> queryAllAd(QueryParam param){
        PagerResult<Map> pagerResult = new PagerResult<>();
        param.setPaging(true);
        int total = getMapper().queryAllAdTotal(param);

        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            //根据实际记录数量重新指定分页参数
//            param.rePaging(total);
            pagerResult.setData(getMapper().queryAllAd(param));
        }
        return pagerResult;
    }

}
