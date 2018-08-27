package com.base.web.dao;


import com.base.web.bean.Message;
import com.base.web.bean.common.QueryParam;

import java.util.List;
import java.util.Map;

public interface MessageMapper extends GenericMapper<Message, Long>{
    List<Map> queryMessage(QueryParam param);

    Integer queryMessageTotal();
}
