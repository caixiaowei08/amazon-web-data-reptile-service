package org.framework.core.common.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by User on 2017/6/11.
 */
public class EmailCodePojo implements Serializable{
    /**
     * 验证码
     */
    private String code ;
    /**
     * 产生时间
     */
    private Date createTime;
    /**
     * 失效时间
     */
    private Date invalidTime;
    /**
     * 验证邮箱
     */
    private String email;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 邮件内容
     */
    private String content;




    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(Date invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
