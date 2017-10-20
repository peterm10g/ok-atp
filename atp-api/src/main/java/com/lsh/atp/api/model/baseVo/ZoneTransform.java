package com.lsh.atp.api.model.baseVo;

/**
 * 区域转换的接口
 *
 * 实现此接口的model类,业务中对区域码进行转换,通过aop
 *
 * ZoneTransAspect
 *
 * Created by zhangqiang on 17/6/22.
 */
public interface ZoneTransform {

    String getZoneCode();

    void setZoneCode(String zoneCode);

    String getChannel();

    void setChannel(String channel);
}
