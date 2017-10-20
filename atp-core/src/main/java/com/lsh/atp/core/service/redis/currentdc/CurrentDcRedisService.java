package com.lsh.atp.core.service.redis.currentdc;

import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.area.WarehouseDao;
import com.lsh.atp.core.dao.jedis.JedisHashDao;
import com.lsh.atp.core.dao.jedis.JedisStringDao;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.text.MessageFormat;
import java.util.*;

/**
 * Created by zhangqiang on 16/12/20.
 */
@Component
public class  CurrentDcRedisService{

    Logger logger = LoggerFactory.getLogger(CurrentDcRedisService.class);

    @Autowired
    private JedisStringDao jedisStringDao;

    @Autowired
    private JedisHashDao jedisHashDao;

    @Autowired
    private WarehouseDao warehouseDao;


    /** 更新当前售卖仓库 **/
    public void updateCurrentDC(String zoneCode,String subZoneCode, Long itemId, SupplyDc supplyDc){
        if(subZoneCode == null){
            return;
        }
        //根据supplyDc 获取 realDc
        String dcId = getDcId(supplyDc);
        logger.info("开始更新当前售卖仓库, zoneCode = " + zoneCode + ",itemId = " + itemId + ",dc = " + dcId);

        String redisKey = getRedisKey(zoneCode,itemId);

        this.jedisHashDao.hset(redisKey,subZoneCode,dcId);
    }

    /** 更新当前售卖仓库 **/
    public void updateCurrentDC(String zoneCode,String subZoneCode, Long itemId, String dcId){
        if(subZoneCode == null){
            return;
        }
        //根据supplyDc 获取 realDc
        logger.info("开始更新当前售卖仓库, zoneCode = " + zoneCode + ",itemId = " + itemId + ",dc = " + dcId);

        String redisKey = getRedisKey(zoneCode,itemId);

        this.jedisHashDao.hset(redisKey,subZoneCode,dcId);
    }

    public String getCurrentDc(Long itemId, String zoneCode, String subZoneCode){
        String key = this.getRedisKey(zoneCode,itemId);
        return this.jedisHashDao.hget(key,subZoneCode);
    }

    public Map<Long,Collection<String>> getCurrentDC(String zoneCode, List<Long> items){
        final Map<Long,Collection<String>> result = new HashMap<>(items.size());
        Jedis jedis = null;
        Pipeline pipeline = null;
        Map<Long,Response<Map<String, String>>> map = new HashMap<>(items.size());
        try {
            jedis = jedisHashDao.getJedis();
            pipeline = jedis.pipelined();
            for (Long itemId : items) {
                String redisKey = getRedisKey(zoneCode,itemId);
                Response<Map<String, String>> response = pipeline.hgetAll(redisKey);
                map.put(itemId, response);
            }
            pipeline.sync();
        } catch (Throwable e) {
            logger.error("", e);
        } finally {
            try {
                pipeline.close();
                jedis.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        for(Long itemId : map.keySet()){
            Collection<String> currentDcs = new HashSet<>();
            Response<Map<String, String>> response = map.get(itemId);
            if(response != null){
                Map<String, String> subzoneDcMap = response.get();
                if(subzoneDcMap == null){
                    continue;
                }
                currentDcs.addAll(subzoneDcMap.values());
            }

            result.put(itemId,currentDcs);
        }

        return result;
    }

    /***
     * 获取给商城返回的dcId
     * @param supplyDc
     * @return
     */
    public String getDcId(SupplyDc supplyDc){
        Integer supply = supplyDc.getSupply();
        String realDc = supplyDc.getDc();
        String dcId = this.jedisStringDao.get(MessageFormat.format(RedisKeyConstant.REAL_DC,supply.toString(),realDc));
        if(dcId == null){ //redis中没有,查数据库
            dcId = this.warehouseDao.getDcId(realDc,supply);

            if(dcId == null){
                //db中没有,邮件报警
                AsyncEvent.post(new EmailModel("数据库和redis中都没有数据,supply = " + supply + ",dcReal = " + realDc,"仓库数据预警"));
                throw new BusinessException(ExceptionStatus.E2001027.getCode(),ExceptionStatus.E2001027.getMessage());
            }
            //异步存入redis
            AsyncEvent.post(new Warehouse(null,supply,null,dcId,realDc,null,null,null,null,null,null));
        }
        return dcId;
    }

    public void setDcId(Integer supply, String realDc, String dcId){
        this.jedisStringDao.set(MessageFormat.format(RedisKeyConstant.REAL_DC,supply.toString(),realDc),dcId);
    }

    private String getRedisKey(String zoneCode, Long itemId){
        return  MessageFormat.format(RedisKeyConstant.CURRENT_SALE_DC,zoneCode,String.valueOf(itemId));
    }

}
