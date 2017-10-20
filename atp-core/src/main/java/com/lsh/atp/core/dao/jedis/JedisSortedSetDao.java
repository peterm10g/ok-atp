package com.lsh.atp.core.dao.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by zhangqiang on 16/12/8.
 */
@Repository
public class JedisSortedSetDao extends JedisBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(JedisSortedSetDao.class);

    public Double zscore(String key, String member){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zscore(key,member);
        } catch (Throwable e) {
            logger.error("zscore", e);
            throw e;
        } finally {
            returnConnection(jedis);
        }
    }


    public Set<String> zrevrangeByScore(String key, double max, double min) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrevrangeByScore(key, max, min);
        } catch (Throwable e) {
            logger.error("zrevrangeByScore", e);
            throw e;
        } finally {
            returnConnection(jedis);
        }
    }

    public Long zadd(String key, Map<String, Double> scoreMembers) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zadd(key, scoreMembers);
        } catch (Throwable e) {
            logger.error("zadd", e);
            throw e;
        } finally {
            returnConnection(jedis);
        }
    }

    public Long zrem(String key, String supplyId) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.zrem(key, supplyId);
        } catch (Throwable e) {
            logger.error("delItemSupplyWeight",e);
            throw e;
        }finally {
            returnConnection(jedis);
        }
    }


}
