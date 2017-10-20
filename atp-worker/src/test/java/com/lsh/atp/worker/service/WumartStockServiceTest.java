package com.lsh.atp.worker.service;

import com.lsh.atp.core.dao.hold.SkuHoldDao;
import com.lsh.atp.core.dao.hold.SkuHoldQtyDao;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuan4j on 2017/1/3.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class WumartStockServiceTest {
    @Autowired
    private WumartStockService wumartStockService;

    @Autowired
    private SkuHoldDao skuHoldDao;

    @Autowired
    private SkuHoldQtyDao skuHoldQtyDao;

    @Test
    public void getAllNotDeducted() throws Exception {
        Set<String> orderIdSet = new HashSet<String>();
        orderIdSet.add("2679408575475025831");
        orderIdSet.add("3156227185990650127");

        List<Long> holdIdList = this.skuHoldDao.getSkuHoldIdBySequenceId(orderIdSet);

        //将返回的list转为map
        Map<Long, Long> itemNotDeductedMap = new HashMap<Long, Long>();
        if (CollectionUtils.isEmpty(holdIdList)) {
            itemNotDeductedMap.put(0L,0L);
        }

        List<SkuHoldQty> itemNotDeductedList = this.skuHoldQtyDao.getTotalHoldQtyBySkuIdAndDc(holdIdList, "DC10");
        for (SkuHoldQty skuHoldQty : itemNotDeductedList) {
            itemNotDeductedMap.put(skuHoldQty.getSkuId(), skuHoldQty.getHoldQty());
        }

        System.out.println(itemNotDeductedMap.size());
    }
}
