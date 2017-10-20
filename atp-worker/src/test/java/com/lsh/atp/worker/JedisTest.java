package com.lsh.atp.worker;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangqiang on 16/12/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/applicationContext-worker.xml")
public class JedisTest {

    @Resource(name="jedisPool")
    private JedisPool jedisPool;

    @Test
    public void testRunData() throws Exception {
        int num = 1;
        List<String> list = new ArrayList<String>(num);
        List<String> pipelineList = new ArrayList<String>(num);
        for(int i = 0; i < num; i ++){
            list.add("test:key:" + i);
            pipelineList.add("test:pipe:key:" + i);
        }



        //普通方式
        long t = System.currentTimeMillis();
        Jedis jedis = jedisPool.getResource();
        for(String key : list){
            jedis.get(key);
        }
        jedis.close();
        System.out.println("time : " + (System.currentTimeMillis() - t));

        //pipeline
        t = System.currentTimeMillis();
        jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        Map<String,Response<String>> result =  new HashMap();
        for(String key : pipelineList){
            Response<String> value = pipeline.get(key);
            result.put(key,value);
        }
        pipeline.sync();
        pipeline.close();
        jedis.close();
        System.out.println("pipe time : " + (System.currentTimeMillis() - t));


//        for(Map.Entry<String,Response<String>> entry : result.entrySet()){
//            System.out.print(entry.getKey() + "" +  entry.getValue().get());
//        }
    }
}
