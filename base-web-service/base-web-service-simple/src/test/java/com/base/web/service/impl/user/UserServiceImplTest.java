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

package com.base.web.service.impl.user;

import com.base.web.bean.common.DeleteParam;
import com.base.web.bean.common.InsertParam;
import com.base.web.bean.common.UpdateParam;
import com.base.web.bean.po.user.User;
import com.base.web.dao.role.UserRoleMapper;
import com.base.web.dao.user.UserMapper;
import com.base.web.service.module.ModuleService;
import com.base.web.service.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;

/**
 * @author
 */
public class UserServiceImplTest {

    @InjectMocks
    UserService userService = new UserServiceImpl();
    @Mock
    UserMapper                 userMapper;
    @Mock
    ModuleService              moduleService;
    @Mock
    UserRoleMapper             userRoleMapper;
    @Mock
    javax.validation.Validator validator;

    InsertParam<User> insertParam;

    @Before
    public void setup() {
        User user = new User();
        insertParam = new InsertParam<>(user);
        user.setUsername("admin");
        MockitoAnnotations.initMocks(this);
        when(userMapper.insert(insertParam)).thenReturn(1);
        when(userMapper.update(UpdateParam.build(new User()))).thenReturn(1);
        when(userMapper.delete(DeleteParam.build().where("id", "test"))).thenReturn(1);
    }

    @Test
    public void insert() throws Exception {
        Assert.assertNotNull(userService.insert(new User()));
    }

    @Test
    public void update() throws Exception {
        Assert.assertEquals(userService.update(new User()), 1);
    }

}