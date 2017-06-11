package org.framework.core.global.service.impl;

import org.framework.core.common.pojo.EmailCodePojo;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.global.service.GlobalService;
import org.framework.core.utils.MailUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/10.
 */
@Service("globalService")
@Transactional
public class GlobalServiceImpl extends BaseServiceImpl implements GlobalService {

    public boolean sendEmail() throws Exception{
        return false;
    }

    public boolean sendEmailEmailCodePojo(EmailCodePojo emailCodePojo) {
        emailCodePojo.setSubject("Seller Assistant找回密码，发送验证码！");
        emailCodePojo.setContent("您的验证码是"+emailCodePojo.getCode()+",请在10分钟内使用该验证码完成密码修改！");
        try {
            MailUtils.sendEmail(emailCodePojo);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
