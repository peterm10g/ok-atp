package com.lsh.atp.worker.service;

import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.inventory.InventoryLogic;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.service.email.SendEmailService;
import com.lsh.atp.core.service.goodsapi.GoodsService;
import com.lsh.atp.core.util.HttpClientUtils;
import com.lsh.base.common.json.JsonUtils;
import com.lsh.base.common.json.JsonUtils2;
import com.lsh.base.common.utils.CollectionUtils;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import scala.Int;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/11/01
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.worker.service
 * desc:黑狗库存service类
 */
@Component
public class HigoStockService {
    private static final Logger logger = LoggerFactory.getLogger("higo");

    private static final int MAX_SIZE = 50;     //每次查询黑狗接口最大数量

    @Value("${higo.url.queryStocks}")
    private String url;

    @Value("${higo.userKey}")
    private String higoUserKey;

    @Value("${higo.userValue}")
    private String higoUserValue;

    @Resource(name = "zoneCodeMap")
    private Map<String,String> zoneAreaMap;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private GoodsService goodsService;


    @Autowired
    private WumartStockService wumartStockService;




    public List<InventoryLogic> getHigoStock(final String dc) throws Exception {

        //根据仓库获得WareHouse对象
        final Warehouse warehouse = this.warehouseService.getWarehouseByDc(dc);
        Integer marketSupply = warehouse.getSupplyMarket();

        if(warehouse == null){
            return Collections.EMPTY_LIST;
        }
        final Long areaId =Long.parseLong(this.zoneAreaMap.get(warehouse.getZoneCode()));
        //获取所有要同步的物美码
        logger.info("开始调用商城接口,查询货主对应的所有物美码, supplyId = " + marketSupply);
        final List<String> codeList = this.goodsService.getCodeBySupply(marketSupply);
        logger.info("结束调用商城接口,查询货主对应的所有物美码, supplyId = " + marketSupply + ",物美码条数为" + codeList.size());

        //获取物美码-itemid对应map
        final Map<String, Long> goodsDict = this.wumartStockService.getGoodsDict(dc, warehouse.getZoneCode());
        logger.info("共获取" + goodsDict.size() + "条 物美码-itemId 数据");
        //获取itemid-订单未扣减数量map
        final Map<Long,Long> itemNotDeductedMap = this.wumartStockService.getAllNotDeducted(warehouse.getZoneCode(),dc,new ArrayList<Long>(goodsDict.values()));
        final Map<String, BigDecimal> higoStockInfo = this.queryStocksInfo(codeList,dc,MAX_SIZE);

        List<InventoryLogic> resultList = new ArrayList<InventoryLogic>(higoStockInfo.size());

        for(String wumartCode : codeList){
            final Long itemId = goodsDict.remove(wumartCode);
            if(itemId == null){
                logger.info(wumartCode + "此冻品没有对应的itemId");
                continue;
            }

            if(!higoStockInfo.containsKey(wumartCode) || higoStockInfo.get(wumartCode) == null){
                resultList.add(new InventoryLogic(null,areaId,itemId,null,0L,null,null,null,null,null,null,warehouse.getSupplyId(),dc,warehouse.getZoneCode()));
                continue;
            }

            long quantity = higoStockInfo.get(wumartCode).intValue();
            logger.info("itemId : " + itemId + " , dc : " + dc + " 黑狗库存为 " + quantity);

            //减去未扣减数量
            if (quantity > 0L) {
                long notDeductedQty = itemNotDeductedMap.get(itemId) == null ? 0 : itemNotDeductedMap.get(itemId);
                logger.info("itemId : " + itemId + " , dc : " + dc + " 未扣减数量为 " + notDeductedQty);
                quantity = quantity - notDeductedQty;
            }

            if (quantity < 0L) {
                quantity = 0L;
            }

            logger.info("itemId : " + itemId + " , dc : " + dc + " 应同步数量为 " + quantity);
            InventoryLogic inventoryLogic = new InventoryLogic(null,areaId,itemId,null,quantity,null,null,null,null,null,null,warehouse.getSupplyId(),dc,warehouse.getZoneCode());
            resultList.add(inventoryLogic);
        }

        return resultList;
    }

