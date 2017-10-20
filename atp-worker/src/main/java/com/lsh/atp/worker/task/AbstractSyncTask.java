package com.lsh.atp.worker.task;

import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.model.inventory.InventoryRecordType;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.service.inventory.InventorySyncService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.InventoryRecord;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/11/15.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.task
 * desc: 库存同步抽象类
 */
public abstract class AbstractSyncTask implements IScheduleTaskDealSingle<InventoryLogic> {

    private Logger logger;

    @Autowired
    private InventorySyncService inventorySyncService;

    @Autowired
    private InventoryLogicService inventoryLogicService;
    /**
     *
     * @param inventoryLogic 任务项
     * @param ownSign 参数
     * @return boolean
     * @throws Exception
     */
    public boolean execute(InventoryLogic inventoryLogic, String ownSign) throws Exception {
        if(inventoryLogic == null){
            return true;
        }
        //查询之前的数量
        Long preQuantity = this.inventoryLogicService.getInventoryQty(inventoryLogic.getSkuId(),inventoryLogic.getZoneCode(),inventoryLogic.getSupplyId(),inventoryLogic.getDc());

        logger.info("开始执行任务 : item_id = " + inventoryLogic.getSkuId() + ",数量" + inventoryLogic.getInventoryQuantity() + ",dc:" + inventoryLogic.getDC());
        StringBuilder logStr = new StringBuilder();
        logStr.append("itemId = ").append(inventoryLogic.getSkuId())
                .append(",supply = ").append(inventoryLogic.getSupplyId())
                .append(",dc =").append(inventoryLogic.getDC())
                .append(",数量为").append(inventoryLogic.getInventoryQuantity());
        Boolean flag = this.inventorySyncService.updateInventoryLogic(inventoryLogic);
        logger.info(logStr.toString() + ",更新逻辑库存," + flag.toString());
        logger.info("结束执行任务 : item_id = " + inventoryLogic.getSkuId() + ",数量" + inventoryLogic.getInventoryQuantity() + ",dc:" + inventoryLogic.getDC());

        AsyncEvent.post(new InventoryRecord(inventoryLogic.getSkuId(),inventoryLogic.getSupplyId(),inventoryLogic.getDC(),inventoryLogic.getZoneCode(),null,inventoryLogic.getInventoryQuantity(),null,null, InventoryRecordType.SYNC.toString(),!preQuantity.equals(inventoryLogic.getInventoryQuantity())));

        return true;
    }

    /**
     * 模板方法  选取任务
     * @param taskParameter
     * @param ownSign
     * @param taskItemNum
     * @param taskItemList
     * @param eachFetchDataNum
     * @return
     * @throws Exception
     */
    public List<InventoryLogic> selectTasks (String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        final List<InventoryLogic> inventoryLogicList = this.getInventoryLoigcs(taskParameter, ownSign, taskItemNum, taskItemList, eachFetchDataNum);
        //同步redis
        this.syncInventoryLogicRedis(inventoryLogicList);

        return inventoryLogicList;
    }

    /**
     * 库存同步到redis中
     * @param inventoryLogicList
     */
    protected void syncInventoryLogicRedis(final List<InventoryLogic> inventoryLogicList){
        logger.info("begin set redis");
        inventoryLogicService.addInventoryLogicRedis(inventoryLogicList);
        logger.info("end set redis");
    }


    protected abstract List<InventoryLogic> getInventoryLoigcs (String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception;


    public Comparator<InventoryLogic> getComparator() {
        return null;
    }


    public final void setLogger(Logger logger){
        this.logger = logger;
    }
}
