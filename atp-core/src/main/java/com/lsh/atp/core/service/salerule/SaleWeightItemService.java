package com.lsh.atp.core.service.salerule;

import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.core.dao.jedis.salerule.SaleWeightItemJedisDao;
import com.lsh.atp.core.dao.salerule.SaleWeightItemDao;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.salerule.SaleWeightItem;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.task.model.SupplyWeightModel;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by zhangqiang on 17/7/27.
 */
@Component
public class SaleWeightItemService {

    private final static Logger logger = LoggerFactory.getLogger(SaleWeightItemService.class);

    @Autowired
    private SaleWeightItemDao saleWeightItemDao;

    @Autowired
    private SaleWeightItemJedisDao saleWeightItemJedisDao;

    /**
     * 获取指定商品的supplyDC的权重,由大到小排序
     * @param itemId
     * @param zoneCode
     * @param subZoneCode
     * @return
     */
    public Collection<SupplyDc> getSupplyWeightFromDb(Long itemId, String zoneCode, String subZoneCode){
//        Collection<SupplyDc> result = new ArrayList<>();
//        Collection<String> supplyStrs = this.saleWeightItemJedisDao.getSupplyWeight(itemId,zoneCode,subZoneCode);
//        if(result != null){
//            for(String supplyStr : supplyStrs){
//                result.add(new SupplyDc(supplyStr));
//            }
//        }else{  //redis中查不到,查询db
//            result = this.saleWeightItemDao.getSupplyWeight(itemId,zoneCode,subZoneCode);
//        }
//        return result;
        return this.saleWeightItemDao.getSupplyWeight(itemId,zoneCode,subZoneCode);
    }

    public Map<Long,Collection<String>> getSupplyWeight(Long[] itemList, String zoneCode, String subZoneCode) {
        Map<Long, Collection<String>> weightMap = this.saleWeightItemJedisDao.getSupplyWeight(itemList,zoneCode,subZoneCode);
        this.replaceNullFromDb(weightMap,zoneCode,subZoneCode);
        return weightMap;
    }

    /**
     * 获取指定商品列表,指定区域内的出货规则,有序supplyDC字符串代表出货权重由大到小。
     * 先取redis,redis中取不到查询db,并异步set进redis
     * @param itemList
     * @param zoneCode
     * @param subZoneCode
     * @return
     */
    public Map<Long,Collection<String>> getSupplyWeight(List<Item> itemList, String zoneCode, String subZoneCode) {
        Map<Long, Collection<String>> weightMap = this.saleWeightItemJedisDao.getSupplyWeight(itemList,zoneCode,subZoneCode);
        this.replaceNullFromDb(weightMap,zoneCode,subZoneCode);
        return weightMap;
    }

    /**
     * 删除指定item的出货权重
     * @param itemId
     * @param zoneCode
     * @param subZoneCode
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSaleWeightItem(Long itemId, String zoneCode, String subZoneCode){
        //删除db
        int num = this.saleWeightItemDao.deleteByItem(itemId,zoneCode,subZoneCode);
        if(num <= 0 ){
            throw new BusinessException(ExceptionStatus.E2001039.getCode(), ExceptionStatus.E2001039.getMessage());
        }
        //删除redis
        this.saleWeightItemJedisDao.delItemWeight(itemId,zoneCode,subZoneCode);
    }

    /**
     * 添加指定的出货权重
     * @param saleWeightItem
     */
    @Transactional(rollbackFor = Exception.class)
    public void addSaleWeightItem(SaleWeightItem saleWeightItem){
        //insert db
        int num = this.saleWeightItemDao.insert(saleWeightItem);
        if (num != 1) {
            throw new BusinessException(ExceptionStatus.E2001041.getCode(), ExceptionStatus.E2001041.getMessage());
        }
        //insert redis
        boolean isSuccess = this.saleWeightItemJedisDao.setItemWeight(saleWeightItem.getItemId(), saleWeightItem.getZoneCode(), saleWeightItem.getSubZoneCode(), saleWeightItem.getWeight().doubleValue(), saleWeightItem.getSupplyId(),saleWeightItem.getDc());
        if (!isSuccess) {
            throw new BusinessException(ExceptionStatus.E2001046.getCode(), ExceptionStatus.E2001046.getMessage());
        }
    }

    /**
     * 从数据库中查询出指定item的出货权重,设置到redis中
     * @param itemId
     * @param zoneCode
     * @param subZoneCode
     */
    public void setSaleWeightItemRedis(Long itemId, String zoneCode, String subZoneCode) {
        //查询db
        List<SaleWeightItem> saleWeightItems = this.saleWeightItemDao.getSupplyWeightWithWeight(itemId, zoneCode, subZoneCode);
        for(SaleWeightItem saleWeightItem : saleWeightItems){
            this.saleWeightItemJedisDao.setItemWeight(itemId,zoneCode,subZoneCode,saleWeightItem.getWeight().doubleValue(),saleWeightItem.getSupplyId(),saleWeightItem.getDc());
        }
    }


    private void replaceNullFromDb(Map<Long, Collection<String>> weightMap, String zoneCode, String subZoneCode) {
        for(Map.Entry<Long,Collection<String>> entry : weightMap.entrySet()){
            Long itemId = entry.getKey();
            if(CollectionUtils.isEmpty(entry.getValue())){   //redis中查不到,查询db
                List<SupplyDc> list  = this.saleWeightItemDao.getSupplyWeight(itemId, zoneCode, subZoneCode);
                if (CollectionUtils.isEmpty(list)) {
                    // TODO: 17/7/31 测试环境暂时关了 
//                    AsyncEvent.post(new EmailModel("数据库与redis中都没有此商品的货主权重, itemId = " + itemId, "货主权重预警"));
                    continue;
                } else {
                    //异步同步redis
                    AsyncEvent.post(new SupplyWeightModel(itemId, zoneCode,subZoneCode));
                }
                //重新set进map
                Collection<String> supplys = new ArrayList<>(list.size());
                for(SupplyDc supplyDc : list){
                    supplys.add(supplyDc.toString());
                }
                weightMap.put(itemId,supplys);
            }
        }
    }

    // TODO: 17/7/27
    public boolean isHasWeight(Long itemId, String zoneCode, String subZoneCode, String supplyDcStr) {
        return true;
    }
}
