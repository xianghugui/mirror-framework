package com.base.web.service.impl;

import com.base.web.bean.PageViewStatisticalVice;
import com.base.web.dao.PageViewStatisticalViceMapper;
import com.base.web.service.PageViewStatisticalViceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author: FQ
 * @Date: Created in 11:18  2018/8/6
 */

@Service("PageViewStatisticalViceService")
public class PageViewStatisticalViceServiceImpl extends AbstractServiceImpl<PageViewStatisticalVice,Long> implements PageViewStatisticalViceService {
    @Resource
    private PageViewStatisticalViceMapper pageViewStatisticalViceMapper;

    @Override
    protected PageViewStatisticalViceMapper getMapper(){
        return this.pageViewStatisticalViceMapper;
    }


}
