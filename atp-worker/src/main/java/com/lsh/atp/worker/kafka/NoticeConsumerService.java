package com.lsh.atp.worker.kafka;

import com.dmall.dmg.sdk.core.consumer.ConsumerContext;
import com.dmall.dmg.sdk.core.consumer.ConsumerRecord;
import com.dmall.dmg.sdk.core.consumer.MessageHandler;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.model.inventory.InventoryRecordType;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.service.inventory.InventorySyncService;
import com.lsh.atp.core.service.redis.kafka.KafkaRedisService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.task.model.InventoryRecord;
import com.lsh.atp.worker.model.KafkaStock;
import com.lsh.atp.worker.service.KafkaDeliveryService;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.json.JsonUtils2;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.*;

@Component
public class NoticeConsumerService implements MessageHandler {

    private static Logger kafkaLogger = LoggerFactory.getLogger("kafka");

    @Autowired
    private KafkaDeliveryService kafkaDeliveryService;

    @Autowired
    private InventorySyncService inventorySyncService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private InventoryLogicService inventoryLogicService;

    @Autowired
    private KafkaRedisService kafkaRedisService;

    @Resource(name = "zoneCodeMap")
    private Map<String, String> zoneAreaMap;

    public void onMessage(List<ConsumerRecord> records, ConsumerContext consumerContext) {
        for (ConsumerRecord consumerRecord : records) {
            this.syncKafkaDelivery(consumerRecord);
        }
    }


    private void syncKafkaDelivery(ConsumerRecord consumerRecord) {
        //messageId
        final String messageId = consumerRecord.getMessageId();
        kafkaLogger.info("messageId : {} 开始同步库存 , 参数为 {}",messageId,JsonUtils2.obj2Json(consumerRecord));
        try {
            //根据messageId去重
            if (this.kafkaDeliveryService.isHandled(messageId)) {
                kafkaLogger.info("messageId : {} , 该条数据已经被处理过",messageId);
                return;
            }

            String json = new String(consumerRecord.getMessageByte(), "utf-8"); //具体库存数据

            final KafkaStock kafkaStock = JsonUtils.json2Obj(json, KafkaStock.class);

            String wumartCode = kafkaStock.getMaterialNo();     //物美码

            //根据DC查找Warehouse对象
            String dc = kafkaStock.getPlant();
            Warehouse warehouse = this.warehouseService.getWarehouseByDc(dc);
            if (warehouse == null || "DC42".equals(dc) || "DC43".equals(dc)) {  //// TODO: 17/8/31 先写死  
                kafkaLogger.info("messageId : {} 该仓库库存不需要同步, dc = {}", messageId, dc);
                return;
            }

            //排除商品
            if (this.kafkaDeliveryService.isExcluded(wumartCode, warehouse)) {
                kafkaLogger.info("messageId : {} 该商品被排除,不同步, dc = {} ,物美码为" + wumartCode, messageId, dc);
                return;
            }

            //物美码转商品码
            Long itemId = this.goodsService.code2ItemId(wumartCode, this.inventorySyncService.getMarketByZoneCode(warehouse.getZoneCode()));
            if (itemId == null) {
                kafkaLogger.info("messageId : {} , 该物美码 : {} 没有对应的链商码",messageId, wumartCode);
                return;
            }

            try{
                //检验最后修改时间
                if(! kafkaRedisService.checkUpdated(warehouse,itemId,kafkaStock.getGmtModified())){
                    kafkaLogger.info("messageId : {} ,时间校验未通过",messageId);
                    return;
                }
            }catch (Throwable t){
                kafkaLogger.error("校验时间失败",t);
            }


            Long quantity = kafkaStock.getAvailableNum().longValue();       //数量先取整
            kafkaLogger.info("messageId : {} itemId : {} 同步的库存量为 : " + quantity,messageId,itemId);

            //减去预留数量
            if (quantity > 0) {
                int saleUnit = this.goodsService.getSaleUnit(itemId);
                int reservedQuality = this.inventorySyncService.getQualityOfReserved(saleUnit, dc);
                kafkaLogger.info("messageId : {} itemId : {} , dc : {} 预留的数量为 {}",new String[]{messageId,itemId.toString(),dc,String.valueOf(reservedQuality)});
                quantity -= reservedQuality;
            }
            //减去订单未扣减数量
            if (quantity > 0) {
                Long deadline = Long.parseLong(kafkaStock.getGmtModified()) / 1000;   //ms转成s
                String sequenceId = kafkaStock.getBusinessId();
                if( ! StringUtils.isBlank(sequenceId)){     //去掉最后一位
                    sequenceId = sequenceId.substring(0,sequenceId.length() - 1);
                }
                int notDeductedQuality = this.inventorySyncService.getQualityOfNotDeducted(itemId, deadline, warehouse.getZoneCode(), dc, sequenceId);
                kafkaLogger.info("messageId : {} itemId : {} , dc : {} 未扣减的数量为 {}",new String[]{messageId,itemId.toString(),dc,String.valueOf(notDeductedQuality)});
                quantity -= notDeductedQuality;
            }

            if (quantity < 0) {
                quantity = 0L;
            }

            kafkaLogger.info("messageId : {}, itemId : {} , dc : {}  同步逻辑库存的数量为 : {}", new String[]{messageId,itemId.toString(),dc,String.valueOf(quantity)});
            //新建库存对象
            final Long areaId = Long.parseLong(this.zoneAreaMap.get(warehouse.getZoneCode()));
            InventoryLogic inventoryLogic = new InventoryLogic(null, areaId, itemId, null, quantity.longValue(), null, null, null, null, null, null, warehouse.getSupplyId(), warehouse.getDcReal(), warehouse.getZoneCode());

            //查询之前的数量
            Long preQuantity = this.inventoryLogicService.getInventoryQty(inventoryLogic.getSkuId(),inventoryLogic.getZoneCode(),inventoryLogic.getSupplyId(),inventoryLogic.getDc());

            //mysql
            Boolean ifDbSuccess = this.inventorySyncService.updateInventoryLogic(inventoryLogic);
            kafkaLogger.info("messageId : " + messageId + " db状态" + ifDbSuccess);
            //redis
            Boolean ifRedisSuccess = this.inventoryLogicService.addInventoryLogicRedis(inventoryLogic);
            kafkaLogger.info("messageId : " + messageId + " redis状态" + ifRedisSuccess);

            //redis中存入messageId
            this.kafkaDeliveryService.putHandledToRedis(messageId,json);
            kafkaLogger.info("messageId : {} 结束同步库存",messageId);

            //redis中存入update
            this.kafkaRedisService.setUpdated(warehouse,itemId,kafkaStock.getGmtModified());

            AsyncEvent.post(new InventoryRecord(inventoryLogic.getSkuId(),inventoryLogic.getSupplyId(),inventoryLogic.getDC(),inventoryLogic.getZoneCode(),null,inventoryLogic.getInventoryQuantity(),null,null, InventoryRecordType.SYNC.toString(),!preQuantity.equals(inventoryLogic.getInventoryQuantity())));


        } catch (Throwable e) {
            kafkaLogger.error("messageId : " + messageId + " 处理失败", e);
            AsyncEvent.post(new EmailModel( JsonUtils2.obj2Json(consumerRecord) + "\n" + e.getMessage(),"kafka同步失败"));
        }
    }
}
