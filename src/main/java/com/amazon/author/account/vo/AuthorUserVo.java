package com.amazon.author.account.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/8/4.
 */
public class AuthorUserVo implements Serializable {

    private Integer id;

    private String account;

    private String oldPwd;

    private String newPwd;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
}
