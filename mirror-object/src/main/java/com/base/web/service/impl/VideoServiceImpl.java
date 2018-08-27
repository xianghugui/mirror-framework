package com.base.web.service.impl;

import com.base.web.bean.Video;
import com.base.web.dao.VideoMapper;
import com.base.web.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * @Author: Geek、
 * @Date: Created in 10:03  2018/3/28
 */
@Service("VideoService")
public class VideoServiceImpl extends AbstractServiceImpl<Video, Long> implements VideoService{

    @Resource
    private VideoMapper VideoMapper;
    
    @Override
    protected VideoMapper getMapper() {
        return this.VideoMapper;
    }
}
