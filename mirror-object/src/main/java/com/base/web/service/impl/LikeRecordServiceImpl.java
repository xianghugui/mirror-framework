package com.base.web.service.impl;

import com.base.web.bean.LikeRecord;
import com.base.web.dao.LikeRecordMapper;
import com.base.web.service.LikeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("LikeRecordService")
public class LikeRecordServiceImpl extends AbstractServiceImpl<LikeRecord, Long> implements LikeRecordService {
    @Resource
    private LikeRecordMapper LikeRecordMapper;

    @Override
    public LikeRecordMapper getMapper(){
        return this.LikeRecordMapper;
    }

}
