package com.lsh.atp.core.service.redis.kafka;

import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisHashDao;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.area.Warehouse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by zhangqiang on 17/4/9.
 */
@Component
public class KafkaRedisService {

    @Autowired
    private JedisHashDao jedisHashDao;

    /**
     * 检验最后修改时间
     * @param warehouse
     * @param itemId
     * @param curUpdated
     * @return 如果当前的修改时间大于等于上一次的修改时间,则返回true,否则返回false
     */
    public boolean checkUpdated(Warehouse warehouse, Long itemId, String curUpdated) {
        String redisKey = getUpdatedRedisKey(warehouse.getZoneCode(),itemId);
        String lastUpdated = jedisHashDao.hget(redisKey, new SupplyDc(warehouse.getSupplyId(),warehouse.getDcId()).toString());
        if(StringUtils.isBlank(lastUpdated)){
            return true;
        }
        return Long.parseLong(lastUpdated) <= Long.parseLong(curUpdated) ? true : false;
    }

    public void setUpdated(Warehouse warehouse, Long itemId, String updated){
        String redisKey = getUpdatedRedisKey(warehouse.getZoneCode(),itemId);
        this.jedisHashDao.hset(redisKey,new SupplyDc(warehouse.getSupplyId(),warehouse.getDcId()).toString(),updated);
    }

    private String getUpdatedRedisKey(final String zoneCode, final String itemId) {
        return MessageFormat.format(RedisKeyConstant.KAFKA_UPDATED, zoneCode, itemId);
    }

    private String getUpdatedRedisKey(final String zoneCode, final Long itemId) {
        return this.getUpdatedRedisKey(zoneCode,itemId.toString());
    }


}
