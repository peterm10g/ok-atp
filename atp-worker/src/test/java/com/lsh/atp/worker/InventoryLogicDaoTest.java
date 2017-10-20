package com.lsh.atp.worker;

import com.lsh.atp.core.dao.inventory.InventoryLogicDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zhangqiang on 16/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class InventoryLogicDaoTest {

    @Autowired
    private InventoryLogicDao inventoryLogicDao;

    @Test
    public void test(){
        System.out.println(inventoryLogicDao.getQty(10002L,"1000",2,"DC31"));
    }
}
