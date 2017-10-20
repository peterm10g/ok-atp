package com.lsh.atp.core.service.area;

import com.lsh.atp.core.dao.area.WarehouseDao;
import com.lsh.atp.core.dao.jedis.area.WarehouseJedisDao;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.task.AsyncEvent;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangqiang on 16/8/20.
 */
@Component
public class WarehouseService {

    @Autowired
    private WarehouseDao warehouseDao;

    @Autowired
    private WarehouseJedisDao warehouseJedisDao;


    /**
     * 根据dc获取对应的WareHouse对象,如果没有,返回null
     *
     * @param dc       dc
     * @return WareHouse
     */
    public Warehouse getWarehouseByDc(String dc){
        return warehouseDao.getWarehoseByDc(dc);
    }

    /**
     * 根据dc,zoneCode获取对应的WareHouse对象,如果没有,返回null
     *
     * @param zoneCode zoneCode
     * @param dc       dc
     * @return WareHouse
     */
    public Warehouse getWarehouseByDc(String dc, String zoneCode){
        Warehouse warehouse = this.getWarehouseByDc(dc);
        if(warehouse != null){
            if(!warehouse.getZoneCode().equals(zoneCode)){
                return null;
            }
        }
        return warehouse;
    }


    public Integer getSupplyIdByDc(String dcId){
        return warehouseDao.getSupplyIdByDc(dcId);
    }

    public List<Warehouse> getFrozenWarehouse(String zoneCode){
        return this.getWarehouseByType(zoneCode,2);
    }

    public List<Warehouse> getCommonWarehouse(String zoneCode, Integer type) {
        return this.getWarehouseByType(zoneCode,type);
    }

    /**
     * 查询某地区,指定货主的仓库。查询较多,先查缓存,缓存中没有查db
     * @param zoneCode
     * @param supply
     * @return
     */
    public List<Warehouse> getWarehouseBySupply(String zoneCode, Integer supply){
        List<Warehouse> warehouses = this.warehouseJedisDao.getWarehouseBySupply(zoneCode,supply);
        if(CollectionUtils.isEmpty(warehouses)){        //redis没查到
            warehouses = this.warehouseDao.getWarehouseBySupply(zoneCode,supply);

            //异步更新redis
            AsyncEvent.post(warehouses);
        }

        //过滤DC10
        Iterator<Warehouse> iterable = warehouses.iterator();
        while(iterable.hasNext()){
            Warehouse warehouse = iterable.next();
            if("DC10".equalsIgnoreCase(warehouse.getDcReal())){
                iterable.remove();
            }
        }

        return warehouses;
    }


    private List<Warehouse> getWarehouseByType(String zoneCode, Integer type){
        return warehouseDao.getWarehouseByType(zoneCode,type);
    }


}
