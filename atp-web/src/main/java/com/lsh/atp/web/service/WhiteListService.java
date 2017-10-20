package com.lsh.atp.web.service;

import com.lsh.atp.core.util.HttpClientUtils;
import com.lsh.atp.web.domain.ItemWhite;
import com.lsh.base.common.json.JsonUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17/6/15.
 */
@Service
public class WhiteListService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(WhiteListService.class);

    @Value("${risk.api.host}")
    private String riskHost = "";

    public boolean addWhiteList(ItemWhite itemWhite){

        List<ItemWhite> list =  new ArrayList<>(1);
        list.add(itemWhite);
        Map<String,Object> map = new HashMap<>(3);
        map.put("itemWhiteList_add",list);
        String requestJson = JsonUtils.obj2Json(map);

        Map header = new HashMap(4);
        header.put("Content-Type","application/json");

        logger.info("请求数据为 : " + requestJson);
        String responseJson = HttpClientUtils.doPostBody(riskHost + "/api/rsm/java/v1/risk/sku/whiteList",requestJson,header);
        logger.info("返回数据为 : " + responseJson);

        Map responseMap = JsonUtils.json2Obj(responseJson,Map.class);
        if(((Map)(responseMap.get("head"))).get("status") == 1){
            return true;
        }
        return false;
    }

    public static void main(String[] args){
       ItemWhite itemWhite = new ItemWhite("123","123","123","123","123","123","123");
        System.out.println(JsonUtils.obj2Json(itemWhite));
    }
}
