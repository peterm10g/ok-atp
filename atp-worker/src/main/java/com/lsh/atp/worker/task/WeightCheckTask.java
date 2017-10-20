package com.lsh.atp.worker.task;

import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.worker.service.WeightCheckService;
import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by yuan4j on 2017/1/13.
 */
@Component
public class WeightCheckTask implements IScheduleTaskDealSingle<EmailModel> {

    Logger logger = LoggerFactory.getLogger(WeightCheckTask.class);

    @Autowired
    private WeightCheckService weightCheckService;

    public boolean execute(EmailModel emailModel, String s) throws Exception {
        AsyncEvent.post(emailModel);
        return true;
    }

    public List selectTasks(String s, String s1, int i, List<TaskItemDefine> taskItemList, int i1) throws Exception {

        String zoneCode = taskItemList.get(0).getTaskItemId();
        List<EmailModel> emails = new ArrayList<EmailModel>(taskItemList.size());
        try {
            logger.info("WeightCheckTask---开始核对任务 ,zoneCode = " + zoneCode);
            emails.addAll(weightCheckService.checkSaleWeightItem(zoneCode));
            logger.info("WeightCheckTask---结束核对任务, zoneCode = " + zoneCode + " 权重不一致条数 : " + emails.size());
        } catch (Throwable e) {
            logger.error("商品权重对比失败", e);
            //邮件报警
            AsyncEvent.post(new EmailModel(e.toString(), "商品权重对比失败"));
        }
        return emails;
    }

    public Comparator getComparator() {
        return null;
    }
}
