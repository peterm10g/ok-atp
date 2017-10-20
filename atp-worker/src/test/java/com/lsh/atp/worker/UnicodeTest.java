package com.lsh.atp.worker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhangqiang on 17/1/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class UnicodeTest {

    @Value("${mail.tilte.prefix}")
    private String value ;

    @Test
    public void test(){
        System.out.println(value);
    }
}
