package com.lsh.atp.core.dao.jedis;

import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * Created by zhangqiang on 16/12/8.
 */
@Repository
public class JedisHashDao extends JedisBaseDao{
    private static final Logger logger = LoggerFactory.getLogger(JedisHashDao.class);

    public String hget(final String key, final String field){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hget(key,field);
        }catch (Throwable e){
            logger.error("redis错误",e);
            throw e;
        }finally {
            returnConnection(jedis);
        }
    }

    public Map<String, String> hgetAll(String key){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hgetAll(key);
        }catch (Throwable e){
            logger.error("redis错误",e);
            throw e;
        }finally {
            returnConnection(jedis);
        }
    }


    public Long hset(final String key, final String field, final String value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hset(key,field,value);
        }catch (Throwable e){
            logger.error("redis错误",e);
            AsyncEvent.post(new EmailModel(e.getMessage(),"redis报警"));
            return -1L;
        }finally {
            returnConnection(jedis);
        }
    }

    public Long hincrBy(final String key, final String field, final long value){
        Jedis jedis = null;
        try{
            jedis = getJedis();
            return jedis.hincrBy(key,field,value);
        }catch (Throwable e){
            logger.error("redis错误",e);
            AsyncEvent.post(new EmailModel(e.getMessage(),"redis报警"));
            return 0L;
        }finally {
            returnConnection(jedis);
        }
    }

}
