package com.base.web.service;

import com.base.web.bean.UserShare;

import java.util.List;
import java.util.Map;

/**
 * @Author: ChenXiaohui
 * @Date: Created in 10:42  2018/3/29
 */
public interface UserShareService extends GenericService<UserShare, Long> {
    List<Map> buyList( Long goodsId,  Long parentId);
    List<Map> userList( Long goodsId,  Long parentId);
}
