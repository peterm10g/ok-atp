package com.lsh.atp.core.service.hold;

import com.lsh.atp.api.model.Hold.HoldRequest;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.Item;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.model.baseVo.PreHold;
import com.lsh.atp.core.constant.BusiConstant;
import com.lsh.atp.core.constant.RedisKeyConstant;
import com.lsh.atp.core.dao.area.WarehouseDao;
import com.lsh.atp.core.dao.hold.SkuHoldDao;
import com.lsh.atp.core.dao.hold.SkuHoldQtyDao;
import com.lsh.atp.core.dao.inventory.InventoryLogicDao;
import com.lsh.atp.core.dao.jedis.JedisStringDao;
import com.lsh.atp.core.enums.HoldStatus;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.hold.SkuHold;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import com.lsh.atp.core.service.common.RandomUtil;
import com.lsh.atp.core.service.salerule.SaleWeightItemService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.CurrentDcModel;
import com.lsh.atp.core.task.model.IncrInventoryLogicModel;
import com.lsh.atp.core.util.AtpAssert;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Project Name: lsh-atp
 * Created by miaozhuang
 * Date: 16/7/16
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.hold.
 * desc:预占接口业务层
 */
@Component
@Transactional(readOnly = true)
public class HoldService {

    private static Logger logger = LoggerFactory.getLogger(HoldService.class);

    @Autowired
    private SkuHoldDao skuHoldDao;

    @Autowired
    private InventoryLogicDao inventoryLogicDao;

    @Autowired
    private SkuHoldQtyDao skuHoldQtyDao;

    @Autowired
    private WarehouseDao warehouseDao;


    @Autowired
    private JedisStringDao jedisStringDao;

    @Autowired
    private StrategyContextService strategyContextService;


    @Autowired
    private SaleWeightItemService saleWeightItemService;

    /**
     * 预占库存
     *
     * @param holdRequest 请求对象
     * @return 请求结果
     */
    @Transactional(rollbackFor = Exception.class)
    public PreHold skuPreHold(HoldRequest holdRequest) {
        String sequenceId =  holdRequest.getSequence();
        logger.info("订单号 : " + sequenceId + ",进入skuPreHold方法内部");

        PreHold preHold = new PreHold();

        long time_1 = System.currentTimeMillis();

        SkuHold skuHold = this.initSkuHold(holdRequest);

        //判断该订单号+channel是否已经存在
        SkuHold dbSkuHold = this.getSkuHoldList(skuHold);
        if (dbSkuHold != null) {
            throw new BusinessException(ExceptionStatus.E2001012.getCode(), ExceptionStatus.E2001012.getMessage());
        }

        //(1)生成预占记录,得到预占id
        if (skuHoldDao.insert(skuHold) <= 0) {
            throw new BusinessException(ExceptionStatus.E2001012.getCode(), ExceptionStatus.E2001012.getMessage());
        }

        int isdecrease = 0;

        if (holdRequest.getIsDecrease() != null) {
            isdecrease = holdRequest.getIsDecrease();
        }

        //预占库存
        logger.info("订单号 : " + sequenceId + ",开始执行preHoldInventoryLogic方法");
        String subZoneCode = holdRequest.getSaleAreaCode();
        if( StringUtils.isBlank(subZoneCode)){
            subZoneCode = Zone.parse(skuHold.getZoneCode()).getDefaultSubZoneCode();
        }
        int preNo = this.preHoldInventoryLogic(skuHold, holdRequest.getItems(),subZoneCode, isdecrease);
        logger.info("订单号 : " + sequenceId + ",结束执行preHoldInventoryLogic方法");

        preHold.setResNo(String.valueOf(preNo));
        preHold.setHoldNo(skuHold.getHoldNo());
        preHold.setHoldStatus(String.valueOf(skuHold.getStatus()));
        preHold.setZoneCode(skuHold.getZoneCode());

        strategyContextService.insertLog(Integer.parseInt(holdRequest.getChannel()), holdRequest.getSequence(), HoldStatus.DEDUCTION.getKey(), skuHold.getId(), holdRequest.getItems());

        logger.info("订单号 : " + holdRequest.getSequence() + ",skuPreHold方法内部用时 : " + (System.currentTimeMillis() - time_1));

        return preHold;
    }





