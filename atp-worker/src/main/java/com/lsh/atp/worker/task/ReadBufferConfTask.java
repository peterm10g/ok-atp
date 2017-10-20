package com.lsh.atp.worker.task;

import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.model.buffer.SkuBufferConf;
import com.lsh.atp.core.model.buffer.SkuBufferConfs;
import com.lsh.atp.core.service.RedisService.RedisBufferConfService;
import com.lsh.atp.worker.service.ReadBufferConfService;
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
 * Created by zhangqiang on 16/9/21.
 */
@Component
public class ReadBufferConfTask implements IScheduleTaskDealSingle<SkuBufferConfs> {

    Logger logger = LoggerFactory.getLogger(ReadBufferConfTask.class);

    @Autowired
    private RedisBufferConfService redisBufferConfService;

    @Autowired
    private ReadBufferConfService readBufferConfService;


    public boolean execute(SkuBufferConfs skuBufferConfs, String s) throws Exception {

        if(skuBufferConfs == null || skuBufferConfs.getBuffers() == null || skuBufferConfs.getBuffers().size() == 0){
            return false;
        }

        //检验配置
        for(SkuBufferConf skuBufferConf : skuBufferConfs.getBuffers()){
            if (!this.readBufferConfService.validateSkuBufferConf(skuBufferConf)) {
                return false;
            }
        }

        //存入缓存
//        this.redisBufferConfService.set(skuBufferConfs, RedisKeyConstant.SKU_BUFFER_CONF);
        // TODO: 17/5/15 jedis
        return true;
    }

    public List<SkuBufferConfs> selectTasks(String s, String s1, int i, List<TaskItemDefine> list, int i1) throws Exception {
        List<SkuBufferConfs> buffers = new ArrayList<SkuBufferConfs>(1);
        logger.info("------------- 开始读取 Buffer 配置文件---------------");
        SkuBufferConfs skuBufferConfs = this.readBufferConfService.getSkuBufferConfList();
        buffers.add(skuBufferConfs);
        logger.info("------------- 结束读取 Buffer 配置文件---------------");
        return buffers;

    }

    public Comparator<SkuBufferConfs> getComparator() {
        return null;
    }
}
