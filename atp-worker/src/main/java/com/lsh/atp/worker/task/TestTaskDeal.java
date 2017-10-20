package com.lsh.atp.worker.task;

import com.taobao.pamirs.schedule.IScheduleTaskDealSingle;
import com.taobao.pamirs.schedule.TaskItemDefine;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by huangdong on 16/6/23.
 */
@Component
public class TestTaskDeal implements IScheduleTaskDealSingle<String> {

    private final static Logger logger = Logger.getLogger(TestTaskDeal.class);

    private AtomicInteger i = new AtomicInteger(0);

    public boolean execute(String task, String ownSign) throws Exception {
        logger.info("[" + new Timestamp(System.currentTimeMillis()) + "] [execute][S] " + Thread.currentThread().getName() + "..." + task);
        Thread.sleep(TimeUnit.SECONDS.toMillis(8));
        logger.info("[" + new Timestamp(System.currentTimeMillis()) + "] [execute][E] " + Thread.currentThread().getName() + "..." + task);
        return false;
    }

    public List<String> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
        logger.info(taskItemList.size());
        String id = taskItemList.get(0).getTaskItemId() + "-" + i.getAndIncrement();
        logger.info("[" + new Timestamp(System.currentTimeMillis()) + "] [select] " + Thread.currentThread().getName() + "..." + id);
        int num = eachFetchDataNum / taskItemList.size();
        List<String> list = new ArrayList<String>();
        list.add("[" + id + "]111");
        list.add("[" + id + "]222");
        list.add("[" + id + "]333");
        list.add("[" + id + "]444");
        list.add("[" + id + "]555");
        list.add("[" + id + "]666");
        return list;
    }

    public Comparator<String> getComparator() {
        logger.info(Thread.currentThread().getName());
        return new Comparator<String>() {
            public int compare(String o1, String o2) {
                return 0;
            }
        };
    }
}
