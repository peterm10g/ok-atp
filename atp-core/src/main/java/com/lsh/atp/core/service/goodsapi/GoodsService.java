package com.lsh.atp.core.service.goodsapi;

import com.alibaba.fastjson.JSON;
import com.lsh.atp.api.model.baseVo.ExceptionStatus;
import com.lsh.atp.core.constant.BusiConstant;
import com.lsh.atp.core.exception.BusinessException;
import com.lsh.atp.core.model.area.Warehouse;
import com.lsh.atp.core.model.supply.*;
import com.lsh.atp.core.service.area.WarehouseService;
import com.lsh.atp.core.util.AtpAssert;
import com.lsh.atp.core.util.HttpClientUtils;
import com.lsh.base.common.exception.BizCheckedException;
import com.lsh.base.common.json.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.util.*;

/**
 * Project Name: lsh-atp
 * Created by zhangqiang
 * Date: 16/10/27
 * 北京链商电子商务有限公司
 * Package com.lsh.atp.core.service.goodsapi
 * desc:调用商品服务相关接口Service类
 */
@Component
public class GoodsService {

    private final static Logger logger = LoggerFactory.getLogger(GoodsService.class);


    @Value("${shop.api.host}")
    private String goodServiceHost;

    @Autowired
    private WarehouseService warehouseService;

    /**
     * 根据supplyid 获取 该货主下所有商品
     * @param supplyId
     * @return
     */
    public List<String> getCodeBySupply(Integer supplyId){

        Map params = new HashMap(2);
        params.put("supplier_id",supplyId);
        logger.info("开始调用商城 supplierid2code 接口");
        long currentTime = System.currentTimeMillis();
        String resStr = this.doPost(goodServiceHost + "/goods/supply/supplierid2code",params);
        logger.info("结束调用商城 supplierid2code 接口,时间为 : " + (System.currentTimeMillis() - currentTime));

        Map<String,Object> resMap = JsonUtils.json2Obj(resStr,Map.class);

        if(CollectionUtils.isEmpty(resMap) || Integer.parseInt(String.valueOf(resMap.get("ret"))) != 0){
            return Collections.EMPTY_LIST;
        }

        Map<String,Object> contentMap = (Map<String, Object>) resMap.get("content");
        if(CollectionUtils.isEmpty(contentMap)){
            return Collections.EMPTY_LIST;
        }

        List codeList = (List) contentMap.get("code_list");
        if(CollectionUtils.isEmpty(codeList)){
            return Collections.EMPTY_LIST;
        }

        List<String> resultList = new ArrayList<>(codeList.size());
        for(Object obj : codeList){
            Map map = (Map)obj;
            resultList.add((String)map.get("code"));
        }

        return resultList;
    }

    /**
     * 物美码转换itemID
     *
     * @param wumartCode 物美码
     * @param market market
     * @return itemId
     */
    public Long code2ItemId(String wumartCode, String market){
        //构造参数map
        StringBuilder url = new StringBuilder();
        url.append(goodServiceHost)
                .append("/goods/supply/code2itemid")
                .append("?supply_ids=[%7B%22supply_id%22:%22")
                .append(wumartCode)
                .append("%22%7D]&market_id=")
                .append(market);

        logger.info("开始调用商城 code2itemid 接口,物美码 : " + wumartCode + " , market = " + market);
        long currentTime = System.currentTimeMillis();
        String resStr = this.doPost(url.toString(), Collections.EMPTY_MAP);
        logger.info("结束调用商城 code2itemid 接口,时间为 : " + (System.currentTimeMillis() - currentTime) + "返回为 : " + resStr);

        Map<String,Object> resMap = JsonUtils.json2Obj(resStr,Map.class);

        if(CollectionUtils.isEmpty(resMap) || Integer.parseInt(String.valueOf(resMap.get("ret"))) != 0){
            return null;
        }

        Map<String,Object> contentMap = (Map<String, Object>) resMap.get("content");
        if(CollectionUtils.isEmpty(contentMap)){
            return null;
        }

        Object obj = contentMap.get(wumartCode);

        Object itemObj ;
        if(obj instanceof ArrayList){
            List list = (ArrayList)obj;
            if(CollectionUtils.isEmpty(list)){
                return null;
            }
            itemObj = ((Map)list.get(0)).get("item_id");
            return Long.parseLong(((Map)list.get(0)).get("item_id").toString());
        }else{
            Map map = (LinkedHashMap)obj;
            if(CollectionUtils.isEmpty(map)){
                return null;
            }
            itemObj = map.get("item_id");
        }

        if(itemObj == null){
            return null;
        }
        return Long.parseLong(itemObj.toString());
    }

