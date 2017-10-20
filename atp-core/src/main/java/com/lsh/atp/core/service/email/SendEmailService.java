package com.lsh.atp.core.service.email;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * lsh-atp
 * Created by peter on 16/7/25.
 */
@Component
public class SendEmailService {

    private static Logger logger = LoggerFactory.getLogger(SendEmailService.class);


    @Autowired
    private MailSender mailSender;

    @Value("${mail.tilte.prefix:未设置配置}")
    private String mailPrefix;


    /**
     * 邮件发送同步
     * @param content
     * @param title
     * @param array
     * @return
     */
       public boolean send(String content,String title,String[] array) {
           logger.info("-------------send email");
           if(array == null){
                return false;
           }
           if(StringUtils.isBlank(content)){
               return false;
           }
           SimpleMailMessage mail = new SimpleMailMessage();

           try {
               StringBuilder sb = new StringBuilder();
               mail.setTo(array);//接受者
               mail.setFrom("miaozhuang@lsh123.com");//发送者
               mail.setSubject(sb.append(mailPrefix).append("库存预警-").append(title).toString());//主题
               mail.setText(content);//邮件内容
               mailSender.send(mail);
               logger.info("邮件发送成功!");
           } catch (MailException me){
               logger.error("邮件发送失败",me);
               return false;
           } catch (Exception e) {
               logger.error("邮件发送异常",e);
               return false;
           }

           return true;
       }

    /**
     * 邮件发送异步
     * @param content 内容
     * @param title 标题
     * @param toEmails 接收人
     * @return boolean
     */
    public boolean sendAsyn(String content,String title,String[] toEmails) {

        this.toSendPushAll(title,content,toEmails,mailSender);

        return true;
    }

    /**
     *
     * @param title 标题
     * @param content 内容
     * @param toEmails 接收人
     * @param mailSender 发送器
     */
    public void toSendPushAll(String title, String content, String[] toEmails, MailSender mailSender) {
        ExecutorService pool = ThreadPoolSet.getInstance().getThreadPool("sendPool");
        if(pool == null){
            logger.info("**************************新pool");
            pool = Executors.newFixedThreadPool(5);
            ThreadPoolSet.getInstance().addThreadPool("sendPool", pool);
        }else{
            logger.info("**************************用pool");
        }

        pool.submit(new SendEmailJop(title, content, toEmails, mailSender));

        //pool.shutdown();

    }


    }