    /**
     * sequence 是否存在
     *
     * @param skuHold 预占对象
     * @return 数据库预占对象
     */
    private SkuHold getSkuHoldList(SkuHold skuHold) {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("channel", skuHold.getChannel());
        param.put("sequenceId", skuHold.getSequenceId());
        List<SkuHold> skList = skuHoldDao.getSkuHoldList(param);

        if (skList != null && !skList.isEmpty()) {
            return skList.get(0);
        }

        return null;
    }

    /**
     * 初始化SkuHold
     *
     * @param holdRequestVo 初始化参数
     * @return 预占对象
     */
    private SkuHold initSkuHold(HoldRequest holdRequestVo) {
        SkuHold skuHold = new SkuHold();

        long currentTime = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        //组织SkuHold对象
        skuHold.setCreatedAt(currentTime);
        skuHold.setUpdatedAt(currentTime);
        skuHold.setStatus(HoldStatus.DEDUCTION.getKey());   //没有预占阶段,直接扣减
        skuHold.setHoldEndTime(holdRequestVo.getHoldEndTime());
        skuHold.setSequenceId(holdRequestVo.getSequence());
        if (StringUtils.isNotBlank(holdRequestVo.getAreaCode())) {
            skuHold.setAreaId(Integer.parseInt(holdRequestVo.getAreaCode()));
        }
        if (StringUtils.isNotBlank(holdRequestVo.getZoneCode())) {
            String zoneCode = holdRequestVo.getZoneCode();
            skuHold.setZoneCode(zoneCode);
        }
        skuHold.setChannel(Integer.parseInt(holdRequestVo.getChannel()));
        String holdNo = RandomUtil.getHoldNoStr(holdRequestVo.getChannel(), holdRequestVo.getSequence());
        skuHold.setHoldNo(holdNo);

        return skuHold;
    }


