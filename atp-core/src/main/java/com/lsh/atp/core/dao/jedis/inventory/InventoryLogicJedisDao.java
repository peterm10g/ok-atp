package com.lsh.atp.core.dao.jedis.inventory;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisHashDao;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17/7/27.
 */
@Component
public class InventoryLogicJedisDao {

    private static final Logger logger = LoggerFactory.getLogger(InventoryLogicJedisDao.class);

    @Autowired
    private JedisHashDao jedisHashDao;

    /**
     * 查询指定商品库存数量
     * @param itemId
     * @param zoneCode
     * @param supply
     * @param dc
     * @return
     */
    public Long getInventoryQty(Long itemId, String zoneCode, Integer supply, String dc){
        String key = getRedisKey(itemId,zoneCode);
        String quantity = this.jedisHashDao.hget(key,new SupplyDc(supply,dc).toString());
        if(StringUtils.isBlank(quantity)){
            return null;
        }
        return Long.parseLong(quantity);
    }

    /**
     * 批量查询指定item列表的库存列表
     * @param items
     * @param zoneCode
     * @return itemId -> Map(supplyDcStr -> qty)
     */
    public Map<Long,Map<String,String>> getInventoryLogic(final Collection<Long> items, final String zoneCode){
        Map<Long, Map<String,String>> result = new HashMap<>((int)(items.size() / 0.75));
        Map<Long, Response<Map<String, String>>> responseHashMap = new HashMap<>((int)(items.size() / 0.75));
        Jedis jedis = null;
        Pipeline pipeline;
        try {
            jedis = jedisHashDao.getJedis();
            pipeline = jedis.pipelined();
            for (Long itemId : items) {
                Response<Map<String, String>> response = pipeline.hgetAll(getRedisKey(itemId,zoneCode));
                responseHashMap.put(itemId, response);
            }
            pipeline.close();
        } catch (Throwable e) {
            logger.error("", e);
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }

        for(Map.Entry<Long, Response<Map<String, String>>> entry : responseHashMap.entrySet()){
            Long itemId = entry.getKey();
            Map<String,String> map = null;
            Response<Map<String, String>> response = entry.getValue();
            if(response != null){
                map = response.get();
            }
            result.put(itemId,map);
        }

        return result;
    }

    /**
     * 数据添加到redis
     *
     * @param inventoryLogic
     * @return
     */
    public Boolean setInventoryLogicReids(InventoryLogic inventoryLogic) {
        String dc = inventoryLogic.getDC();
        Integer supply = inventoryLogic.getSupplyId();
        SupplyDc supplyDc = new SupplyDc(supply,dc);
        String redisKey = getRedisKey(inventoryLogic.getSkuId(),inventoryLogic.getZoneCode());

        long num = jedisHashDao.hset(redisKey, supplyDc.toString(), inventoryLogic.getInventoryQuantity().toString());
        boolean isSuccess = num == -1L ? false : true;
        return isSuccess;
    }

    public void setInventoryLogicReidsValue(Collection<InventoryLogic> inventoryLogicList) {
        for(InventoryLogic inventoryLogic : inventoryLogicList){
            this.setInventoryLogicReids(inventoryLogic);
        }
    }

    /**
     * 增加或减少库存
     *
     * @param zoneCode
     * @param supply
     * @param dc
     * @param itemId
     * @param num
     * @return
     */
    public Object hincrInventory(String zoneCode, String supply, String dc, Long itemId, Long num) {
        SupplyDc supplyDc = new SupplyDc(Integer.parseInt(supply),dc);
        String script = "if redis.call(\"HEXISTS\",KEYS[1],ARGV[1]) == 1 then return redis.call(\"HINCRBY\",KEYS[1],ARGV[1],ARGV[2]) else return nil end";
        return jedisHashDao.eval(script, 1, getRedisKey(itemId, zoneCode), supplyDc.toString(), num.toString());
    }

    private String getRedisKey(Long itemId, String zoneCode) {
        return this.getRedisKey(itemId.toString(),zoneCode);
    }

    private String getRedisKey(String itemId, String zoneCode) {
        return MessageFormat.format(RedisKeyConstant.INVENTORY_LOGIC, zoneCode, itemId);
    }
}
