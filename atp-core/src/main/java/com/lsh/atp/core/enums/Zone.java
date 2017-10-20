package com.lsh.atp.core.enums;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 17/7/21.
 */
public enum Zone {

    BEIJING("1000","北京"),

    TIANJING("1001","天津"),

    HANGZOUH("1002","杭州");

    private String zoneCode;

    private String zoneName;

    private static Map<String,Zone> map = new HashMap<>(8);
    static {
        for(Zone zone : Zone.values()){
            map.put(zone.getZoneCode(),zone);
        }
    }

    Zone(String zoneCode, String zoneName) {
        this.zoneCode = zoneCode;
        this.zoneName = zoneName;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public static Zone parse(String zoneCode){
        return map.get(zoneCode);
    }

    public List<String> getSubZoneCode(){   //// TODO: 17/7/21 后期改为配置,先写死
        List<String> result = new ArrayList<>();
        if(this == Zone.BEIJING){
            result.add("SABJ01");
            result.add("SABJ02");
        }else{
            result.add(this.getZoneCode());
        }
        return result;
    }


    public String getDefaultSubZoneCode(){
        String result = null;
        if(this == Zone.BEIJING){
            result = "SABJ02";
        }else{
            result = this.zoneCode;
        }
        return result;
    }
}
