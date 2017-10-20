package com.lsh.atp.core.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;

/**
 * Created by zhangqiang on 17/6/20.
 */
public class ZoneResolverTest {

    @Test
    public void testJsonFile(){
        ZoneResolver zoneResolver = new ZoneResolver();
        Set<String> set = zoneResolver.getChannels();
        Assert.assertTrue(set.size() == 1);
        Assert.assertTrue(set.contains("2"));
    }

    @Test
    public void testCode(){
        ZoneResolver zoneResolver = new ZoneResolver();
        Assert.assertEquals(zoneResolver.getRealZoneCode("2","2000"),"1000");
        Assert.assertEquals(zoneResolver.getRealZoneCode("2","3000"),"4000");
        Assert.assertEquals(zoneResolver.getZoneCode("2","1000"),"2000");
        Assert.assertEquals(zoneResolver.getZoneCode("2","4000"),"3000");
    }
}
