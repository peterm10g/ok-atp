package com.lsh.atp.core.dao.jedis.salerule;

import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisHashDao;
import com.lsh.atp.core.model.salerule.SaleRule;
import com.lsh.base.common.json.JsonUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by zhangqiang on 17/7/26.
 */
@Component
public class SaleRuleJedisDao {

    @Autowired
    private JedisHashDao jedisHashDao;

    /**
     * 获取指定区域,指定supplys字符串的出货规则
     * @param zoneCode
     * @param supplys
     * @return
     */
    public SaleRule selectBySupplys(String zoneCode, String supplys){
        String redisKey = MessageFormat.format(RedisKeyConstant.SALE_RULE,zoneCode);
        String result =  jedisHashDao.hget(redisKey,supplys);
        return JsonUtils2.json2Obj(result,SaleRule.class);
    }

    public void setSaleRule(SaleRule saleRule) {
        String redisKey = MessageFormat.format(RedisKeyConstant.SALE_RULE,saleRule.getZoneCode());
        this.jedisHashDao.hset(redisKey,saleRule.getSupplys(),JsonUtils2.obj2Json(saleRule));
    }
}
