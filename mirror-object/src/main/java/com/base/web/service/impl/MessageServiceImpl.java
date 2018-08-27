package com.base.web.service.impl;

import com.base.web.bean.Message;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;
import com.base.web.dao.MessageMapper;
import com.base.web.service.MessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;

@Service("MessageService")
public class MessageServiceImpl extends AbstractServiceImpl<Message, Long> implements MessageService {
    @Resource
    private MessageMapper messageMapper;

    @Override
    protected MessageMapper getMapper() {
        return this.messageMapper;
    }

    @Override
    public PagerResult<Map> queryMessage(QueryParam param){
        PagerResult<Map> pagerResult = new PagerResult<>();
        Integer total = getMapper().queryMessageTotal();
        pagerResult.setTotal(total);
        if (total == 0) {
            pagerResult.setData(new ArrayList<>());
        } else {
            pagerResult.setData(getMapper().queryMessage(param));
        }
        return pagerResult;
    }
}
