package com.lsh.atp.worker.task;

import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.worker.service.HigoStockService;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/11/8.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.task
 * desc: 黑狗库存同步
 */
@Component
public class HigoStockSyncTask extends AbstractSyncTask {

    private static final Logger logger = LoggerFactory.getLogger("higo");

    public HigoStockSyncTask(){
        this.setLogger(logger);
    }

    @Autowired
    private HigoStockService higoStockService;

    public List<InventoryLogic> getInventoryLoigcs(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        String dc = taskItemList.get(0).getTaskItemId();    //黑狗仓库名
        logger.info("WumartStockSyncTask---开始选择任务 : dc = " + dc);
        List<InventoryLogic> result = null;
        try{
            result = higoStockService.getHigoStock(dc);
        }catch (Throwable e){
            logger.error("获取黑狗任务失败",e);
            //邮件报警
            AsyncEvent.post(new EmailModel(e.toString(),"黑狗库存同步失败"));
        }
        logger.info("WumartStockSyncTask---完成选择任务 : dc = " + dc + ",共选择任务条数 : " + (result == null ? "0" : result.size()));
        return result;
    }
}
