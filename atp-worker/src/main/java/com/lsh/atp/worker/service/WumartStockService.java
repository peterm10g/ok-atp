package com.lsh.atp.worker.service;

import com.lsh.atp.core.dao.area.WarehouseDao;
import com.lsh.atp.core.dao.hold.SkuHoldDao;
import com.lsh.atp.core.dao.hold.SkuHoldQtyDao;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.hold.SkuHold;
import com.lsh.atp.core.model.hold.SkuHoldQty;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.service.inventory.InventorySyncService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by huangdong on 16/7/18.
 */
@Component
public class WumartStockService {

    private Logger logger = LoggerFactory.getLogger(WumartStockService.class);

    private static Logger syncLogger = LoggerFactory.getLogger("sync");

    @Resource(name = "baseJdbcTemplate")
    private NamedParameterJdbcTemplate baseJdbcTemplate;

    @Resource(name = "marketJdbcTemplate")
    private NamedParameterJdbcTemplate marketJdbcTemplate;

    @Resource(name = "ofcJdbcTemplate")
    private NamedParameterJdbcTemplate ofcJdbcTemplate;

    @Resource(name = "omsJdbcTemplate")
    private NamedParameterJdbcTemplate omsJdbcTemplate;

    @Resource(name = "dcZoneMap")
    private Map<String, String> dcZoneMap;

    @Resource(name = "zoneCodeMap")
    private Map<String, String> zoneAreaMap;

    @Autowired
    private WarehouseDao warehouseDao;


    @Autowired
    private InventorySyncService inventorySyncService;

    @Autowired
    private SkuHoldQtyDao skuHoldQtyDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SkuHoldDao skuHoldDao;

    //第一个key是zone,value中的key为itemid
    private Map<String, Map<Long, Integer>> saleUnitMap = null;

    /**
     * 根据dc获取zone_id
     *
     * @param dc
     * @return
     */
    public String getZoneIdByDc(String dc) {
        return dcZoneMap.get(dc);
    }

    public Warehouse getWarehosue(String dcId) {
        return this.warehouseDao.getWarehoseByDc(dcId);
    }


    /**
     * 获取售罄SKU ID
     *
     * @return
     * @throws Exception
     */
    public List<Long> getSkuIdsOfSellOut(String zoneId) throws Exception {
        Set<String> codes = this.getGoodsCodesOfSellOut(zoneId);
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        Map<String, Long> map = this.getGoodsDict(codes, Collections.EMPTY_SET, this.inventorySyncService.getMarketByZoneCode(zoneId));
        if (CollectionUtils.isEmpty(map)) {
            return Collections.emptyList();
        }
        return new ArrayList<Long>(map.values());
    }

    /**
     * 获取saleunit
     *
     * @param itemId itemId
     * @return saleunit
     **/
    public Integer getSaleUnit(Long itemId, String zoneId) {
        Map<Long, Integer> saleUnitMap = this.saleUnitMap.get(zoneId);
        if (saleUnitMap == null) {
            return 0;
        }
        return saleUnitMap.get(itemId);
    }

