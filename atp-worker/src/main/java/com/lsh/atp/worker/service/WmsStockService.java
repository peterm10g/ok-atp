package com.lsh.atp.worker.service;

import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.task.AsyncEvent;
import com.lsh.atp.core.task.model.EmailModel;
import com.lsh.atp.core.util.HttpClientUtils;
import com.lsh.base.common.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import scala.Int;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang on 16/11/15.
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.task
 * desc: wms库存同步Service
 */
@Component
public class WmsStockService {

    private static Logger logger = LoggerFactory.getLogger("wms");

    @Resource(name = "zoneCodeMap")
    private Map<String, String> zoneAreaMap;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private WumartStockService wumartStockService;

    @Value("${wms.url.queryStocks}")
    private String url;

    @Value("${wms.exception.isSync}")
    private String isExceptionSync;

    public List<InventoryLogic> getWmsStocks(final Warehouse warehouse) throws Exception {
        String dc = warehouse.getDcId();
        String realDc = warehouse.getDcReal();
        Integer supply = warehouse.getSupplyId();   //货主

        final Long areaId = Long.parseLong(this.zoneAreaMap.get(warehouse.getZoneCode()));

        //获取所有要同步的物美码
        logger.info("开始调用商城接口,查询货主对应的所有物美码, supplyId = " + warehouse.getSupplyMarket());
        long t = System.currentTimeMillis();
        final List<String> codeList = this.goodsService.getCodeBySupply(warehouse.getSupplyMarket());
        logger.info("结束调用商城接口,查询货主对应的所有物美码, supplyId = " + warehouse.getSupplyMarket() + ",物美码条数为" + codeList.size() + ",时间为 : " + (System.currentTimeMillis() - t));


        if (CollectionUtils.isEmpty(codeList)) {
            return Collections.EMPTY_LIST;
        }

        //获取物美码-itemid对应map
        final Map<String, Long> goodsDict = this.wumartStockService.getGoodsDict(dc, warehouse.getZoneCode());
        logger.info("共获取" + goodsDict.size() + "条 物美码-itemId 数据");
        final Map<String, Long> wmsStocks = queryStocksInfo(codeList, warehouse.getDcReal(),/* TODO: 17/8/31,owner先写死为2*/"2", 100);

        if (CollectionUtils.isEmpty(wmsStocks)) {
            logger.info("wms redis 取出数据 为 空 , dc = " + warehouse.getDcId());
        }

        //获取itemid-订单未扣减数量map
        int deadline = (int) TimeUnit.MILLISECONDS.toSeconds(t) - 5 * 60;
        final Map<Long, Long> itemNotDeductedMap = this.wumartStockService.getAllNotDeducted(warehouse.getZoneCode(), warehouse.getDcId(), goodsDict, deadline);
        logger.info("结束查询未扣减");
        final List<InventoryLogic> resultList = new ArrayList<InventoryLogic>(codeList.size());
        logger.info(codeList.toString());
        for (String wumartCode : codeList) {
            Long itemId = goodsDict.get(wumartCode);
            if (itemId == null) {
                logger.info(wumartCode + "此商品没有对应的itemId");
                continue;
            }

            InventoryLogic inventoryLogic = null;
            if (wmsStocks.containsKey(wumartCode)) {
                long quantity = wmsStocks.get(wumartCode).longValue();
                logger.info("itemId : " + itemId+ "supply : " + supply + " , dc : " + dc + " wms库存为 " + quantity);

                //减去未扣减数量
                if (quantity > 0L) {
                    long notDeductedQty = itemNotDeductedMap.get(itemId) == null ? 0 : itemNotDeductedMap.get(itemId);
                    logger.info("itemId : " + itemId+ "supply : " + supply + " , dc : " + dc + " 未扣减数量为 " + notDeductedQty);
                    quantity = quantity - notDeductedQty;
                    quantity = quantity < 0L ? 0L : quantity;
                }

                if(quantity < 0L){
                    quantity = 0L;
                }

                logger.info("itemId : " + itemId + "supply : " + supply +  " , dc : " + dc + " 应同步数量为 " + quantity);
                inventoryLogic = new InventoryLogic(null, areaId, itemId, null, quantity, null, null, null, null, null, null,supply, realDc, warehouse.getZoneCode());
            } else {
                inventoryLogic = new InventoryLogic(null, areaId, itemId, null, 0L, null, null, null, null, null, null,supply, realDc, warehouse.getZoneCode());
            }


            resultList.add(inventoryLogic);
        }

        return resultList;
    }


