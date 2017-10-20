package com.lsh.atp.core.service.inventory;

import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.model.inventory.RestoreInventoryRequest;
import com.lsh.atp.core.dao.hold.SkuHoldDao;
import com.lsh.atp.core.dao.hold.SkuHoldQtyDao;
import com.lsh.atp.core.dao.inventory.InventoryLogicDao;
import com.lsh.atp.core.dao.jedis.inventory.InventoryLogicJedisDao;
import com.lsh.atp.core.enums.HoldStatus;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.hold.SkuHold;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.hold.HoldLogService;
import com.lsh.atp.core.service.redis.currentdc.CurrentDcRedisService;
import com.lsh.atp.core.service.salerule.SaleWeightItemService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.IncrInventoryLogicModel;
import com.lsh.atp.core.util.AtpAssert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zhangqiang on 16/7/7.
 */

@Component
public class InventoryLogicService {

    private static Logger logger = LoggerFactory.getLogger(InventoryLogicService.class);

    @Autowired
    private InventoryLogicDao inventoryLogicDao;

    @Autowired
    private InventoryLogicJedisDao inventoryLogicJedisDao;

    @Autowired
    private SaleWeightItemService saleWeightItemService;

    @Autowired
    private SkuHoldDao skuHoldDao;

    @Autowired
    private SkuHoldQtyDao skuHoldQtyDao;

    @Autowired
    private HoldLogService holdLogService;

    @Autowired
    private CurrentDcRedisService currentDcRedisService;

    public InventoryLogic getInventoryLogic(InventoryLogic model){
        return this.inventoryLogicDao.getInventoryLogicByModel(model);
    }

    public Long getInventoryQty(Long itemId, String zoneCode, Integer supply, String dc){
        Long qty = this.inventoryLogicJedisDao.getInventoryQty(itemId,zoneCode,supply,dc);
        if(qty == null){    //redis中没有,查询db
            qty = this.inventoryLogicDao.getQty(itemId,zoneCode,supply,dc);
        }
        return qty != null ? qty : 0L;
    }

    /***
     * 根据出货权重查询redis库存,商城查询接口
     *
     * @param
     * @return
     */
    public Map<Long, Long> getInventoryLogic(Collection<Long> items, String zoneCode, String subZoneCode) {
        final Map<Long, Long> inventory = new HashMap<>((int)(items.size() / 0.75));

        //查询库存
        Map<Long, Map<String, String>> supplyDCQtyMap = this.getSupplyDCQtyMap(items,zoneCode);
        //查询规则
        Long[] itemArray = new Long[items.size()];
        Map<Long,Collection<String>> supplyDcMap = this.saleWeightItemService.getSupplyWeight(items.toArray(itemArray),zoneCode,subZoneCode);

        for (Map.Entry<Long, Map<String,String>> entry : supplyDCQtyMap.entrySet()) {
            Long itemId = entry.getKey();
            Map<String,String> supplyDcQtyMap = entry.getValue();   //supplyDC -> qty

            Collection<String> weights = supplyDcMap.get(itemId);   //出货权重

            //根据商品库存条数得到返回的数量
            long qty = 0;
            int size = supplyDcQtyMap.size();
            if(size == 0){
                qty = 0;
            } else{
                for(Map.Entry<String,String> stockEntry : supplyDcQtyMap.entrySet()){
                    String supplyDcStr = stockEntry.getKey();
                    long temp = Long.parseLong(stockEntry.getValue());
                    //查询此商品是否有权重
                    if(weights.contains(supplyDcStr)){
                        qty = qty > temp ? qty : temp;
                    }
                }
            }

            inventory.put(itemId,qty);
        }

        return inventory;
    }

    /***
     * 根据出货权重查询redis库存,mis查询接口
     *
     * @return
     */
    public Map<Long, List<ItemDc>> getInventoryLogicDetail(Collection<Long> items, String zoneCode){
        int mapSize = (int)(items.size() / 0.75);
        Map<Long, List<ItemDc>> inventory = new HashMap<>(mapSize);
        Map<Long, Map<String, String>> supplyDCQtyMap = this.getSupplyDCQtyMap(items,zoneCode);

        for(Map.Entry<Long, Map<String, String>> supplyDcQtyEntry : supplyDCQtyMap.entrySet()){
            Long itemId = supplyDcQtyEntry.getKey();
            Map<String,String> map = supplyDcQtyEntry.getValue();

            //转为ItemDc格式
            List<ItemDc> itemDcs = new ArrayList<>(map.size());
            for(Map.Entry<String,String> stockEntry : map.entrySet()) {
                SupplyDc supplyDc = new SupplyDc(stockEntry.getKey());
                String dc = currentDcRedisService.getDcId(supplyDc);
                ItemDc itemDc = new ItemDc(itemId,Long.parseLong(stockEntry.getValue()),dc);
                itemDcs.add(itemDc);
            }

            inventory.put(itemId,itemDcs);
        }
        return inventory;
    }

