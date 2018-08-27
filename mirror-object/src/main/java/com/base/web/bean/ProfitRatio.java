package com.base.web.bean;

import com.base.web.bean.po.GenericPo;


/**
 * @Author: Geek、
 * @Date: Created in 11:12  2018/3/27
 */
public class ProfitRatio extends GenericPo<Long> {

    //角色id
    private Long roleId;

  //分润比
    private Integer profitRatio;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getProfitRatio() {
        return profitRatio;
    }

    public void setProfitRatio(Integer profitRatio) {
        this.profitRatio = profitRatio;
    }

    public interface Property extends GenericPo.Property{
        String roleId = "roleId";
        String profitRatio = "profitRatio";
    }
}
