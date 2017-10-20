package com.lsh.atp.worker.service;

import com.lsh.atp.core.dao.salerule.SaleWeightItemDao;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.salerule.SaleWeightItem;
import com.lsh.atp.core.task.model.EmailModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yuan4j on 2017/1/13.
 */
@Component
public class WeightCheckService {
    @Autowired
    private SaleWeightItemDao saleWeightItemDao;


    /**
     * Mysql与Redis商品权重对比
     */
    public List<EmailModel> checkSaleWeightItem(String zoneCode) {
        return null; // TODO: 2017/7/22
//        List<EmailModel> emails = new ArrayList<EmailModel>();
//        Long itemId;
//        List<SaleWeightItem> saleWeightItems = saleWeightItemDao.selectDistItem(zoneCode);
//
//        for (SaleWeightItem weightItem : saleWeightItems) {
//            itemId = weightItem.getItemId();
//
//            StringBuilder redisSupplyDc = new StringBuilder();
//            Set<String> supplyDcs = itemWeightRedisService.getSupplyWeight(itemId, zoneCode);
//            if (CollectionUtils.isEmpty(supplyDcs)) {
//                continue;
//            }
//            for (String supplyDc : supplyDcs) {
//                redisSupplyDc.append(supplyDc).append(" ");
//            }
//
//            StringBuilder sqlSupplyDc = new StringBuilder();
//            List<SupplyDc> supplyDcList = saleWeightItemDao.getSupplyWeight(itemId, zoneCode);
//            for (SupplyDc supplyDc : supplyDcList) {
//                sqlSupplyDc.append(supplyDc.toString()).append(" ");
//            }
//
//
//            if (!sqlSupplyDc.toString().equals(redisSupplyDc.toString())) {
//                emails.add(new EmailModel(itemId + ":" + zoneCode + "\n" + "Redis权重顺序为:" + redisSupplyDc + "\n" +
//                        "Mysql权重顺序为:" + sqlSupplyDc, "Mysql与Redis商品权重不一致!"));
//            }
//
//        }
//        return emails;
    }
}