    /**
     * 调用商品服务接口,获取该item对应的saleunit
     * @param itemId itemId
     * @return
     */
    public int getSaleUnit(Long itemId){
        StringBuilder url = new StringBuilder();
        url.append(goodServiceHost)
                .append("/goods/item/getskulist")
                .append("?item_ids=[%7B%22item_id%22:%22")
                .append(itemId.toString())
                .append("%22%7D]");

        logger.info("开始调用商城 getskulist 接口,参数为 : " + itemId);
        long currentTime = System.currentTimeMillis();
        String resStr = this.doPost(url.toString(), Collections.EMPTY_MAP);
        logger.info("结束调用商城 getskulist 接口,时间为 : " + (System.currentTimeMillis() - currentTime) + "返回为 : " + resStr);

        Map<String,Object> resMap = JsonUtils.json2Obj(resStr,Map.class);

        if(CollectionUtils.isEmpty(resMap) || Integer.parseInt(String.valueOf(resMap.get("ret"))) != 0){
            logger.error("调用商品服务失败");
            return 0;
        }

        Map<String,Object> contentMap = (Map<String, Object>) resMap.get("content");
        if(CollectionUtils.isEmpty(contentMap)){
            logger.error("调用商品服务失败");
            return 0;
        }
        List<Map<String,Object>> skuList = (List<Map<String, Object>>) contentMap.get("sku_list");

        //取最大的saleUnit
        int result = 0;
        for(Map<String,Object> sku : skuList){
            String level = (String) sku.get("level");
            if(level.equals("1") || level.equals("2")){
                int saleUnit = Integer.parseInt((String)sku.get("sale_unit"));
                result = saleUnit > result ? saleUnit : result;
            }
        }

        return result;
    }

    /**
     * 调用商城接口,判断该商品应设置的权重
     * @param itemId
     * @param zoneCode
     * @param type
     * @return
     */
    public List<Warehouse> getSupply(Long itemId, String zoneCode, Integer type) {
        List<Warehouse> result = new ArrayList<>();
        if(type == 2){      //冻品
            //查询该区域冻品仓库
            List<Warehouse> list = warehouseService.getFrozenWarehouse(zoneCode);
            if(CollectionUtils.isEmpty(list)){
                throw new BusinessException(ExceptionStatus.E2001045.getCode(),ExceptionStatus.E2001045.getMessage());
            }
            return list;
        }else{              //普通品

            List<Warehouse> warehouseList = warehouseService.getCommonWarehouse(zoneCode,type);

            Map<String, Object> params = new HashMap<>(4);       //参数map
            params.put("item_id",itemId);
            params.put("zone_id",zoneCode);

            logger.info("开始调用商城 获取sku列表 接口,参数为 : " + JsonUtils.obj2Json(params));
            long currentTime = System.currentTimeMillis();
            String resStr = this.doPost(goodServiceHost + "/goods/item/getsplist", params);
            logger.info("结束调用商城 获取sku列表 接口,时间为 : " + (System.currentTimeMillis() - currentTime) + "返回为 : " + resStr);

            Map<String,Object> resMap = JsonUtils.json2Obj(resStr,Map.class);

            if(CollectionUtils.isEmpty(resMap) || Integer.parseInt(String.valueOf(resMap.get("ret"))) != 0){
                throw new BizCheckedException(ExceptionStatus.E2001043.getCode(),ExceptionStatus.E2001043.getMessage());
            }

            Map<String,Object> contentMap = (Map<String, Object>) resMap.get("content");

            List<Map<String,Object>> detailList = (List<Map<String, Object>>) contentMap.get("list");
            AtpAssert.notEmpty(detailList,ExceptionStatus.E2001047.getCode(),ExceptionStatus.E2001047.getMessage());

            List<String> supplyList = new ArrayList<>();    //有价格的货主列表
            for(Map<String,Object> detail : detailList){
                String supplier_id = detail.get("supplier_id").toString();
                supplyList.add(supplier_id);
            }

            Integer supply = null;
            for(Warehouse warehouse : warehouseList){
                if (warehouse.getSupplyId() == 2){     //寄售仓
                    if(supplyList.contains(warehouse.getSupplyMarket().toString())){
                        supply = 2;
                        break;
                    }
                }else{              //非寄售仓库
                    if(supplyList.contains(warehouse.getSupplyMarket().toString())){
                        supply = 1;
                        break;
                    }
                }
            }
            for(Warehouse warehouse : warehouseList){
                if(warehouse.getSupplyId().intValue() == supply.intValue()){
                    result.add(warehouse);
                }
            }

            return result;

        }

    }

