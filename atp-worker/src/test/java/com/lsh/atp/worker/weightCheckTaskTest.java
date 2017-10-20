package com.lsh.atp.worker;

import com.lsh.atp.worker.service.WeightCheckService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by yuan4j on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class weightCheckTaskTest {

    @Autowired
    private WeightCheckService weightCheckService;

    @Test
    public void test() throws Exception {
        weightCheckService.checkSaleWeightItem("1000");
        System.out.println("AS.........SA");
    }

}
