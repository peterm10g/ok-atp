package com.lsh.atp.worker;

import com.lsh.atp.core.dao.area.WarehouseDao;
import com.lsh.atp.core.dao.inventory.InventoryLogicDao;
import com.lsh.atp.core.dao.salerule.SaleWeightItemDao;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.area.WarehouseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Set;

/**
 * Created by zhangqiang on 16/12/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class SaleWeightItemDaoTest {
    @Autowired
    private SaleWeightItemDao saleWeightItemDao;

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private WarehouseService warehouseService;


    @Test
    public void getSupplyWeightTest(){
//        System.out.println(saleWeightItemDao.selectByPrimaryKey(1L));;
       // System.out.println(warehouseService.getSupplyIdByDc("DC10"));
//        System.out.println(saleWeightItemDao.getSupplyWeight(10002L,"1000"));
//        System.out.println(inventoryLogicDao.getInventoryLogicById(1000L));
    }
}
