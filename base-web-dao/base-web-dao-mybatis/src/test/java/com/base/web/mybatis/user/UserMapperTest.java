/*
 * Copyright 2015-2016 http://base.me
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.base.web.mybatis.user;

import org.hsweb.ezorm.core.dsl.Query;
import com.base.web.bean.common.DeleteParam;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.QueryParam;
import com.base.web.bean.common.UpdateParam;
import com.base.web.bean.po.user.User;
import com.base.web.dao.user.UserMapper;
import com.base.web.mybatis.AbstractTestCase;
import com.base.web.mybatis.plgins.pager.Pager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Date;

/**
 * @author
 */
public class UserMapperTest extends AbstractTestCase {

    @Autowired
    private UserMapper userMapper;


    @Test
    public void testInsert() {
        User user = new User();
        user.setId(Long.valueOf(1008611));
        user.setUsername("admin");
        user.setName("test");
        user.setCreateDate(new Date());
        int i = userMapper.insert(InsertParam.build(user));
        userMapper.update(UpdateParam.build(user));
        userMapper.delete(DeleteParam.build().where("id", "111"));
        Assert.assertEquals(i, 1);
    }

    @Test
    public void testQuery() {
        User user = new User();
        user.setId(Long.valueOf(1008611));
        user.setUsername("admin");
        user.setName("test");
        Pager.doPaging(0, 20);
        Query.forList(userMapper::select, new QueryParam())
                .sql("username is not null")
                .or().sql("username=#{username}", Collections.singletonMap("username", "root"))
                .fromBean(user)
                .$like$("username").list();
    }
}
