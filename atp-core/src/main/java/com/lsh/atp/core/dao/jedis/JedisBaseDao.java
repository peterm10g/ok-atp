package com.lsh.atp.core.dao.jedis;

import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

/**
 * Created by zhangqiang on 16/12/8.
 */
public abstract class JedisBaseDao {

    private static final Logger logger = LoggerFactory.getLogger(JedisBaseDao.class);

    @Resource(name = "jedisPool")
    private JedisPool jedisPool;


    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public Long deleteKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.del(key);
        } catch (Throwable e) {
            logger.error("deleteKey error", e);
            AsyncEvent.post(new EmailModel(e.getMessage(), "redis报警"));
            return 0L;
        } finally {
            try {
                if (null != jedis) {
                    jedis.close();
                }
            } catch (Throwable e) {
                logger.error("redis close 错误", e);
            }
        }
    }

    public Object eval(String script, int keyCount, String... params) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.eval(script, keyCount, params);
        } catch (Throwable e) {
            logger.error("eval error", e);
            AsyncEvent.post(new EmailModel(e.getMessage(), "redis报警"));
            return 0L;
        } finally {
            try {
                if (null != jedis) {
                    jedis.close();
                }
            } catch (Throwable e) {
                logger.error("redis close 错误", e);
            }
        }
    }

    public Long  expire(String key, int seconds){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.expire(key,seconds);
        }catch (Throwable e) {
            logger.error("expire", e);
        }finally {
            this.returnConnection(jedis);
        }
        return -1L;
    }

    public void returnConnection(Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
    }
}
