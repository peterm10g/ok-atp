package com.lsh.atp.core.service.hold;

import com.lsh.atp.api.model.baseVo.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.hold.
 * desc:
 */
@Component
public class StrategyContextService {

    private static Logger logger = LoggerFactory.getLogger(StrategyContextService.class);


    @Autowired
    private HoldLogService holdLogService;

    @Autowired
    private MongoDBLogService mongoDBLogService;


    /**
     * 日志生成
     * @param channel 渠道
     * @param sequence 序列号
     * @param operationType 操作类型
     * @param holdId 预占id
     * @param items 商品列表
     */
    void insertLog(int channel, String sequence, int operationType, Long holdId, List<Item> items) {

        ILogStrategy iLogStrategy;
        try {
            iLogStrategy = holdLogService;

            iLogStrategy.insertLog(channel, sequence, operationType, holdId, items);
        } catch (Exception ex) {
            logger.error("MYSQL记录日志异常",ex);
            try {
                iLogStrategy = mongoDBLogService;
                iLogStrategy.insertLog(channel, sequence, operationType, holdId, items);
            } catch (Exception e) {
                logger.error("mongoDB记录日志异常",ex);
            }

        }

    }


}