    /**
     * 调用wms查询库存接口
     *
     * @param goodsNo     (物美码)list
     * @param warehouseNo 仓库
     * @param maxSize     每次查询list最大数量
     * @return 物美码-库存数量map
     */
    private Map<String, Long> queryStocksInfo(List<String> goodsNo, String warehouseNo, String owner, int maxSize) throws Exception {

        Map<String, Long> goodsMap = new HashMap<String, Long>((int) (goodsNo.size() / 0.75));

        if (CollectionUtils.isEmpty(goodsNo)) {
            return Collections.EMPTY_MAP;
        }

        if (goodsNo.size() <= maxSize) {
            return this.queryStocksInfo(goodsNo, warehouseNo, owner);
        }

        int goodssize = goodsNo.size();
        int num = goodssize / maxSize;

        for (int i = 0; i < num; i++) {
            List<String> goods = goodsNo.subList(i * maxSize, (i + 1) * maxSize);
            goodsMap.putAll(this.queryStocksInfo(goods, warehouseNo, owner));
        }
        if (goodssize % maxSize != 0) {
            List<String> goods = goodsNo.subList(num * maxSize, goodssize);
            goodsMap.putAll(this.queryStocksInfo(goods, warehouseNo, owner));
        }

        return goodsMap;
    }


    /**
     * 调用wms查询库存接口
     *
     * @param goodsNo
     * @param dc
     * @param owner
     * @return
     * @throws Exception
     */
    public Map<String, Long> queryStocksInfo(List<String> goodsNo, String dc, String owner) throws Exception {

        if (CollectionUtils.isEmpty(goodsNo)) {
            return Collections.EMPTY_MAP;
        }

        Map<String, Long> goodsMap = new HashMap<String, Long>((int) (goodsNo.size() / 0.75));


        Map params = new HashMap(4);
        params.put("ownerId", owner);
        params.put("warehouseCode", dc);
        params.put("skuCodeList", goodsNo.toArray());

        Map headMap = new HashMap(2);
        headMap.put("Content-Type", "application/json");

        String json = JsonUtils.obj2Json(params);

        logger.info("开始调用wms接口,参数为 : " + json);
        long t = System.currentTimeMillis();
        String result = HttpClientUtils.doPostBody(url, json, headMap);
        logger.info("结束调用wms接口,返回参数为 : " + result + ";时间为 : " + (System.currentTimeMillis() - t));

        try {
            Map map = JsonUtils.json2Obj(result, HashMap.class);

            logger.info(((Map) map.get("head")).get("status").toString());
            if (((Map) map.get("head")).get("status").toString().equals("1")) {      //success

                Map body = (Map) map.get("body");

                for (String wumartCode : goodsNo) {

                    Long qty = new BigDecimal(((Map) body.get(wumartCode)).get("avail_qty").toString()).longValue();
                    if (qty != null) {
                        goodsMap.put(wumartCode, qty);
                    }
                }

            } else {
                throw new Exception("wms查询接口失败, 返回参数为 \n" + result);
            }
        } catch (Throwable e) {
            logger.error("wms error", e);
            StringBuilder sb = new StringBuilder();
            sb.append("wms查询接口失败, 返回参数为 \n").append(result).append("\n").append(e.toString());
            AsyncEvent.post(new EmailModel(sb.toString(),"wms库存同步失败"));

            if("false".equals(isExceptionSync)){
                throw e;
            }
        }


        return goodsMap;
    }
}
