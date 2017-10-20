package com.lsh.atp.core.dao.jedis.area;

import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.jedis.JedisHashDao;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.base.common.json.JsonUtils2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17/7/26.
 */
@Component
public class WarehouseJedisDao {

    @Autowired
    private JedisHashDao jedisHashDao;

    /**
     * 查询某地区,指定货主的仓库。
     * @param zoneCode
     * @param supply
     * @return
     */
    public List<Warehouse> getWarehouseBySupply(String zoneCode,Integer supply){
        List<Warehouse> warehouses = new ArrayList<>();
        String redisKey = MessageFormat.format(RedisKeyConstant.WAREHOUSE_ZONE,zoneCode);
        Map<String,String> map = jedisHashDao.hgetAll(redisKey);      //supply:dc -> Warehouse Json
        String supplyPrefix = String.valueOf(supply);
        for(String key : map.keySet()){
            if(key.startsWith(supplyPrefix)){
                Warehouse warehouse = JsonUtils2.json2Obj(map.get(key),Warehouse.class);
                warehouses.add(warehouse);
            }
        }
        return warehouses;
    }

    public void setWarehouseZone(List<Warehouse> warehouses) {
        for(Warehouse warehouse : warehouses){
            this.setWarehouseZone(warehouse);
        }
    }

    public void setWarehouseZone(Warehouse warehouse){
        String zoneCode = warehouse.getZoneCode();
        String redisKey = MessageFormat.format(RedisKeyConstant.WAREHOUSE_ZONE,zoneCode);
        SupplyDc supplyDc = new SupplyDc(warehouse.getSupplyId(),warehouse.getDcId());
        this.jedisHashDao.hset(redisKey,supplyDc.toString(),JsonUtils2.obj2Json(warehouse));
    }
}
