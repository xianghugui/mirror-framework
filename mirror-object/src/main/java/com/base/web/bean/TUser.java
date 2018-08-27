package com.base.web.bean;

import com.base.web.Run;
import com.base.web.bean.po.GenericPo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Geek、
 * @Date: Created in 10:32  2018/3/28
 */
public class TUser extends GenericPo<Long> {

    //用户端小程序
    private static final String APPID  = "wxd93e04f3989aad51";
    private static final String SECRET   = "67e1884473fa0c9d1d253ccd96ae76a3";

    //微信唯一标识
    private String openId;

    //年龄
    private Integer age;

    //性别
    private Integer sex;

    //用户名
    private String name;

    //密码
    private String password;

    //邮箱
    private String email;
    //用户微信端获取头像地址
    private String avatar;

    //联系电话
    private String phone;

    //吸引会员和下线购买分佣
    private BigDecimal earn;

    //会员上线（父级会员）
    private Long parentId;

    //会员角色ID
    private Long roleId;

    //当前会员所属区域ID
    private Integer areaId;

    //剩余试衣次数
    private Integer residueDegree;

    //身份证正反面关联ID
    private Long cardId;

    //状态：0：正常，1：注销
    private Integer status;

    //创建时间
    private Date createTime;

    public static String getAPPID() {
        return APPID;
    }

    public static String getSECRET() {
        return SECRET;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getEarn() {
        return earn;
    }

    public void setEarn(BigDecimal earn) {
        this.earn = earn;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public Integer getResidueDegree() {
        return residueDegree;
    }

    public void setResidueDegree(Integer residueDegree) {
        this.residueDegree = residueDegree;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public interface Property extends GenericPo.Property{
        String openId = "openId";
        String age = "age";
        String sex = "sex";
        String name = "name";
        String password = "password";
        String email = "email";
        String phone = "phone";
        String earn = "earn";
        String parentId = "parentId";
        String roleId = "roleId";
        String areaId = "areaId";
        String residueDegree = "residueDegree";
        String cardId = "cardId";
        String status = "status";
        String  avatar = "avatar";
        String createTime = "createTime";
    }
}
