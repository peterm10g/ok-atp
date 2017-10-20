package com.lsh.atp.worker.task;


import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.worker.service.WumartStockService;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/7/18.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.task
 * desc: 库存同步
 */
@Component
public class WumartStockSyncTask extends AbstractSyncTask {


    private static Logger syncLogger = LoggerFactory.getLogger("sync");

    public WumartStockSyncTask(){
        this.setLogger(syncLogger);
    }

    @Autowired
    private WumartStockService wumartStockService;

    /**
     *
     * @param taskParameter taskParameter
     * @param ownSign ownSign
     * @param taskItemNum taskItemNum
     * @param taskItemList taskItemList
     * @param eachFetchDataNum eachFetchDataNum
     * @return list
     * @throws Exception
     */
    public List<InventoryLogic> getInventoryLoigcs(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        String dc = taskItemList.get(0).getTaskItemId();

        //根据dcid 获取 warehouse
        final Warehouse warehouse = this.wumartStockService.getWarehosue(dc);

        syncLogger.info("WumartStockSyncTask---开始选择任务 : dc = " + dc);

        List<InventoryLogic> inventoryLogicList = null;

        try{
            inventoryLogicList = this.wumartStockService.getWumartStock(warehouse);
        }catch (Throwable e){
            syncLogger.error("获取黑狗任务失败",e);
            //邮件报警
            AsyncEvent.post(new EmailModel(e.toString(),"物美库存同步失败"));
        }

        if(CollectionUtils.isEmpty(inventoryLogicList)){
            syncLogger.info("WumartStockSyncTask---完成选择任务: dc = " + dc + "共选择0条任务");
            return Collections.emptyList();
        }

        syncLogger.info("WumartStockSyncTask---完成选择任务: dc = " + dc + "共选择" + inventoryLogicList.size() + "条任务");

        return inventoryLogicList;
    }

}
