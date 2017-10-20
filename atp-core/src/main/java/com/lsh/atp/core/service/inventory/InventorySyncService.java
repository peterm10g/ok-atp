package com.lsh.atp.core.service.inventory;

import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.core.constant.BusiConstant;
import com.lsh.atp.core.dao.hold.SkuHoldQtyDao;
import com.lsh.atp.core.dao.inventory.InventoryLogicDao;
import com.lsh.atp.core.dao.inventory.InventoryPhysicalDao;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.buffer.SkuBufferConf;
import com.lsh.atp.core.model.buffer.SkuBufferConfs;
import com.lsh.atp.core.model.inventory.BulkWumarkStock;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.model.inventory.InventoryPhysical;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.*;

/**
 * Project Name: lsh-atp
 * Created by huangdong on 16/7/6.
 * 北京链商电子商务有限公司
 * Package
 * desc:
 */
@Service
public class InventorySyncService {

    private static Logger logger = LoggerFactory.getLogger(InventorySyncService.class);

    @Autowired
    private InventoryPhysicalDao inventoryPhysicalDao;

    @Autowired
    private InventoryLogicDao inventoryLogicDao;

    @Resource(name = "consignmentDCList")
    private List<String> consignmentDCList;

    @Resource(name = "omsJdbcTemplate")
    private NamedParameterJdbcTemplate omsJdbcTemplate;


    @Resource(name = "marketZoneCodeMap")
    protected Map<String, String> marketZoneCodeMap;

    @Resource(name = "zoneCodeMap")
    protected Map<String, String> zoneCodeMap;

    @Autowired
    private SkuHoldQtyDao skuHoldQtyDao;

    /**
     * 更新物理库存
     *
     * @param dc          仓库号
     * @param skuId       物料号
     * @param skuQuantity 库存
     * @param timestamp   时间戳
     * @return boolean
     */
    private boolean updateInventoryPhysical(String dc, Long skuId, long skuQuantity, int timestamp) {
        InventoryPhysical param = new InventoryPhysical();
        param.setWarehouseCode(dc);
        param.setSkuId(skuId);
        InventoryPhysical entity = this.inventoryPhysicalDao.findOne(param);
        if (entity == null) { // 数据库中无库存记录
            //如果同步库存小于0,以前是不操作。现在需要改为insert
            if (skuQuantity <= 0) {
                //return false;
                skuQuantity = 0L;
            }
            //新增物理库存信息
            InventoryPhysical info = new InventoryPhysical();
            info.setWarehouseCode(dc);
            info.setSkuId(skuId);
            info.setSkuQuantity(skuQuantity);
            info.setCreatedAt(timestamp);
            info.setUpdatedAt(timestamp);
            this.inventoryPhysicalDao.insert(info);
            return true;
        } else {
            long quantity = entity.getSkuQuantity();
            if (skuQuantity <= 0) {
                skuQuantity = 0;
            }
            //如果同步库存等于数据库中库存数,不做任何操作。
            if (quantity == skuQuantity) {
                return false;
            }
            //更新物理库存
            InventoryPhysical info = new InventoryPhysical();
            info.setId(entity.getId());
            info.setSkuQuantity(skuQuantity);
            info.setUpdatedAt(timestamp);
            this.inventoryPhysicalDao.update(info);
            return true;
        }
    }

    /**
     * 更新逻辑库存
     * @param inventoryLogic
     * @return
     */
    public boolean updateInventoryLogic(final InventoryLogic inventoryLogic){
        int num ;
        InventoryLogic entity = this.inventoryLogicDao.getInventoryLogicByModel(inventoryLogic);
        if(entity == null){
            entity = new InventoryLogic(null,inventoryLogic.getAreaId(),inventoryLogic.getSkuId(),null,inventoryLogic.getInventoryQuantity(),0L,0L,0L,1,null,null,inventoryLogic.getSupplyId(),inventoryLogic.getDC(),inventoryLogic.getZoneCode());
            num = this.inventoryLogicDao.insert(entity);
        }else{
            entity.setInventoryQuantity(inventoryLogic.getInventoryQuantity());
            num = this.inventoryLogicDao.updateInventoryLogic(entity);
        }

        boolean isSuccess = num == 1 ? true : false;
        return isSuccess;
    }


    /**
     * 获取预留数量
     *
     * @param saleUnit saleUnit
     * @return 预留数量
     */
    public int getQualityOfReserved(Integer saleUnit, String dc) {

        if(!"DC10".equals(dc)){
            return 0;
        }

        //logger.info()
        if (saleUnit == null || saleUnit <= 0) {
            return 0;
        }

        SkuBufferConfs skuBufferConfs = null;

        try {
//            skuBufferConfs = this.redisBufferConfService.get(RedisKeyConstant.SKU_BUFFER_CONF);
            skuBufferConfs = null;  // TODO: 17/5/15 springredis to jedis
        } catch (Exception e) {
            logger.error("从redis中取得buffer配置失败", e);
        }


        int reservedQuality = 0;     //预留数量

        if (skuBufferConfs == null) {
            return reservedQuality;
        }

        List<SkuBufferConf> bufferConfList = skuBufferConfs.getBuffers();

        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

        for (SkuBufferConf bufferConf : bufferConfList) {
            int start = bufferConf.getStart();      //开始时间
            int end = bufferConf.getEnd();          //截止时间
            int times = bufferConf.getTimes();      //预留倍数
            if (hour >= start && hour < end) {
                reservedQuality += saleUnit * times;
            }
        }

        return reservedQuality;
    }

    public static Logger getLogger() {
        return logger;
    }







    /**
     * 获取订单未扣减数量
     *
     * @param itemId   itemId
     * @param dc      dc
     * @param deadline 时间戳(单位为s)
     * @param zoneCode 区域码
     * @return 订单未扣减数量
     **/
    public int getQualityOfNotDeducted(final Long itemId, final Long deadline, final String zoneCode, String dc, String filter) {
        int result = 0;

        String sql = "select order_code from order_head where order_status in (12,20,21) and region_code = :zoneCode";
        Map<String,Object> params = new HashMap<>(4);

        params.put("zoneCode", zoneCode);
        List<String> orderIds = omsJdbcTemplate.queryForList(sql, params, String.class);

//        Set<String> orderIdSet = new HashSet<>(orderIds);
        //ofc
//        sql = "select order_id from ofc_bill where bill_type='ORDER' and system = :system and created_at > :deadline";
//        params = new HashMap<>(4);
//        params.put("system", (this.isConsignment(dc) ? "WUMART_SAP_JISHOU" : "WUMART_SAP"));
//        params.put("deadline", deadline);
//        List<String> ofcOrderIds = ofcJdbcTemplate.queryForList(sql, params, String.class);
//        orderIdSet.addAll(ofcOrderIds);

        if(!StringUtils.isBlank(filter)){
            orderIds.remove(filter);
        }

        if (orderIds.size() == 0) {
            return 0;
        }

        result += this.skuHoldQtyDao.getTotalHoldQtySingle(orderIds, itemId, dc).intValue();

        return result;
    }

    /**
     * 判断该仓库是否是寄售仓库
     *
     * @param dc 仓库号
     * @return 如果是寄售仓库, 则返回
     **/
    public boolean isConsignment(String dc) {
        return this.consignmentDCList.contains(dc);
    }

    /**
     * 根据zoneCode获取对应的market
     *
     * @param zoneCode 区域码
     * @return market
     */
    public String getMarketByZoneCode(String zoneCode) {
        return this.marketZoneCodeMap.get(zoneCode);
    }

}
