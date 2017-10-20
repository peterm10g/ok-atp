package com.lsh.atp.core.service.mis;

import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.api.model.baseVo.ItemDc;
import com.lsh.atp.api.model.baseVo.ItemDetail;
import com.lsh.atp.api.model.baseVo.Salerule;
import com.lsh.atp.api.model.mis.QueryItemRuleRequest;
import com.lsh.atp.core.dao.jedis.salerule.SaleRuleJedisDao;
import com.lsh.atp.core.dao.salerule.SaleRuleDao;
import com.lsh.atp.core.enums.Zone;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.SupplyDc;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.model.salerule.SaleRule;
import com.lsh.atp.core.model.salerule.SaleWeightItem;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.service.inventory.InventoryLogicService;
import com.lsh.atp.core.service.redis.currentdc.CurrentDcRedisService;
import com.lsh.atp.core.service.salerule.SaleWeightItemService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.util.AtpAssert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by jingyuan on 16/10/18.
 * 查询库存(分DC和出货规则)
 */
@Component
public class SaleRuleService {

    private Logger logger = LoggerFactory.getLogger(SaleRuleService.class);

    @Autowired
    private SaleRuleDao saleRuleDao;

    @Autowired
    private SaleRuleJedisDao saleRuleJedisDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private CurrentDcRedisService currentDcService;

    @Autowired
    private SaleWeightItemService saleWeightItemService;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private InventoryLogicService inventoryLogicService;

    @Resource(name = "zoneCodeMap")
    private Map<String, String> zoneAreaMap;
//
//    @Autowired
//    private SaleWeightItemDao saleWeightItemDao;


    /**
     * 根据区域和type查询出货规则
     *
     * @param zoneCode
     * @param type
     * @return
     */
    public List<SaleRule> getSaleRuleList(String zoneCode, Integer type) {
        return this.saleRuleDao.selectByZonecodeAndType(zoneCode, type);
    }

    /**
     * 修改或新增商品售卖规则
     *
     * @param zoneCode 区域码
     * @param subZoneCode
     * @param itemId   itemid
     * @param dcs dcs
     **/
    @Transactional(rollbackFor = Exception.class)
    public void updateItemSaleRule(String zoneCode,String subZoneCode, Long itemId, List<String> dcs) {
        AtpAssert.notEmpty(dcs, ExceptionStatus.E1001001.getCode(), "dcs不能为空");
        if(StringUtils.isBlank(subZoneCode)){
            subZoneCode = Zone.parse(zoneCode).getDefaultSubZoneCode();
        }

        List<Warehouse> warehouses = new ArrayList<>(dcs.size());
        for(String dc : dcs){
            Warehouse warehouse = this.warehouseService.getWarehouseByDc(dc);
            AtpAssert.notNull(warehouse,ExceptionStatus.E2001027.getCode(),ExceptionStatus.E2001027.getMessage());
            warehouses.add(warehouse);
        }
        //warehouse排序
        Collections.sort(warehouses);   //权重由大到小

        //delete db & redis
        this.saleWeightItemService.deleteSaleWeightItem(itemId,zoneCode,subZoneCode);

        int weight = 100;
        for(int i = 0; i < warehouses.size(); i ++){
            Warehouse warehouse = warehouses.get(i);
            //insert db and redis
            SaleWeightItem saleWeightItem = new SaleWeightItem(null, itemId, zoneCode, warehouse.getSubZoneCode(), warehouse.getSupplyId(),warehouse.getDcReal(), weight, null, null);
            this.saleWeightItemService.addSaleWeightItem(saleWeightItem);
            weight -= 20;

            if(i == 0){
                //update当前售卖仓库
                this.currentDcService.updateCurrentDC(zoneCode,subZoneCode,itemId, warehouse.getDcId());
            }
        }
    }


