package com.lsh.atp.worker.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 16/11/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class HigoStockServiceTest {

    @Autowired
    private HigoStockService service;

    @Test
    public void queryStocksInfo(){
        List<String> goodsnoList = new ArrayList<String>(10);
        goodsnoList.add("40041234");
        //goodsnoList.add("40041201");
        String warehouseNo = "A001";
        //Map<String, BigDecimal> result =  service.queryStocksInfo(goodsnoList, warehouseNo);
        //System.out.println(result.toString());
    }
}
