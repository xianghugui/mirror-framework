package com.base.web.service.impl;

import com.base.web.bean.FaceFeature;
import com.base.web.bean.FaceFeature;
import com.base.web.dao.FaceFeatureMapper;
import com.base.web.service.FaceFeatureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("FaceFeatureService")
public class FaceFeatureServiceImpl extends AbstractServiceImpl<FaceFeature, Long> implements FaceFeatureService{

    @Resource
    private FaceFeatureMapper FaceFeatureMapper;
    
    @Override
    protected FaceFeatureMapper getMapper() {
        return this.FaceFeatureMapper;
    }
}
