package com.base.web.service;

import com.base.web.bean.Message;
import com.base.web.bean.common.PagerResult;
import com.base.web.bean.common.QueryParam;

import java.util.Map;

public interface MessageService extends GenericService<Message, Long>{
    PagerResult<Map> queryMessage(QueryParam param);
}