    /**
     * 获取商品物美库存
     *
     * @param warehouse
     * @return
     * @throws Exception
     */
    public List<InventoryLogic> getWumartStock(final Warehouse warehouse) throws Exception {
        final Integer supplyId = warehouse.getSupplyId();
        final String dc = warehouse.getDcReal();
        final String zoneId = warehouse.getZoneCode();
        final Long areaId = Long.parseLong(this.zoneAreaMap.get(zoneId));
        final String marketId = this.inventorySyncService.getMarketByZoneCode(zoneId);

        /* ------------- 查询货主对应的所有物美码--------------------*/
        syncLogger.info("开始调用商城接口,查询货主对应的所有物美码, supplyId = " + warehouse.getSupplyMarket());
        final List<String> codeList = this.goodsService.getCodeBySupply(warehouse.getSupplyMarket());
        syncLogger.info("结束调用商城接口,查询货主对应的所有物美码, supplyId = " + warehouse.getSupplyMarket() + ",物美码条数为" + codeList.size());

        if(CollectionUtils.isEmpty(codeList)){
            return Collections.emptyList();
        }

        /* ------------- 查询saleunit --------------------*/
        long t = System.currentTimeMillis();
        saleUnitMap = goodsService.getMaxSaleUnitByTime(null);
        syncLogger.info("dc = " + dc + " 调用商品服务 同步热销品 链商码-saleunit对应关系 , 时间为 : " + (System.currentTimeMillis() - t));

        /* ------------- 查询物美码-链商码对照表--------------------*/
        final Map<String, Long> goodsDict = this.getGoodsDict(dc, zoneId);
        syncLogger.info("共获取物美-链商码对照数量" + goodsDict.size());
        if (CollectionUtils.isEmpty(goodsDict)) {
            return Collections.emptyList();
        }

        //查询deadline
        String zDateTime;
        final Map<String, Object> params = new HashMap<String, Object>(8);
        params.put("dc", dc);
        params.put("codes", codeList);
        params.put("marketId", marketId);
        try {
            String sql = "select zdate, ztime from item_delivery where market_id = :marketId and werks = :dc and sku_id in (:codes) order by zdate desc, ztime desc limit 1";
            zDateTime = this.baseJdbcTemplate.queryForObject(sql, params, new RowMapper<String>() {

                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    String zdate = rs.getString("zdate");
                    String ztime = rs.getString("ztime");
                    return zdate + ztime;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }

        /* ------------- 查询应扣减数量--------------------*/
        int deadline = (int) TimeUnit.MILLISECONDS.toSeconds(new SimpleDateFormat("yyyyMMddHHmmss").parse(zDateTime).getTime()) - 5 * 60;
        //链商码-未扣减数量 map
        t = System.currentTimeMillis();
        final Map<Long, Long> itemNotDeductedMap = this.getAllNotDeducted(zoneId, dc, goodsDict, deadline);
        syncLogger.info("dc = " + dc + " 获取未扣减数量 map 用时 : " + (System.currentTimeMillis() - t));

        //查询物美同步库存表,得到WumartStock对象list
        String sql = "select sku_id, lbkum, zdate, ztime, werks from item_delivery where market_id = :marketId and werks = :dc and sku_id in (:codes)";

        List<InventoryLogic> wumartStockList = this.baseJdbcTemplate.query(sql, params, new RowMapper<InventoryLogic>() {
            public InventoryLogic mapRow(ResultSet rs, int i) throws SQLException {
                //物美码转链商码
                String wumartCode = rs.getString("sku_id");
                codeList.remove(wumartCode);
                final Long itemId = goodsDict.remove(wumartCode);

                if (itemId == null) {
                    return null;
                }

                StringBuilder logSb = new StringBuilder();
                logSb.append("itemId : ").append(itemId)
                        .append(" , supply : ").append(supplyId)
                        .append(" , dc : ").append(dc).append(" ");
                String itemMsg = logSb.toString();

                BigDecimal quantity = rs.getBigDecimal("lbkum");
                syncLogger.info(itemMsg + " 物美库存为 " + quantity.toString());
                //减去预留数量
                if (quantity.compareTo(BigDecimal.ZERO) == 1) {

                    Integer saleUnit = WumartStockService.this.getSaleUnit(itemId, zoneId);
                    if (saleUnit == null) {
                        saleUnit = 0;
                    }
                    syncLogger.info(itemMsg + " saleUnit为 " + saleUnit);
                    int reservedQuality = WumartStockService.this.inventorySyncService.getQualityOfReserved(saleUnit,dc);
                    syncLogger.info(itemMsg + " 预留的数量为 " + reservedQuality);
                    quantity = quantity.subtract(new BigDecimal(reservedQuality));
                    if (quantity.compareTo(BigDecimal.ZERO) != 1) {
                        quantity = BigDecimal.ZERO;
                    }
                }

                //减去未扣减数量
                if (quantity.compareTo(BigDecimal.ZERO) == 1) {
                    long notDeductedQty = itemNotDeductedMap.get(itemId) == null ? 0 : itemNotDeductedMap.get(itemId);
                    syncLogger.info(itemMsg + " 未扣减数量为 " + notDeductedQty);
                    quantity = quantity.subtract(new BigDecimal(notDeductedQty));
                    if (quantity.compareTo(BigDecimal.ZERO) != 1) {
                        quantity = BigDecimal.ZERO;
                    }
                }

                syncLogger.info(itemMsg + " 应同步数量为 " + quantity.toString());
                InventoryLogic inventoryLogic = new InventoryLogic(null, areaId, itemId, null, quantity.longValue(), null, null, null, null, null, null, supplyId, dc, zoneId);

                return inventoryLogic;
            }
        });

        //如果没有,则将库存记为0(缺品情况)
        for (String wumartCode : codeList) {
            if (StringUtils.isBlank(wumartCode)) {
                continue;
            }

            final Long itemId = goodsDict.remove(wumartCode);
            if (itemId == null) {
                continue;
            }

            StringBuilder logSb = new StringBuilder();
            logSb.append("itemId : ").append(itemId)
                    .append(" , supply : ").append(supplyId)
                    .append(" , dc : ").append(dc).append(" ");

            syncLogger.info(logSb.toString() + "base表中无数据,库存置0, ");
            wumartStockList.add(new InventoryLogic(null, areaId, itemId, null, 0L, null, null, null, null, null, null, supplyId, dc, zoneId));

        }

        //删掉null
        List<InventoryLogic> nullList = new ArrayList<InventoryLogic>(1);
        nullList.add(null);
        wumartStockList.removeAll(nullList);

        return wumartStockList;
    }

    /**
     * 获取商品物美-链商对照表
     *
     * @param includes includes
     * @param excludes excludes
     * @param market   market
     * @return 物美-链商对照表map
     * @throws Exception
     */
    public Map<String, Long> getGoodsDict(Set<String> includes, Set<String> excludes, String market) throws Exception {
        long t = System.currentTimeMillis();
        Map<String, Map<String, Long>> wumartCode2ItemIdMap = goodsService.wumartCode2lshCode(null);
        logger.info("market = " + market + " 同步物美码-链商码对应关系 , 时间为 : " + (System.currentTimeMillis() - t) + ",数量为 : " + wumartCode2ItemIdMap.size());


        //获取该market下的所有对应关系
        final Map<String, Long> marketMap = wumartCode2ItemIdMap.get(market);

        if (CollectionUtils.isEmpty(marketMap)) {
            return Collections.emptyMap();
        }

        Map<String, Long> result = new HashMap<String, Long>();

        //处理includes
        if (!CollectionUtils.isEmpty(includes)) {         //如果includes不为空,则只取includes中的商品
            for (String includeCode : includes) {
                final Long itemId = marketMap.get(includeCode);
                if (itemId == null) {
                    logger.error("market = " + market + " 物美码与链商码对应关系错误 , 物美码 = " + includeCode);
                } else {
                    result.put(includeCode, marketMap.get(includeCode));
                }
            }
        } else {                                          //如果includes为空,则全取
            result.putAll(marketMap);
        }

        //处理excludes
        if (!CollectionUtils.isEmpty(excludes)) {
            for (String excludeCode : excludes) {
                result.remove(excludeCode);
            }
        }

        return result;
    }


    /**
     * 根据不同仓库号 获取商品物美-链商对照表
     * <p>
     * <b>DC10</b> 只获取非寄售商品
     * <p>
     * <b>DC31</b> 只获取寄售商品
     * <p>
     * <b>DC09</b> 全部获取
     * <p>
     * 以上都需排除售罄商品
     *
     * @param dc     仓库号
     * @param zoneId 仓库号对应的区域
     * @return 商品物美-链商对照表Map
     * @throws Exception
     */
    public Map<String, Long> getGoodsDict(String dc, String zoneId) throws Exception {
        String market = this.inventorySyncService.getMarketByZoneCode(zoneId);
        //需要新加参数,根据dc获取对应的售罄商品
        Set<String> excludes = this.getGoodsCodesOfSellOut(zoneId);           //售罄商品物美码集合

        return this.getGoodsDict(null, excludes, market);


//        if (this.inventorySyncService.isConsignment(dc)) {                    //如果为寄售仓库,只取寄售商品
//            Set<String> includes = this.getGoodsCodesOfConsignment(zoneId);
//            if (CollectionUtils.isEmpty(includes)) {
//                return Collections.emptyMap();
//            }
//            return this.getGoodsDict(includes, excludes, market);        //includes为寄售,excludes为售罄
//        } else {                    //如果为非寄售仓库,则先都取。在执行时根据配置文件进一步处理
//            //逻辑不需变,例如DC09全部获取,取出的寄售商品为空。
//
////            if("DC10".equals(dc)){
////                excludes.addAll(this.getGoodsCodesOfConsignment(zoneId));
////            }
//            //20160819修改   DC10也不排除寄售。
//            return this.getGoodsDict(null, excludes, market);           //excludes为售罄和寄售的商品总和
//        }
    }

    /**
     * 获取售罄商品物美码集合
     *
     * @param zoneId 区域id
     * @return 售罄物美码Set
     * @throws Exception
     */
    private Set<String> getGoodsCodesOfSellOut(String zoneId) throws Exception {
        return this.getWuMartCodesByType(2, zoneId);
    }

    /**
     * 查询售罄 寄售 物美商品码
     *
     * @param type   类型 : 1为寄售,2为售罄
     * @param zoneId 区域id
     * @return 该类型的物美码Set
     */
    private Set<String> getWuMartCodesByType(int type, String zoneId) {

        String sql = "SELECT icc.`code` FROM item_consignment_code icc WHERE icc.type = :type and zone_id = :zoneId and valid = 1";
        Map<String, Object> params = new HashMap<String, Object>(4);
        params.put("type", type);
        params.put("zoneId", zoneId);
        Set<String> set = new HashSet<String>();

        List<String> wuMartList = this.marketJdbcTemplate.queryForList(sql, params, String.class);
        if (!CollectionUtils.isEmpty(wuMartList)) {
            set.addAll(wuMartList);
        }
        return set;

    }

    /**
     * 返回所有itemid 与 未扣减数量Map(oms && ofc)
     *
     * @param zoneCode  区域码
     * @param dc        仓库
     * @param deadline  时间
     * @param goodsDict 物美吗-链商码 map
     * @return map
     */
    public Map<Long, Long> getAllNotDeducted(final String zoneCode, final String dc, final Map<String, Long> goodsDict, final int deadline) throws Exception {
        //oms
        Map<String, Object> params;
        String sql = "select order_code from order_head where order_status in (12,20,21) and region_code = :zoneId";
        params = new HashMap<String, Object>(4);
        params.put("zoneId", zoneCode);
        List<String> orderIds = omsJdbcTemplate.queryForList(sql, params, String.class);


        Set<String> orderIdSet = new HashSet<String>(orderIds);
        //ofc
        sql = "select order_id from ofc_bill where bill_type='ORDER' and system = :system and created_at > :deadline";
        params = new HashMap<String, Object>(4);
        params.put("system", (this.inventorySyncService.isConsignment(dc) ? "WUMART_SAP_JISHOU" : "WUMART_SAP"));
        params.put("deadline", deadline);

        List<String> ofcOrderIds = ofcJdbcTemplate.queryForList(sql, params, String.class);
        orderIdSet.addAll(ofcOrderIds);

        if (CollectionUtils.isEmpty(orderIdSet)) {
            return Collections.emptyMap();
        }

//        List<SkuHoldQty> itemNotDeductedList = this.skuHoldQtyDao.getTotalHoldQty(orderIdSet, new ArrayList<Long>(goodsDict.values()), dc);
        List<Long> holdIdList = this.skuHoldDao.getSkuHoldIdBySequenceId(orderIdSet);
        if (CollectionUtils.isEmpty(holdIdList)) {
            return Collections.emptyMap();
        }

        Map<Long, Long> itemNotDeductedMap = new HashMap<Long, Long>();

        List<SkuHoldQty> itemNotDeductedList = this.skuHoldQtyDao.getTotalHoldQtyBySkuIdAndDc(holdIdList, dc);
        for (SkuHoldQty skuHoldQty : itemNotDeductedList) {
            itemNotDeductedMap.put(skuHoldQty.getSkuId(), skuHoldQty.getHoldQty());
        }

        return itemNotDeductedMap;
    }

    /**
     * 返回所有itemid 与 未扣减数量Map(oms)
     *
     * @param zoneCode
     * @param dc
     * @param itemList
     * @return
     * @throws Exception
     */
    public Map<Long, Long> getAllNotDeducted(final String zoneCode, final String dc, final List<Long> itemList) throws Exception {
        //oms
        Map<String, Object> params;
        String sql = "select order_code from order_head where order_status in (12,20,21) and region_code = :zoneId";
        params = new HashMap<String, Object>(4);
        params.put("zoneId", zoneCode);
        List<String> orderIds = omsJdbcTemplate.queryForList(sql, params, String.class);


        if (CollectionUtils.isEmpty(orderIds)) {
            return Collections.emptyMap();
        }

        List<SkuHoldQty> itemNotDeductedList = this.skuHoldQtyDao.getTotalHoldQty(orderIds, itemList, dc);
        //将返回的list转为map
        Map<Long, Long> itemNotDeductedMap = new HashMap<Long, Long>();
        for (SkuHoldQty skuHoldQty : itemNotDeductedList) {
            itemNotDeductedMap.put(skuHoldQty.getSkuId(), skuHoldQty.getHoldQty());
        }

        return itemNotDeductedMap;
    }


}
