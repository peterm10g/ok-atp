package com.lsh.atp.worker.task;

import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.worker.service.WmsStockService;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/11/15.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.task
 * desc: wms库存同步
 */
@Component
public class WmsStockSyncTask extends AbstractSyncTask{

    private static Logger logger = LoggerFactory.getLogger("wms");

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private WmsStockService wmsStockService;

    public WmsStockSyncTask(){
        setLogger(logger);
    }

    public List<InventoryLogic> getInventoryLoigcs(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        String dc = taskItemList.get(0).getTaskItemId();        //dc
        Warehouse warehouse = warehouseService.getWarehouseByDc(dc);
        if(warehouse == null){
            return Collections.EMPTY_LIST;
        }
        logger.info("WmsStockSyncTask---开始选择任务 : dc = " + dc);
        List<InventoryLogic> inventoryLogicList = null;

        try{
            inventoryLogicList = wmsStockService.getWmsStocks(warehouse);
        }catch (Throwable e){
            logger.error("获取wms任务失败",e);
            //邮件报警
            AsyncEvent.post(new EmailModel(e.toString(),"wms库存同步失败"));
        }

        logger.info("WmsStockSyncTask---完成选择任务 : dc = " + dc + ",共选择任务条数 : " + inventoryLogicList.size());

        return inventoryLogicList;
    }
}
