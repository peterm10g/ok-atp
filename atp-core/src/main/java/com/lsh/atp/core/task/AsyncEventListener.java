package com.lsh.atp.core.task;


import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.lsh.atp.core.dao.es.InventoryRecordEsDao;
import com.lsh.atp.core.dao.jedis.area.WarehouseJedisDao;
import com.lsh.atp.core.dao.jedis.salerule.SaleRuleJedisDao;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.model.inventory.InventoryRecordType;
import com.lsh.atp.core.model.salerule.SaleRule;
import com.lsh.atp.core.service.email.SendEmailService;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.service.redis.currentdc.CurrentDcRedisService;
import com.lsh.atp.core.service.salerule.SaleWeightItemService;
import com.lsh.atp.core.task.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.lsh.atp.core.task.model.IncrInventoryLogicModel.*;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/12/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.task
 * desc: 异步任务监听类
 */
@Component
public class AsyncEventListener {

    private static final Logger logger = LoggerFactory.getLogger(AsyncEventListener.class);

    @Autowired
    private SendEmailService sendEmailService;


    @Autowired
    private InventoryLogicService inventoryLogicService;

    @Autowired
    private SaleWeightItemService saleWeightItemService;


    @Autowired
    private CurrentDcRedisService currentDcService;

    @Autowired
    private InventoryRecordEsDao inventoryRecordEsDao;

    @Autowired
    private WarehouseJedisDao warehouseJedisDao;

    @Autowired
    private SaleRuleJedisDao saleRuleJedisDao;

    /**
     * 设置逻辑库存redis监听
     * @param inventoryLogic
     */
    @Subscribe
    @AllowConcurrentEvents
    public void setInventoryLogic(InventoryLogic inventoryLogic) {
        inventoryLogicService.addInventoryLogicRedis(inventoryLogic);
    }

    /**
     * 发送邮件监听
     * @param emailModel
     */
    @Subscribe
    @AllowConcurrentEvents
    public void sendEmail(EmailModel emailModel){
        sendEmailService.send(emailModel.getContent(),emailModel.getTitle(),emailModel.getSendTo());
    }

    /**
     * 设置货主权重监听
     * @param supplyWeightModel
     */
    @Subscribe
    @AllowConcurrentEvents
    public void setSupplyWeight(SupplyWeightModel supplyWeightModel){
        saleWeightItemService.setSaleWeightItemRedis(supplyWeightModel.getItemId(),supplyWeightModel.getZoneCode(), supplyWeightModel.getSubZoneCode());
    }

    /**
     * 扣减/还原 库存监听
     * @param incrInventoryLogicModel
     */
    @Subscribe
    @AllowConcurrentEvents
    public void decreaseInventory(IncrInventoryLogicModel incrInventoryLogicModel){

        String zoneCode = incrInventoryLogicModel.getZoneCode();
        Operation operation = incrInventoryLogicModel.getOperatation();
        List<SkuHoldQty> skuHoldQtyList = incrInventoryLogicModel.getSkuHoldQtyList();

        logger.info("订单号 : " + incrInventoryLogicModel.getSequence() + "开始操作库存 , " + operation.toString());

        try{
            for(SkuHoldQty skuHoldQty : skuHoldQtyList){
                switch (operation){
                    case DCREASE:
                        inventoryLogicService.hincrInventoryRedis(zoneCode,skuHoldQty.getSupplyId().toString(),skuHoldQty.getDc(),skuHoldQty.getSkuId(),0 - skuHoldQty.getHoldQty());
                        this.indexInventoryRecord(new InventoryRecord(skuHoldQty.getSkuId(),skuHoldQty.getSupplyId(),skuHoldQty.getDc(),zoneCode,0 - skuHoldQty.getHoldQty(),null,null,incrInventoryLogicModel.getSequence(), InventoryRecordType.DECREASE.toString(),true));
                        break;
                    case RESTORE:
                        inventoryLogicService.hincrInventoryRedis(zoneCode,skuHoldQty.getSupplyId().toString(),skuHoldQty.getDc(),skuHoldQty.getSkuId(),skuHoldQty.getHoldQty());
                        this.indexInventoryRecord(new InventoryRecord(skuHoldQty.getSkuId(),skuHoldQty.getSupplyId(),skuHoldQty.getDc(),zoneCode,skuHoldQty.getHoldQty(),null,null,incrInventoryLogicModel.getSequence(), InventoryRecordType.RESTORE.toString(),true));
                        break;
                }
            }
        }catch (Throwable e){
            logger.error(incrInventoryLogicModel.getSequence(),e);

            StringBuilder content = new StringBuilder();
            content.append("订单号 : ").append(incrInventoryLogicModel.getSequence())
                    .append(" , 操作 : ").append(operation.toString())
                    .append("\n")
                    .append(e.toString());
            AsyncEvent.post(new EmailModel(content.toString(),"异步操作库存失败"));
        }


    }

    @Subscribe
    @AllowConcurrentEvents
    public void setCurrentDc(CurrentDcModel currentDc){
        logger.info("订单号 : " + currentDc.getSequence() + ",开始更新当前售卖仓库");
        String zoneCode = currentDc.getZoneCode();
        Map<Long,SupplyDc> map = currentDc.getMap();
        for(Map.Entry<Long,SupplyDc> entry : map.entrySet()){
            SupplyDc supplyDc = entry.getValue();
            try{
                this.currentDcService.updateCurrentDC(zoneCode,currentDc.getSubZoneCode(),entry.getKey(),supplyDc);
            }catch (Throwable e){
                logger.error(currentDc.getSequence(),e);
                AsyncEvent.post(new EmailModel(e.toString(),"异步更新当前售卖仓库失败"));
            }
        }
    }

    @Subscribe
    @AllowConcurrentEvents
    public void setRealDc(Warehouse warehouse){
        this.currentDcService.setDcId(warehouse.getSupplyId(),warehouse.getDcReal(),warehouse.getDcId());
    }

    @Subscribe
    @AllowConcurrentEvents
    public void indexInventoryRecord(InventoryRecord record){
        if(!record.getRecord()){  //如果不需要记录,直接返回
            return;
        }

        record.setId(UUID.randomUUID().toString());
        if(record.getCurrentQuantity() == null){
            Long quantity = this.inventoryLogicService.getInventoryQty(record.getSkuId(),record.getZoneCode(),record.getSupplyId(),record.getDc());
            record.setCurrentQuantity(quantity);
        }

        if(record.getUpdatedAt() == null){
            record.setUpdatedAt(System.currentTimeMillis());
        }

        this.inventoryRecordEsDao.save(record);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void setWarehouse(List<Warehouse> warehouses){
        warehouseJedisDao.setWarehouseZone(warehouses);
    }

    @Subscribe
    @AllowConcurrentEvents
    public void setSaleRule(SaleRule saleRule){
        saleRuleJedisDao.setSaleRule(saleRule);
    }

    public static void main(String[] args){
        System.out.println(System.currentTimeMillis());
    }
}