    /**
     * 调用黑狗查询库存接口
     *
     * @param goodsNo     (物美码)list
     * @param warehouseNo 仓库
     * @param maxSize     每次查询list最大数量
     * @return 物美码-库存数量map
     */
    private Map<String, BigDecimal> queryStocksInfo(List<String> goodsNo, String warehouseNo, int maxSize) throws Exception {

        Map<String, BigDecimal> goodsMap = new HashMap<String, BigDecimal>((int) (goodsNo.size() / 0.75));

        if (CollectionUtils.isEmpty(goodsNo)) {
            return Collections.EMPTY_MAP;
        }

        if (goodsNo.size() <= maxSize) {
            return this.queryStocksInfo(goodsNo, warehouseNo);
        }

        int goodssize = goodsNo.size();
        int num = goodssize / maxSize;

        for (int i = 0; i < num; i++) {
            List<String> goods = goodsNo.subList(i * maxSize, (i + 1) * maxSize);
            goodsMap.putAll(this.queryStocksInfo(goods, warehouseNo));
        }
        if (goodssize % maxSize != 0) {
            List<String> goods = goodsNo.subList(num * maxSize, goodssize);
            goodsMap.putAll(this.queryStocksInfo(goods, warehouseNo));
        }

        return goodsMap;
    }

    /**
     * 调用黑狗查询库存接口
     *
     * @param goodsNo     (物美码)list
     * @param warehouseNo 仓库
     * @return 物美码-库存数量map
     */
    private Map<String, BigDecimal> queryStocksInfo(List<String> goodsNo, String warehouseNo) throws Exception {

        if (CollectionUtils.isEmpty(goodsNo)) {
            return Collections.EMPTY_MAP;
        }

        Map<String, BigDecimal> goodsMap = new HashMap<String, BigDecimal>((int) (goodsNo.size() / 0.75));

        String goodsno = CollectionUtils.convertToString(goodsNo, ",");

        Map mapgw = new HashMap(4);
        mapgw.put("goodsno", goodsno);
        // TODO: 16/11/17 对应关系
        mapgw.put("warehouseno", "A01");

        Map params = new HashMap(8);
        params.put("json", JsonUtils2.obj2Json(mapgw));
        params.put("userKey", this.higoUserKey);
        params.put("userValue", this.higoUserValue);

        logger.info("结束调用黑狗接口,参数为 : " + JsonUtils.obj2Json(params));
        long t = System.currentTimeMillis();
        String result = HttpClientUtils.doPost(url, params);
        logger.info("结束调用黑狗接口,返回参数为 : " + result + ";时间为 : " + (System.currentTimeMillis() - t));

        Map map = JsonUtils2.json2Obj(result, HashMap.class);

        if (map.get("code").equals("success")) {

            List<Map<String, Object>> mapListJson = (List) map.get("data");

            for (Map cell : mapListJson) {
                BigDecimal qit = new BigDecimal(cell.get("qit").toString());
                BigDecimal orderQit = new BigDecimal(cell.get("orderQit").toString());

                String cellgoods = (String) cell.get("goodsno");
                logger.info("商品号:" + cellgoods + " 可支配库存:" + qit.toString());
                logger.info("商品号:" + cellgoods + " 应出未出数量:" + orderQit.toString());
                BigDecimal qty = qit.subtract(orderQit);
                goodsMap.put(cellgoods, qty);
            }

        }else{
            throw new Exception("黑狗查询接口失败, 返回参数为 \n" + result);
        }
        return goodsMap;

    }

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>(10);
        list.add("111111111");
        list.add("222222222");
        list.add("333333333");
        Map mapgw = new HashMap();
        mapgw.put("goodsno", list);
        mapgw.put("warehouseno", "DC10");
        System.out.println(JsonUtils2.obj2Json(mapgw));
    }

}
