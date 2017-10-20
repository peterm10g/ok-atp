package com.lsh.atp.core.service.email;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.concurrent.Callable;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/10/17.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
public class SendEmailJop implements Callable<Boolean> {


    private static Logger logger = LoggerFactory.getLogger(SendEmailService.class);

    /**
     * 标题
     */
    private String title;
    /**
     * push内容
     */
    private String content;
    /**
     * 发送器
     */
    private MailSender mailSender;
    /**
     * 接受者
     */
    private String[] toUsers;

    /**
     * @param title 标题
     * @param content 内容
     * @param toUsers 收件人
     * @param mailSender 发送
     */
    public SendEmailJop(String title, String content, String[] toUsers, MailSender mailSender) {
        this.title = title;
        this.content = content;
        this.toUsers = toUsers;
        this.mailSender = mailSender;
    }

    @Override
    public Boolean call() throws Exception {
        if(toUsers == null){
            return false;
        }
        if(StringUtils.isBlank(content)){
            return false;
        }
        SimpleMailMessage mail = new SimpleMailMessage();

        try {
            mail.setTo(toUsers);//接受者
            mail.setFrom("miaozhuang@lsh123.com");//发送者
            mail.setSubject(title);//主题
            mail.setText(content);//邮件内容
            mailSender.send(mail);
        } catch (MailException me){
            logger.error("邮件发送失败",me);
            return false;
        } catch (Exception e) {
            logger.error("邮件发送异常",e);
            return false;
        }

//        Thread.sleep(20000);

        logger.info("***************邮件发送");

        return true;
    }
}