    /**
     * 获取itemid-saleunit对应关系,存入redis缓存
     *
     * @param time 时间,若为null,则为全量获取
     */
    public Map<String, Map<Long, Integer>> getMaxSaleUnitByTime(final String time) {

        Map<String, String> headMap = new HashMap<>();
        headMap.put("api-version", "1.1");
        headMap.put("sign", "md5");
        headMap.put("platform", "atp");

        Map<String, Object> params = new HashMap<>();   //商城接口参数map
        int pn = BusiConstant.PAGE_START;   //页起始位置
        int rn = BusiConstant.PAGE_SIZE;    //页大小
        params.put("pn", pn);
        params.put("rn", rn);

        if (StringUtils.isNotBlank(time)) {    //如果ts不为null,则为增量获取
            params.put("ts", time);
        }

        boolean flag = true;

        long currentTimes = System.currentTimeMillis();

        Map<Long, Integer> itemSaleUnit;

        Map<String, Map<Long, Integer>> zoneItemSaleUnit = new HashMap<>();


        while (flag) {

            logger.info("开始调用商城 获取sku列表 接口,参数为 : " + JsonUtils.obj2Json(params));
            long currentTime = System.currentTimeMillis();
            String resStr = HttpClientUtils.doPost( goodServiceHost + "/goods/sku/hotlist", params, headMap);
            logger.info("结束调用商城 获取sku列表 接口,时间为 : " + (System.currentTimeMillis() - currentTime));

            Item2skuList item2skuList = JSON.parseObject(resStr, Item2skuList.class);
            int ret = item2skuList.getRet();
            logger.info("ret = " + ret);

            if (ret == 0) {

                SkuContent content = item2skuList.getContent();
                int total = content.getTotal();
                //logger.info("共返回 " + total + " 条数据 , pn = " + pn + ",rn = " + rn);

                pn += rn;

                if (pn >= total) {
                    flag = false;
                } else {
                    params.put("pn", pn);
                }

                if (content != null) {

                    List<SkuInfo> skuList = content.getSku_list();

                    if (skuList != null && !skuList.isEmpty()) {

                        for (SkuInfo skuInfo : skuList) {

                            itemSaleUnit = zoneItemSaleUnit.get(skuInfo.getZone_id());

                            if (CollectionUtils.isEmpty(itemSaleUnit)) {
                                itemSaleUnit = new HashMap<>();
                                itemSaleUnit.put(skuInfo.getItem_id(), skuInfo.getSale_unit());

                                zoneItemSaleUnit.put(skuInfo.getZone_id(), itemSaleUnit);
                            } else {

                                Integer saleUnit = itemSaleUnit.get(skuInfo.getItem_id());

                                if (saleUnit == null) {      //如果redis原来没有此itemId对应的saleunit,则直接放入redis中
                                    itemSaleUnit.put(skuInfo.getItem_id(), skuInfo.getSale_unit());
                                } else {

                                    int maxUnit = saleUnit.intValue() > skuInfo.getSale_unit() ? saleUnit : skuInfo.getSale_unit();
                                    itemSaleUnit.put(skuInfo.getItem_id(), maxUnit);

                                }
                            }
                        }
                    }
                }
            }
        }

        int total = 0;

        for (String zoneCode : zoneItemSaleUnit.keySet()) {

            total += zoneItemSaleUnit.get(zoneCode).size();

        }

        logger.info("all结束调用商城 获取sku列表 接口,时间为 : " + (System.currentTimeMillis() - currentTimes) + ",返回数据总量为 :" + total);

        return zoneItemSaleUnit;
    }


