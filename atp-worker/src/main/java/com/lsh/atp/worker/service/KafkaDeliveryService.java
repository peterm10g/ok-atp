package com.lsh.atp.worker.service;

import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisStringDao;
import com.lsh.atp.core.model.area.Warehouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 16/9/1.
 */
@Component
public class KafkaDeliveryService {

    Logger logger = LoggerFactory.getLogger(KafkaDeliveryService.class);

    @Resource(name = "marketJdbcTemplate")
    private NamedParameterJdbcTemplate marketJdbcTemplate;

    @Autowired
    private JedisStringDao jedisStringDao;

    /**
     * 判断该同步信息是否被处理过,如果已经被处理过,则返回true。
     *
     * @param messageId  messageId
     *
     * @return 如果已经被处理过,则返回true
     * **/
    public boolean isHandled(String messageId) {
        return (jedisStringDao.get(MessageFormat.format(RedisKeyConstant.KAFKA_MESSAGE_HANDLED,messageId)) != null );
    }


    /**
     * 判断该商品是否需要排除
     *
     * 以下几种情况的库存需要被排除:
     *
     * 1.该区域售罄的商品
     *
     *
     * @param wumartCode 商品物美码
     * @param warehouse warehouse对象
     * @return 返回true,说明该商品需要被排除
     * **/
    public Boolean isExcluded(final String wumartCode , final Warehouse warehouse){
        //根据code,zoneId查询item_consignment表
        String sql = "select type from item_consignment_code where code = :wumartCode and zone_id = :zoneId and type = 2 and valid = 1";
        Map<String,Object> params = new HashMap<String, Object>(4);
        params.put("wumartCode",wumartCode);
        params.put("zoneId",warehouse.getZoneCode());
        List<Integer> typeList = this.marketJdbcTemplate.queryForList(sql,params,Integer.class);

        //1.排除该区域售罄的商品
        if(typeList.size() > 0){
            return true;
        }

        return false;
    }

    /**
     * 处理失败的信息存入redis中
     *
     * @param messageId messageId
     * @param json json
     *
     * **/
    public void putFailureToRedis(final String messageId, final String json) {
        String key = MessageFormat.format(RedisKeyConstant.KAFKA_MESSAGE_FAILURE,messageId);
        jedisStringDao.set(key,json);
    }

    /**
     * 将处理过的信息存入redis中,以除重
     *
     * @param json json
     * @param messageId messageId
     * **/
    public void putHandledToRedis(String messageId, String json) {
        String key = MessageFormat.format(RedisKeyConstant.KAFKA_MESSAGE_HANDLED,messageId);
        jedisStringDao.set(key,"success");      //先不存json
        jedisStringDao.expire(key,3*24*3600);   //保存3天时间。
    }

}
