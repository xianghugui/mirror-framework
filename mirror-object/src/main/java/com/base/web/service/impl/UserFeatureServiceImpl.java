package com.base.web.service.impl;

import com.base.web.bean.UserFeature;
import com.base.web.core.utils.WebUtil;
import com.base.web.dao.UserFeatureMapper;
import com.base.web.service.UserFeatureService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;


/**
 * @Author: Geek、
 * @Date: Created in 14:08  2018/3/27
 */
@Service("UserFeatureService")
public class UserFeatureServiceImpl extends AbstractServiceImpl<UserFeature, Long> implements UserFeatureService {

    @Resource
    private UserFeatureMapper userFeatureMapper;


    @Override
    protected UserFeatureMapper getMapper() {
        return this.userFeatureMapper;
    }


    //>>查询用户身材信息
    @Override
    public Map queryUserFeature(){
        return getMapper().queryUserFeature(WebUtil.getLoginUser().getId());
    }


}
