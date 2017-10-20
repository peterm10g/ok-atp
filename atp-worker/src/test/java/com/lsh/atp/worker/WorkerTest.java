package com.lsh.atp.worker;

import com.lsh.atp.core.service.email.SendEmailService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by huangdong on 16/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class WorkerTest {

    @Autowired
    private SendEmailService service;
    @Test
    public void testRunData() throws Exception {
//        System.out.println(11);
//        Thread.sleep(100000000000000L);
        AsyncEvent.post(new EmailModel("test","test"));
//        service.send("test","test",new String[]{"aop@lsh123.com"});
    }
}
