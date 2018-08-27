package com.base.web.service.impl;

import com.base.web.bean.TryTimeRecord;
import com.base.web.dao.TryTimeRecordMapper;
import com.base.web.service.TryTimeRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("TryTimeRecordService")
public class TryTimeRecordServiceImpl extends AbstractServiceImpl<TryTimeRecord, Long>  implements TryTimeRecordService {

    @Resource
    private TryTimeRecordMapper tryTimeRecordMapper;
    @Override
    protected TryTimeRecordMapper getMapper() {
        return this.tryTimeRecordMapper;
    }
}
