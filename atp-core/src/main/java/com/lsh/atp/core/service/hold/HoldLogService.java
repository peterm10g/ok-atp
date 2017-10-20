package com.lsh.atp.core.service.hold;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.core.dao.hold.HoldLogDao;
import com.lsh.atp.core.model.hold.HoldLog;
import com.lsh.base.common.json.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.hold.
 * desc:预占操作日志
 */
@Component
@Transactional(readOnly = true)
public class HoldLogService implements ILogStrategy {

    private static Logger logger = LoggerFactory.getLogger(HoldLogService.class);

    @Autowired
    private HoldLogDao holdLogDao;

    /**
     * 插入预占日志
     *
     * @param channel 渠道
     * @param sequence 序列号
     * @param operationType 操作类型
     * @param holdId 预占id
     * @param items 商品列表
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertLog(int channel, String sequence, int operationType, Long holdId, List<Item> items) {
        try {

            long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());

            HoldLog holdLog = new HoldLog();
            holdLog.setChannel(channel);
            holdLog.setCreatedAt(currentTime);
            holdLog.setUpdatedAt(currentTime);
            holdLog.setOperationType(operationType);
            holdLog.setSequenceId(sequence);
            if (items == null) {
                holdLog.setContent("{}");
            } else {
                String itemStr = JsonUtils.obj2Json(items);
                if(itemStr.length() <= 1480){
                    holdLog.setContent(itemStr);
                }else{
                    holdLog.setContent(itemStr.substring(0,1450));
                    logger.info("items json is " + itemStr);
                }
            }
            holdLog.setHoldId(holdId);

            holdLogDao.insert(holdLog);

        } catch (Exception e) {
            logger.error("记录日志异常",e);
        }

    }


}
