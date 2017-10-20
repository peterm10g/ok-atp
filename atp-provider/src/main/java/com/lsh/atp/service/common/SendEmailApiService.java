package com.lsh.atp.service.common;

import com.lsh.atp.core.service.email.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Project Name: lsh-atp
 * Created by peter on 16/10/18.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@Service
public class SendEmailApiService {

    @Autowired
    private SendEmailService sendEmailService;

    @Value("${to.user.email}")
    private String userEmail;

    @Value("${to.send.email.cir}")
    private String emailCir;



    public void sendEmailSync(String content,String title){
        try {
            String[] userEmails = userEmail.split(",");
            content = emailCir + " : " + content;
            sendEmailService.sendAsyn(content, title, userEmails);
        }catch (Exception e){

        }

    }
}
