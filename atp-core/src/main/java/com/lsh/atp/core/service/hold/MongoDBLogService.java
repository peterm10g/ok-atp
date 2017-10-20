package com.lsh.atp.core.service.hold;

import com.lsh.atp.api.model.baseVo.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.hold.
 * desc:预占接口业务层
 */
@Component
public class MongoDBLogService implements ILogStrategy {

    private static Logger logger = LoggerFactory.getLogger(MongoDBLogService.class);

    /**
     * 日志记录
     * @param channel 渠道
     * @param sequence 序列号
     * @param operationType 操作类型
     * @param holdId 预占id
     * @param items 商品列表
     */
    public void insertLog(int channel, String sequence, int operationType, Long holdId, List<Item> items) {

        logger.error("channel :"+channel+"sequence : "+ sequence + "operationType : " + operationType + "holdId : " + holdId);
    }
}