    /**
     * 预占逻辑库存
     *
     * @param skuHold    预占对象
     * @param itemList   商品列表
     * @param isdecrease 是否扣减
     */
    private int preHoldInventoryLogic(SkuHold skuHold, List<Item> itemList,String saleAreaCode, int isdecrease) {
        Collections.sort(itemList);
        final int size = itemList.size();
        final List<SkuHoldQty> skuHoldQtyList = new ArrayList<>(size);
        final Map<Long,SupplyDc> currentDcMap = new HashMap<>(size);
        final String zonecode = skuHold.getZoneCode();
        final Long holdId = skuHold.getId();


        if(saleAreaCode == null){        //为了兼容之前版本
            if(skuHold.getZoneCode().equals(Zone.BEIJING.getZoneCode())){
                saleAreaCode = "SABJ01";
            }else{
                saleAreaCode = skuHold.getZoneCode();
            }
        }

        if(isdecrease != 1){
            throw new BusinessException(ExceptionStatus.E1001001.getCode(),"isDecrease目前必须为1");
        }

        //获取权重 item-supplyDcStr
        final Map<Long, Collection<String>> weightMap = this.saleWeightItemService.getSupplyWeight(itemList, zonecode,saleAreaCode);

        List<Item> subItemList = new ArrayList<>();     //赠品list

        for (Item item : itemList) {
            Long itemId = item.getItemId();
            Long holdQty = item.getQty();
            if(holdQty == null){
                throw new BusinessException(ExceptionStatus.E1001001.getCode(),"qty不能为空");
            }

            //如果是赠品
            if (StringUtils.isNotBlank((item.getItemType())) && item.getItemType().equals("2")) {
                subItemList.add(item);
                continue;
            }

            Collection<String> supplyDcList = weightMap.get(itemId);
            if(CollectionUtils.isEmpty(supplyDcList)){
                throw new BusinessException(ExceptionStatus.E1001001.getCode(),"该商品没有对应的权重");
            }


            this.waitItemLock(itemId);  //等待redis锁

            //预占并扣减
            if(isdecrease == 1){
                SkuHoldQty skuHoldQty = this.decreaseItem(itemId,zonecode,supplyDcList,holdQty,holdId);
                skuHoldQtyList.add(skuHoldQty);
                currentDcMap.put(itemId,new SupplyDc(skuHoldQty.getSupplyId(),skuHoldQty.getDc()));
            }
        }

        int resFlag = BusiConstant.HOLD_SUCCESS;

        //处理赠品的扣减
        for (Item item : subItemList) {
            Long itemId = item.getItemId();
            Long holdQty = item.getQty();

            Collection<String> supplyDcList = weightMap.get(itemId);
            if(CollectionUtils.isEmpty(supplyDcList)){
                throw new BusinessException(ExceptionStatus.E1001001.getCode(),"该商品没有对应的权重");
            }

            if(isdecrease == 1){
                SkuHoldQty skuHoldQty = this.descreadFreeItem(itemId,zonecode,supplyDcList,holdQty,holdId);
                if(skuHoldQty == null){
                    resFlag = BusiConstant.SUB_HOLD_SUCCESS;
                    continue;
                }
                skuHoldQtyList.add(skuHoldQty);
                currentDcMap.put(itemId,new SupplyDc(skuHoldQty.getSupplyId(),skuHoldQty.getDc()));

                if(skuHoldQty.getHoldQty().compareTo(holdQty) < 0){
                    resFlag = BusiConstant.SUB_HOLD_SUCCESS;
                }
            }
        }

        Integer num = this.skuHoldQtyDao.insertAll(skuHoldQtyList);
        if (num <= 0) {
            throw new BusinessException(ExceptionStatus.E2001004.getCode(), ExceptionStatus.E2001004.getMessage());
        }

        //异步更新当前售卖仓库
        AsyncEvent.post(new CurrentDcModel(skuHold.getSequenceId(),zonecode,saleAreaCode,currentDcMap));
        //异步更新库存redis
        AsyncEvent.post(new IncrInventoryLogicModel(skuHold.getSequenceId(),zonecode,skuHoldQtyList,IncrInventoryLogicModel.Operation.DCREASE));

        return resFlag;
    }

    /**
     * 预占扣减商品
     * @param itemId
     * @param zonecode
     * @param supplyDcList
     * @param holdQty
     * @return
     */
    private SkuHoldQty decreaseItem(final Long itemId, final String zonecode, final Collection<String> supplyDcList,final Long holdQty,final Long holdId) {
        for(String supplyDcStr : supplyDcList){
            SupplyDc supplyDc = new SupplyDc(supplyDcStr);
            int num = this.inventoryLogicDao.decreaseLogicInventory(itemId,zonecode,supplyDc.getSupply(),supplyDc.getDc(),holdQty);
            if(num > 0){    //success
                return new SkuHoldQty(null,holdId,itemId,holdQty,supplyDc.getSupply(),supplyDc.getDc(),0L);
            }else{          //failed
                continue;
            }
        }

        //都没扣减成功,库存不足
        throw new BusinessException(ExceptionStatus.E2001016.getCode(),itemId.toString(),ExceptionStatus.E2001016.getMessage());
    }

