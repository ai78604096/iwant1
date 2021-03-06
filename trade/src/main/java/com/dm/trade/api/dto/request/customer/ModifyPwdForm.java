package com.dm.trade.api.dto.request.customer;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author zhongchao
 * @title ModifyPwdForm.java
 * @Date 2018-03-20
 * @since v1.0.0
 */
public class ModifyPwdForm {

    @NotNull(message = "手机号码不能为空")
    private String phone;

    /**
     * 原密码
     */
    @NotNull(message = "原密码不能为空")
    private String oldPwd;

    /**
     * 新密码
     */
    @NotNull(message = "新密码不能为空")
    @Length(min = 6,  message = "密码至少6位")
    private String newPwd;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    @Override
    public String toString() {
        return "ModifyPwdForm{" +
                "phone='" + phone + '\'' +
                ", oldPwd='" + oldPwd + '\'' +
                ", newPwd='" + newPwd + '\'' +
                '}';
    }
}