    /**
     * 批量查询指定item列表的库存列表,先查询redis,redis查不到的查询db
     * @param items
     * @param zoneCode
     * @return itemId -> Map(supplyDcStr -> qty)
     */
    public Map<Long,Map<String,String>> getSupplyDCQtyMap(Collection<Long> items, String zoneCode){
        Map<Long, Map<String, String>> result = this.inventoryLogicJedisDao.getInventoryLogic(items,zoneCode);
        this.replaceNullFromDb(result,zoneCode); //redis没查到的从db查
        return result;
    }

    public boolean addInventoryLogicRedis(InventoryLogic inventoryLogic){
        return this.inventoryLogicJedisDao.setInventoryLogicReids(inventoryLogic);
    }

    public void addInventoryLogicRedis(Collection<InventoryLogic> inventoryLogics){
        this.inventoryLogicJedisDao.setInventoryLogicReidsValue(inventoryLogics);
    }

    public void hincrInventoryRedis(String zoneCode, String supply, String dc, Long itemId, Long num){
        this.inventoryLogicJedisDao.hincrInventory(zoneCode,supply,dc,itemId,num);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateInventoryLogic(InventoryLogic inventoryLogic){
        int dbStatus = this.inventoryLogicDao.updateInventoryLogic(inventoryLogic);
        if(dbStatus > 0 ){
            return this.addInventoryLogicRedis(inventoryLogic);
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean insertInventoryLogic(InventoryLogic inventoryLogic){
        int dbStatus = this.inventoryLogicDao.insert(inventoryLogic);
        if(dbStatus > 0){
            return this.addInventoryLogicRedis(inventoryLogic);
        }
        return false;
    }


    private void replaceNullFromDb(Map<Long, Map<String, String>> supplyDCQtyMap, String zoneCode) {
        for(Map.Entry<Long, Map<String, String>> supplyDcQtyEntry : supplyDCQtyMap.entrySet()){
            if(CollectionUtils.isEmpty(supplyDcQtyEntry.getValue())){    //redis查询不到,查询db
                Long itemId = supplyDcQtyEntry.getKey();
                List<InventoryLogic> inventoryLogics = this.inventoryLogicDao.getInventoryLogicBySku(itemId,zoneCode);

                Map<String,String> tempMap = new HashMap<>();
                for(InventoryLogic inventoryLogic : inventoryLogics){
                    tempMap.put(new SupplyDc(inventoryLogic.getSupplyId(),inventoryLogic.getDC()).toString(),inventoryLogic.getInventoryQuantity().toString());
                    //异步设置redis
                    AsyncEvent.post(inventoryLogic);
                }
                supplyDCQtyMap.put(itemId,tempMap);
            }
        }
    }

















    /**
     * 根据skuid和dc查询逻辑库存纪录
     *
     * @param dc
     * @param skuId
     *
     * @return
     *
     * **/
    private InventoryLogic queryInventoryByDcAndSkuId(Long skuId,Integer supply,String dc, String zoneCode){
        List<InventoryLogic> list = inventoryLogicDao.getInventoryLogicBySupplyDc(dc,supply,zoneCode,skuId);

        if (list.size() == 0) {
            throw new BusinessException("2001003", "商品数据不存在");
        }
        if (list.size() > 1) {
            throw new BusinessException("2001005", "数据问题，同一区域同一种商品有多个");
        }
        return list.get(0);
    }


    /**
     * 根据商品id和areaCode查询库存
     *
     * @param  zoneCode
     * @param  items
     * @return
     * **/
    public List<Item> queryInventoryByAreaCode(String zoneCode, String saleAreaCode, List<Item> items, String uuid){
        //运行开始时间
        long startTime = System.currentTimeMillis();
        AtpAssert.notEmpty(items,"1001001","items不能为空");
        AtpAssert.notNull(zoneCode,"1001001","zone_code不能为空");

        //查询规则
        //Map<Long, Collection<String>> itemWeightmap = this.itemWeightRedisService.getSupplyWeight(items, zoneCode);

        //logger.info(uuid + " 完成查询出货权重");

        //查询库存
        List<Long> itemidList = new ArrayList<>(items.size());
        for(Item item : items){
            itemidList.add(item.getItemId());
        }
        Map<Long,Long> inventoryMap = this.getInventoryLogic(itemidList,zoneCode,saleAreaCode);
        logger.info(uuid + "完成查询库存数量");

        List<Item> result = new ArrayList<>(items.size());
        for(Map.Entry<Long,Long> entry : inventoryMap.entrySet()){
            result.add(new Item(entry.getKey(),entry.getValue()));
        }

        logger.info(uuid + " core query runtime is : " + (System.currentTimeMillis() - startTime));

        return result;
    }

    /**
     * 还原库存
     *
     * @param request
     * @return
     **/
    @Transactional(readOnly = false,rollbackFor = Exception.class)
    public String restoreInventory(RestoreInventoryRequest request) {
        String holdNo = request.getHoldId();        //其实是hold_no
        String sequence = request.getSequence();    //订单
        String channel = request.getChannel();      //渠道

        SkuHold skuHold = null;         //要还原的预占对象

        if(StringUtils.isNotBlank(holdNo)){         //如果hold_no不为空,则根据hold_no寻找skuHold对象
            skuHold = skuHoldDao.getSkuHoldByHoldNo(holdNo);

            if( ! skuHold.getSequenceId().equals(request.getSequence())){
                throw new BusinessException("2001013", "预占信息不存在");
            }
            if(skuHold.getChannel() != Integer.parseInt(request.getChannel())){
                throw new BusinessException("2001013", "预占信息不存在");
            }
        }else if(StringUtils.isNotBlank(sequence) && StringUtils.isNotBlank(channel)){  //如果订单号和渠道都不为空
            skuHold = skuHoldDao.getSkuHoldByChannelAndSequence(Integer.parseInt(channel),sequence);
            if(skuHold == null){
                throw new BusinessException("2001013", "预占信息不存在");
            }
            if( ! skuHold.getHoldNo().equals(request.getHoldId()) && !StringUtils.isBlank(request.getHoldId())){
                throw new BusinessException("2001013", "预占信息不存在");
            }
        }else{    //抛出异常
            throw new BusinessException("1001001","订单号和预占id必传一个");
        }

        if (skuHold == null) {
            throw new BusinessException("2001013", "预占信息不存在");
        }
        if (skuHold.getStatus() == HoldStatus.RESTORE.getKey()) {
            throw new BusinessException("2001006", "该预占已经还原,不能再次还原");
        }

        //不再考虑秒杀
        //暂时不考虑部分还原
        this.restoreInventoryLogic(skuHold, request.getIsFulfill());

        //成功后记录日志hold_Log
        try{
            holdLogService.insertLog(Integer.parseInt(channel),request.getSequence(),HoldStatus.RESTORE.getKey(),skuHold.getId(),null);
        }catch (Throwable e){
            logger.error("还原插入日志失败" + sequence, e);
        }

        return skuHold.getHoldNo();
    }

    /**
     * 还原逻辑库,全部还原
     *
     * 根据预占记录状态分为两种还原
     *
     * 1.状态为2,说明为扣减后的还原
     *
     * 2.状态为1,说名为预占后的还原
     *
     *
     * @param skuHold
     **/
    private void restoreInventoryLogic(final SkuHold skuHold, final Boolean isFulfill) {
        Long holdId = skuHold.getId();      //预占id
        String zoneCode = skuHold.getZoneCode();

        List<SkuHoldQty> skuHoldQtyList = null;
        if(isFulfill != null && isFulfill == false){         //还原库存
            //根据预占id获取预占明细
            skuHoldQtyList = skuHoldQtyDao.getSkuHoldQtyByHoldId(holdId);
            //循环更新逻辑库存表
            for (SkuHoldQty skuHoldQty : skuHoldQtyList) {

                //逻辑库存id
                Long id = this.queryInventoryByDcAndSkuId(skuHoldQty.getSkuId(),skuHoldQty.getSupplyId(),skuHoldQty.getDc(),zoneCode).getId();
                //根据id还原逻辑库存
                int num = 0;
                if (skuHold.getStatus() == HoldStatus.DEDUCTION.getKey()) {           //扣减后还原
                    num = inventoryLogicDao.restoreInventoryLogicByDecrease(id, skuHoldQty.getHoldQty(), skuHold.getId());
                } else if (skuHold.getStatus() == HoldStatus.PREHOLD.getKey()) {     //预占后还原
                    num = inventoryLogicDao.restoreInventoryLogicByHold(id, skuHoldQty.getHoldQty(), skuHold.getId());
                } else if(skuHold.getStatus() == HoldStatus.SUB_RESTORE.getKey()){    //部分还原,暂时不支持
                    throw new BusinessException("2001011","该状态不能全部还原");
                }else {
                    throw new BusinessException("2001011", "未知预占状态");
                }

                if (num == 0 ) {
                    throw new BusinessException("2001007", "还原失败");
                }

                //更新skuHoldQty的还原数量
                num = this.skuHoldQtyDao.addRestoreQty(holdId,skuHoldQty.getSkuId(),skuHoldQty.getDc(),skuHoldQty.getHoldQty());
                if(num <= 0){
                    throw new BusinessException("2001012", "更新还原数量失败");
                }
            }
        }

        //更新SKU_HOLD表状态
        skuHold.setStatus(HoldStatus.RESTORE.getKey());
        if (skuHoldDao.updateStatus(skuHold) == 0) {
            throw new BusinessException("2001008", "更新预占状态失败");
        }

        //异步更新库存redis
        if(skuHoldQtyList != null){
            AsyncEvent.post(new IncrInventoryLogicModel(skuHold.getSequenceId(),skuHold.getZoneCode(),skuHoldQtyList,IncrInventoryLogicModel.Operation.RESTORE));
        }
    }

}