    /**
     * 预占扣减赠品
     * @param itemId
     * @param zonecode
     * @param supplyDcList
     * @param holdQty
     * @return
     */
    private SkuHoldQty descreadFreeItem(final Long itemId, final String zonecode, final Collection<String> supplyDcList,final Long holdQty,final Long holdId){
        try{
            return this.decreaseItem(itemId,zonecode,supplyDcList,holdQty,holdId);
        }catch (BusinessException e){      //所有仓库都未扣减成功,依据权重扣减当前数量
            try{
                this.waitItemLock(itemId);      //等待redis锁

                //加redis锁
                jedisStringDao.set(MessageFormat.format(RedisKeyConstant.HOLD_INVENTORY_ITEM, String.valueOf(itemId)), "1");//锁
                jedisStringDao.expire(MessageFormat.format(RedisKeyConstant.HOLD_INVENTORY_ITEM, String.valueOf(itemId)), 500);//500毫秒

                for(String supplyDcStr : supplyDcList){
                    SupplyDc supplyDc = new SupplyDc(supplyDcStr);
                    Integer supply = supplyDc.getSupply();
                    String dc = supplyDc.getDc();

                    //查询当前库存数量
                    Long curQty = this.inventoryLogicDao.getQty(itemId,zonecode,supply,dc);
                    if(curQty >= holdQty){      //当前库存足够
                        int num = this.inventoryLogicDao.decreaseLogicInventory(itemId,zonecode,supply,dc,holdQty);
                        if(num > 0){    //success
                            return new SkuHoldQty(null,holdId,itemId,holdQty,supplyDc.getSupply(),supplyDc.getDc(),0L);
                        }else{          //failed
                            continue;
                        }
                    }else{
                        int num = this.inventoryLogicDao.decreaseLogicInventory(itemId,zonecode,supply,dc,curQty);
                        if(num > 0){    //success
                            return new SkuHoldQty(null,holdId,itemId,curQty,supplyDc.getSupply(),supplyDc.getDc(),0L);
                        }else{          //failed
                            continue;
                        }
                    }
                }
                //库存都为0,返回null
                return null;
            }catch (Throwable ex) {
                logger.error("扣减赠品库存异常 itemId:" + itemId);
            } finally {
                jedisStringDao.set(MessageFormat.format(RedisKeyConstant.HOLD_INVENTORY_ITEM, itemId), "0");//释放锁
            }
        }
        return null;
    }

    public void waitItemLock(Long itemId){
        //查看redis是否释放锁
        int tryCount = 0;
        String lock = jedisStringDao.get(MessageFormat.format(RedisKeyConstant.HOLD_INVENTORY_ITEM, itemId.toString()));
        while (StringUtils.isNotBlank(lock) && lock.equals("1")) {
            try {
                if(tryCount ++ > 20){
                    throw new BusinessException(ExceptionStatus.E2001020.getCode(),ExceptionStatus.E2001020.getCode());
                }
                logger.info("等待redis锁...");
                Thread.sleep(10);

                lock = jedisStringDao.get(MessageFormat.format(RedisKeyConstant.HOLD_INVENTORY_ITEM, itemId.toString()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 根据channel,sequence获取预占对象
     *
     * @param sequence sequence
     * @param channel  channel
     * @return SkuHold
     **/
    public SkuHold getSkuHoldByChannelAndSequence(final int channel, final String sequence) {
        AtpAssert.notNull(sequence, "1001001", "sequence不许为空");

        return this.skuHoldDao.getSkuHoldByChannelAndSequence(channel, sequence);
    }

    /**
     * 查询预展明细(ofc)
     *
     * @param holdId holdID
     * @param items  items
     * @return 返回结果
     **/
    public List<ItemDc> queryHoldDetail(final long holdId, final List<ItemDc> items) {

        List<SkuHoldQty> skuHoldQtys;
        if (items == null || items.size() == 0) {     //如果items为空,则查询订单全部商品信息
            skuHoldQtys = this.skuHoldQtyDao.getSkuHoldQtyByHoldId(holdId);
        } else {
            skuHoldQtys = this.skuHoldQtyDao.getSkuHoldQtyForConsignment(String.valueOf(holdId), items);
        }


        List<ItemDc> result = new ArrayList<>(skuHoldQtys.size());

        for (SkuHoldQty skuHoldQty : skuHoldQtys) {
            ItemDc item = new ItemDc();
            item.setItemId(skuHoldQty.getSkuId());
            item.setQty(skuHoldQty.getHoldQty());
            item.setDc(warehouseDao.getDcId(skuHoldQty.getDc(),skuHoldQty.getSupplyId()));
            result.add(item);
        }

        return result;


    }
}
