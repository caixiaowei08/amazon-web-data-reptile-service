package org.framework.core.global.service;

import com.amazon.admin.account.entity.AdminSystemEntity;
import com.amazon.service.user.entity.UserEntity;
import org.framework.core.common.pojo.EmailCodePojo;
import org.framework.core.common.service.BaseService;

/**
 * Created by User on 2017/6/10.
 */
public interface GlobalService extends BaseService{

    /**
     * 邮件发送
     */
    public boolean sendEmail() throws Exception;

    /**
     * 发送邮箱验证码
     * @param emailCodePojo
     * @return
     */
    public boolean sendEmailEmailCodePojo(EmailCodePojo emailCodePojo);


    public UserEntity getUserEntityFromSession();

    public AdminSystemEntity getAdminEntityFromSession();

    public Boolean isNotAdminLogin();

    public String generateOrderNumber();


}
