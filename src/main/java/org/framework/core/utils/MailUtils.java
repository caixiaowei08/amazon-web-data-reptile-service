package org.framework.core.utils;

import org.framework.core.common.pojo.EmailCodePojo;
import org.springframework.util.StringUtils;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by User on 2017/6/11.
 */
public class MailUtils {
    // 发件人的 邮箱 和 密码（替换为自己的邮箱和密码）
    // PS: 某些邮箱服务器为了增加邮箱本身密码的安全性，给 SMTP 客户端设置了独立密码（有的邮箱称为“授权码”）,
    //     对于开启了独立密码的邮箱, 这里的邮箱密码必需使用这个独立密码（授权码）。
    public static String EMAIL_ACCOUNT = "caixiaowei08@163.com";
    public static String EMAIL_PASSWORD = "1q2w3e4r";
    // 发件人邮箱的 SMTP 服务器地址, 必须准确, 不同邮件服务器地址不同, 一般(只是一般, 绝非绝对)格式为: smtp.xxx.com
    // 网易163邮箱的 SMTP 服务器地址为: smtp.163.com
    public static String EMAIL_SMTP_HOST = "smtp.163.com";
    // 收件人邮箱（替换为自己知道的有效邮箱）
    public static String RECEIVE_MAIL_ACCOUNT = "1005814292@qq.com";

    public static Boolean  sendEmail(EmailCodePojo emailCodePojo) throws Exception{
        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", EMAIL_SMTP_HOST);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        Session session = Session.getDefaultInstance(props);
        session.setDebug(true); // 设置为debug模式, 可以查看详细的发送 log
        //创建一封邮件
        MimeMessage message = createMimeMessage(session, EMAIL_ACCOUNT, emailCodePojo);
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        transport.connect(EMAIL_ACCOUNT, EMAIL_PASSWORD);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
        return true;
    }

    /**
     * 创建一封只包含文本的简单邮件
     * @param session 和服务器交互的会话
     * @param sendMail 发件人邮箱
     * @return
     * @throws Exception
     */
    public static MimeMessage createMimeMessage(Session session, String sendMail, EmailCodePojo emailCodePojo) throws Exception {
        // 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        // 2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, "Review Tracker", "UTF-8"));
        // 3. To: 收件人（可以增加多个收件人、抄送、密送）
        int end  = emailCodePojo.getEmail().indexOf('@');
        String emailName = emailCodePojo.getEmail().substring(0,end);
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(emailCodePojo.getEmail(), emailName, "UTF-8"));
        // 4. Subject: 邮件主题
        message.setSubject(emailCodePojo.getSubject(), "UTF-8");
        // 5. Content: 邮件正文（可以使用html标签）
        message.setContent(emailCodePojo.getContent(), "text/html;charset=UTF-8");
        // 6. 设置发件时间
        message.setSentDate(new Date());
        // 7. 保存设置
        message.saveChanges();
        return message;
    }

    public static Boolean isEmail(String email){
        if(StringUtils.hasText(email)){
            String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            return matcher.matches();
        }else{
            return false;
        }
    }

    public static String getRandomNum(){
        String str="0123456789";
        StringBuilder sb=new StringBuilder(4);
        for(int i=0;i<4;i++)
        {
            char ch=str.charAt(new Random().nextInt(str.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
