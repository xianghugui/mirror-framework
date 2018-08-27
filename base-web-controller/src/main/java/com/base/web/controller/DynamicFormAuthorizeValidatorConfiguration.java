package com.base.web.controller;

import com.base.web.bean.po.user.User;
import com.base.web.core.authorize.ExpressionScopeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by   on 16-5-16.
 */
@Component("dynamicFormAuthorizeValidator")
public class DynamicFormAuthorizeValidatorConfiguration implements ExpressionScopeBean {

    @Autowired(required = false)
    private List<DynamicFormAuthorizeValidator> dynamicFormAuthorizeValidators;

    public boolean validate(String formName, User user, Map<String,Object> param, String... actions) {
        if (dynamicFormAuthorizeValidators != null) {
            for (DynamicFormAuthorizeValidator validator : dynamicFormAuthorizeValidators) {
                if (validator.validate(formName, user, param, actions)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
}