    /**
     * 权重售卖规则添加
     *
     * @param zoneCode
     * @param itemId
     * @param type
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertSaleWeightItem(String zoneCode, Long itemId, Integer type) {
        logger.info(String.format("zoneCode = %s , itemId = %d, type = %d 开始插入出货规则",zoneCode,itemId,type));

        //先查询规则表中有没有此商品,如果有,则抛业务异常
        Collection<SupplyDc> itemSaleRule = this.saleWeightItemService.getSupplyWeightFromDb(itemId, zoneCode,null);
        if (!CollectionUtils.isEmpty(itemSaleRule)) {
            throw new BusinessException(ExceptionStatus.E2001044.getCode(), ExceptionStatus.E2001044.getMessage());
        }

        List<Warehouse>  warehouses = this.goodsService.getSupply(itemId, zoneCode, type);    //规则
        if (CollectionUtils.isEmpty(warehouses)) {
            throw new BusinessException(ExceptionStatus.E2001047.getCode(), ExceptionStatus.E2001047.getMessage());
        }

        if(type == 2){      // TODO: 17/9/21 临时处理 
            //insert db (所有子区域都需要新增数据)
            for(Warehouse warehouse : warehouses){
                warehouse.setSubZoneCode("SABJ01");
                SaleWeightItem saleWeightItem = new SaleWeightItem(null, itemId, zoneCode, warehouse.getSubZoneCode(), warehouse.getSupplyId(),warehouse.getDcReal(), 100, null, null);
                this.saleWeightItemService.addSaleWeightItem(saleWeightItem);
                //更新当前售卖仓库
                this.currentDcService.updateCurrentDC(zoneCode,warehouse.getSubZoneCode(), itemId, warehouse.getDcId());

                warehouse.setSubZoneCode("SABJ02");
                SaleWeightItem saleWeightItem1 = new SaleWeightItem(null, itemId, zoneCode, warehouse.getSubZoneCode(), warehouse.getSupplyId(),warehouse.getDcReal(), 100, null, null);
                this.saleWeightItemService.addSaleWeightItem(saleWeightItem1);
                //更新当前售卖仓库
                this.currentDcService.updateCurrentDC(zoneCode,warehouse.getSubZoneCode(), itemId, warehouse.getDcId());
            }
        }else{
            //insert db (所有子区域都需要新增数据)
            for(Warehouse warehouse : warehouses){
                SaleWeightItem saleWeightItem = new SaleWeightItem(null, itemId, zoneCode, warehouse.getSubZoneCode(), warehouse.getSupplyId(),warehouse.getDcReal(), 100, null, null);
                this.saleWeightItemService.addSaleWeightItem(saleWeightItem);
                //更新当前售卖仓库
                this.currentDcService.updateCurrentDC(zoneCode,warehouse.getSubZoneCode(), itemId, warehouse.getDcId());
            }
        }

        if(type == 2) {      //冻品新建品是,库存加到最大 临时需求
            for(Warehouse warehouse : warehouses){
                //冻品设置库存
                InventoryLogic inventoryLogic = new InventoryLogic(null,null,itemId,null,null,null,null,null,null,null,null,warehouse.getSupplyId(),warehouse.getDcId(),zoneCode);
                inventoryLogic = this.inventoryLogicService.getInventoryLogic(inventoryLogic);
                if(inventoryLogic == null){
                    Long areaId = Long.parseLong(this.zoneAreaMap.get(warehouse.getZoneCode()));
                    inventoryLogic = new InventoryLogic(null, areaId, itemId, null, 999999999L, 0L, 0L, 0L, 1, null, null, warehouse.getSupplyId(), warehouse.getDcReal(), warehouse.getZoneCode());
                    this.inventoryLogicService.insertInventoryLogic(inventoryLogic);
                }else{
                    inventoryLogic.setInventoryQuantity(999999999L);
                    this.inventoryLogicService.updateInventoryLogic(inventoryLogic);
                }

            }
        }

    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<ItemDetail> queryItemWeightRedis(QueryItemRuleRequest request) {
        List<ItemDetail> itemDetails = new ArrayList<>();
        String zoneCode = request.getZoneCode();
        List<Long> itemIds = request.getItems();

        Long[] itemsArray = new Long[itemIds.size()];
        for (int i = 0; i < itemIds.size(); i++) {
            itemsArray[i] = itemIds.get(i);
        }

        //获取当前售卖仓库
        final Map<Long, Collection<String>> currentDcMap = this.currentDcService.getCurrentDC(zoneCode, itemIds);
        //获取itemId与库存明细map
        Map<Long, List<ItemDc>> itemDcMap = this.inventoryLogicService.getInventoryLogicDetail(itemIds,zoneCode);
        //获取ItemId 和 Supply:dc的集合 (权重排序)
        Map<Long, Set<String>> itemsAndSupply = new HashMap<>();
        for(String subZoneCode : Zone.parse(zoneCode).getSubZoneCode()){
            Map<Long, Collection<String>> subItemSupply = this.saleWeightItemService.getSupplyWeight(itemsArray, zoneCode, subZoneCode);
            for(Map.Entry<Long,Collection<String>> entry : subItemSupply.entrySet()){
                Long itemId  = entry.getKey();
                Set<String> supplys = itemsAndSupply.get(itemId);
                if(supplys == null){
                    itemsAndSupply.put(itemId, (Set<String>) entry.getValue());
                }else{
                    itemsAndSupply.get(itemId).addAll(entry.getValue());
                }
            }
        }

        for(Long itemId : itemIds){
            //库存明细
            List<ItemDc> itemDcList = itemDcMap.get(itemId);
            //查询出货权重
            Collection<String> collection = itemsAndSupply.get(itemId);
            List<String> dcs = new ArrayList<>();
            if(!CollectionUtils.isEmpty(collection)){
                for(String supplyDcStr : collection){
                    SupplyDc supplyDc = new SupplyDc(supplyDcStr);
                    if(!dcs.contains(supplyDc.getDc())){
                        dcs.add(supplyDc.getDc());
                    }
                }
            }

            Salerule salerule = new Salerule();
            salerule.setDc(dcs);
            salerule.setRuleName(com.lsh.base.common.utils.CollectionUtils.convertToString(dcs,","));

            Collection<String> currentDcs = currentDcMap.get(itemId);
            //itemDetail对象
            itemDetails.add(new ItemDetail(itemId, currentDcs, salerule, itemDcList));
        }

        return itemDetails;
    }

    /**
     * 获取SaleRule
     * @param zoneCode
     * @param supplys
     * @return
     */
    public SaleRule getSaleRule(String zoneCode, String supplys) {
        SaleRule saleRule = this.saleRuleJedisDao.selectBySupplys(zoneCode,supplys);
        if(saleRule == null){
            saleRule = this.saleRuleDao.selectBySupplys(zoneCode,supplys);
            //异步添加redis
            if(saleRule != null){
                AsyncEvent.post(saleRule);
            }
        }
        return saleRule;
    }

    public static void main(String[] args){
        List<String> s = new ArrayList<>();
        s.add("DC10");
        s.add("DC31");
        System.out.println(com.lsh.base.common.utils.CollectionUtils.convertToString(s,","));
    }

}