    /**
     * 增量同步 物美码-链商码对应键值对。如果参数为null,则为全量同步
     *
     * @param time 上次同步时间,为null时是全量同步
     */
    public Map<String, Map<String, Long>> wumartCode2lshCode(final String time) {

        Map<String, String> headMap = new HashMap<>();
        headMap.put("api-version", "1.1");
        headMap.put("sign", "md5");
        headMap.put("platform", "atp");

        Map<String, Object> params = new HashMap<>();   //调用商城接口参数map
        int pn = BusiConstant.PAGE_START;   //页起始位置
        int rn = BusiConstant.PAGE_SIZE;    //页大小
        params.put("pn", pn);
        params.put("rn", rn);
        if (time != null) {
            params.put("ts", time);
        }


        boolean flag = true;

        long currentTimes = System.currentTimeMillis();

        Map<String, Map<String, Long>> marketWumartItemCode = new HashMap<>();

        while (flag) {

            logger.info("开始调用商城物美码-链商码接口,参数为 :  ts = " + time);
            long timestamp = System.currentTimeMillis();
            String resStr = HttpClientUtils.doPost(goodServiceHost + "/goods/supply/code2items", params, headMap);
            logger.info("结束调用商城物美码-链商码接口, 时间为 : " + (System.currentTimeMillis() - timestamp));
            //logger.info(resStr);

            WuCode2ItemCode wuCode2ItemCode = JSON.parseObject(resStr, WuCode2ItemCode.class);

            if (wuCode2ItemCode.getRet().equals("0") && wuCode2ItemCode.getContent() != null && wuCode2ItemCode.getContent().getSku_list() != null) {

                Content content = wuCode2ItemCode.getContent();
                int total = content.getTotal();
                //logger.info("共返回 " + total + " 条数据 , pn = " + pn + ",rn = " + rn);

                pn += rn;

                if (pn >= total) {
                    flag = false;
                } else {
                    params.put("pn", pn);
                }

                for (Supply supply : content.getSku_list()) {

                    if (StringUtils.isAnyBlank(supply.getCode(), supply.getItem_id(), supply.getMarket_id())) {
                        logger.warn("物美码:链商码有误,或同步数据异常 " + JSON.toJSONString(supply));
                    } else {
//                        logger.info("supply json is " + JSON.toJSONString(supply));
                        Map<String, Long> wumartItemCode = marketWumartItemCode.get(supply.getMarket_id());
                        if (CollectionUtils.isEmpty(wumartItemCode)) {
                            wumartItemCode = new HashMap<>();

                            wumartItemCode.put(supply.getCode(), Long.parseLong(supply.getItem_id()));
                            marketWumartItemCode.put(supply.getMarket_id(), wumartItemCode);
                        } else {
                            if (wumartItemCode.get(supply.getCode()) != null) {
                                logger.info("物美码 " + supply.getCode() + "出现重复,market = " + supply.getMarket_id());
//                                logger.info("物美码 " + supply.getCode() + "原code = " + supply.getItem_id() + ": 新Code : " + wumartItemCode.get(supply.getCode()));

                            }
                            wumartItemCode.put(supply.getCode(), Long.parseLong(supply.getItem_id()));
                        }

                    }

                }

            }
        }

        logger.info("all 物美码转链商码 接口,时间为 : " + (System.currentTimeMillis() - currentTimes));

        return marketWumartItemCode;
    }


    /**
     * 调用商城接口
     *
     * @param url url
     * @return 返回结果
     */
    private String doPost(String url, Map param){
        Map<String, String> headMap = new HashMap<>();      //head map
        headMap.put("api-version", "1.1");
        headMap.put("sign", "md5");
        headMap.put("platform", "atp");

        return HttpClientUtils.doPost(url, param, headMap);
    }



}
