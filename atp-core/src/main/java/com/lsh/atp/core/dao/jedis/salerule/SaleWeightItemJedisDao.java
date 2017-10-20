package com.lsh.atp.core.dao.jedis.salerule;

import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisSortedSetDao;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
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
 * Created by zhangqiang on 17/7/27.
 */
@Component
public class SaleWeightItemJedisDao {

    private final static Logger logger = LoggerFactory.getLogger(SaleWeightItemJedisDao.class);

    @Autowired
    private JedisSortedSetDao jedisSortedSetDao;

    public Double getScore(Long itemId, String zoneCode, String subZoneCode, String supplyDcStr){
        return this.jedisSortedSetDao.zscore(getRedisKey(itemId,zoneCode,subZoneCode),supplyDcStr);
    }

    /**
     * 获得商品 货主列表(根据权重从大到小)
     *
     * @param itemId
     * @param zoneCode
     * @param subZoneCode
     * @return 商品 有序货主列表(根据权重从大到小)
     */
    public Set<String> getSupplyWeight(Long itemId, String zoneCode, String subZoneCode) {
        return jedisSortedSetDao.zrevrangeByScore(getRedisKey(itemId, zoneCode, subZoneCode), Double.MAX_VALUE, 1);
    }

    /**
     * 获得商品 货主列表(根据权重从大到小) map
     *
     * @param items
     * @param zoneCode
     * @return
     */
    public Map<Long, Collection<String>> getSupplyWeight(List<Item> items, String zoneCode, String subZoneCode) {
        Long[] itemsArray = new Long[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemsArray[i] = items.get(i).getItemId();
        }
        return this.getSupplyWeight(itemsArray, zoneCode, subZoneCode);
    }

    /**
     * 获得商品 货主列表(根据权重从大到小) map
     *
     * @param items
     * @param zoneCode
     * @return
     */
    public Map<Long, Collection<String>> getSupplyWeight(Long[] items, String zoneCode, String subZoneCode) {
        final Map<Long, Collection<String>> result = new HashMap<>(items.length);

        //从redis获取item对应货主权重
        Jedis jedis = null;
        Pipeline pipeline = null;
        Map<Long, Response<Set<String>>> responseMap = new HashMap<>(items.length);
        try {
            jedis = this.jedisSortedSetDao.getJedis();
            pipeline = jedis.pipelined();
            for (Long itemId : items) {
                if(itemId == null){
                    throw new BusinessException(ExceptionStatus.E2001023.getCode(),ExceptionStatus.E2001023.getMessage());
                }
                String key = this.getRedisKey(itemId, zoneCode, subZoneCode);
                Response<Set<String>> response = pipeline.zrevrange(key,0,-1);
                responseMap.put(itemId, response);
            }
            pipeline.close();
        } catch (Throwable e) {
            logger.error("getSupplyWeight", e);
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }

        for(Long itemId : items){
            Response<Set<String>> response = responseMap.get(itemId);
            Collection<String> supplySet = response == null ? null : response.get();
            result.put(itemId, supplySet);
        }

        return result;
    }


//    /**
//     * 将数据库中的权重设置到redis中
//     *
//     * @param itemId
//     * @param zoneCode
//     * @return
//     */
//    public Long setFromDb(final Long itemId, final String zoneCode, String subZoneCode) {
//        //query db
//        List<SaleWeightItem> list = this.saleWeightItemDao.getSupplyWeightWithWeight(itemId, zoneCode, subZoneCode);
//
//        Map<String, Double> scoreMembers = new HashMap<>(list.size());
//        for (SaleWeightItem saleWeightItem : list) {
//            scoreMembers.put(new SupplyDc(saleWeightItem.getSupplyId(),saleWeightItem.getDc()).toString(), saleWeightItem.getWeight().doubleValue());
//        }
//
//        return this.jedisSortedSetDao.zadd(getRedisKey(itemId, zoneCode, subZoneCode), scoreMembers);
//    }

//    /**
//     * 判断是否有权重
//     */
//    public boolean isHasWeight(Long itemId, String zoneCode, String subZoneCode, String supplyDcStr){
//        Double score = this.jedisSortedSetDao.zscore(getRedisKey(itemId,zoneCode,subZoneCode),supplyDcStr);
//
//        if(score == null){   //查数据库
//            SupplyDc supplyDc = new SupplyDc(supplyDcStr);
//            score = this.saleWeightItemDao.getWeight(itemId,zoneCode,subZoneCode,supplyDc.getSupply(),supplyDc.getDc());
//            if(score != null && score.doubleValue() > 0){
//                //异步设置redis
//                AsyncEvent.post(new SupplyWeightModel(itemId, zoneCode,subZoneCode));
//            }
//        }
//
//        if(score == null || score.doubleValue() <= 0){
//            return false;
//        }
//        return true;
//    }

    /**
     * 更新Redis
     *
     * @param itemId
     * @param zoneCode
     * @param weight
     * @param supplyId
     * @return Long
     */
    public Boolean setItemWeight(Long itemId, String zoneCode, String subZoneCode, Double weight, Integer supplyId, String dc) {
        Map<String, Double> scoreMembers = new HashMap<>(2);
        scoreMembers.put(new SupplyDc(supplyId,dc).toString(), weight);
        Long num = this.jedisSortedSetDao.zadd(this.getRedisKey(itemId, zoneCode,subZoneCode), scoreMembers);
        return (num == 0) || (num == 1) ? true : false;

    }

    /**
     * 删除权重缓存
     * @param itemId
     * @param zoneCode
     * @param subZoneCode
     */
    public void delItemWeight(Long itemId, String zoneCode, String subZoneCode) {
        this.jedisSortedSetDao.deleteKey(this.getRedisKey(itemId, zoneCode,subZoneCode));
    }


    private String getRedisKey(Long itemId, String zoneCode, String subZoneCode) {
        return MessageFormat.format(RedisKeyConstant.ITEM_WEIGHT_ITEM, zoneCode,subZoneCode, itemId.toString());
    }

}
