package com.lsh.atp.core.util;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.lsh.base.common.json.JsonUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.CachedIntrospectionResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.core.OverridingClassLoader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangqiang on 17/6/20.
 */
public class ZoneResolver {

    private static final Logger logger = LoggerFactory.getLogger(ZoneResolver.class);

    private static final String ZoneFilePath = "zone_transform.json";

    private static Set<String> channels = Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>(8));

    private static Map<String,Map<String,String>> zoneToRealMap = new ConcurrentHashMap<>();

    private static Map<String,Map<String,String>> realToZoneMap = new ConcurrentHashMap<>();

    static {
        String json = getJsonFromFile(ZoneFilePath);
        if(!StringUtils.isBlank(json)){
            resolveZone(json);
        }
    }

    public static Set<String> getChannels() {
        return channels;
    }

    public static String getZoneCode(String channel,String realZoneCode){
        Map<String,String> map = realToZoneMap.get(channel);
        if(map == null){
            return realZoneCode;
        }
        String zoneCode =  map.get(realZoneCode);
        if(zoneCode == null){
            return realZoneCode;
        }
        return zoneCode;
    }

    public static String getRealZoneCode(String channel,String zoneCode){
        Map<String,String> map = zoneToRealMap.get(channel);
        if(map == null){
            return zoneCode;
        }
        String realZoneCode =  map.get(zoneCode);
        if(realZoneCode == null){
            return zoneCode;
        }
        return realZoneCode;
    }

    private static void resolveZone(String json) {
        List list = JsonUtils.json2Obj(json.toString(),List.class);
        if(list == null ){
            return;
        }
        for(Object obj : list){
            Map map = (Map) obj;
            String channel = map.get("channel").toString();
            channels.add(channel);

            String zoneCode = (String) map.get("zoneCode");
            String realZoneCode = (String) map.get("realZoneCode");

            addZoneToRealMap(channel,zoneCode,realZoneCode);
            addRealToZoneMap(channel,zoneCode,realZoneCode);
        }
    }



    private  static void addZoneToRealMap(String channel, String zoneCode, String realZoneCode) {
        Map<String,String> map = zoneToRealMap.get(channel);
        if(map == null){
            map = new HashMap<>();
            zoneToRealMap.put(channel,map);
        }
        map.put(zoneCode,realZoneCode);
    }

    private  static void addRealToZoneMap(String channel, String zoneCode, String realZoneCode) {
        Map<String,String> map = realToZoneMap.get(channel);
        if(map == null){
            map = new HashMap<>();
            realToZoneMap.put(channel,map);
        }
        map.put(realZoneCode,zoneCode);
    }

    private static String getJsonFromFile(String zoneFilePath) {
        //读取文件
        ClassPathResource resource = new ClassPathResource(zoneFilePath);
        BufferedReader br = null;
        StringBuffer json = new StringBuffer();
        try {
            File file = resource.getFile();
            br = new BufferedReader(new FileReader(file));
            String temp;
            while((temp = br.readLine()) != null) {
                json.append(temp);
            }
        } catch (IOException e) {
            logger.error("",e);
        }finally {
            try {
                if(br != null){
                    br.close();
                }
            } catch (IOException e) {
                logger.error("",e);
            }
        }
        return json.toString();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader child = new OverridingClassLoader(ZoneResolver.class.getClassLoader());
        Class<?> tbClass = child.loadClass("com.lsh.atp.core.util.ZoneResolver");
        System.out.println(ZoneResolver.class);
        System.out.println(tbClass);
        System.out.println(tbClass.equals(ZoneResolver.class));
    }
}
