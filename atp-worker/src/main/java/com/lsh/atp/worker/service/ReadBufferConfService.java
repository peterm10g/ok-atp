package com.lsh.atp.worker.service;

import com.lsh.atp.core.model.buffer.SkuBufferConf;
import com.lsh.atp.core.model.buffer.SkuBufferConfs;
import com.lsh.base.common.json.JsonUtils2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.io.*;

/**
 * Created by zhangqiang on 16/9/21.
 */
@Component
public class ReadBufferConfService {

    Logger logger = LoggerFactory.getLogger(ReadBufferConfService.class);



    private final static String CONF_PATH = "buffer_conf/buffer_conf.json";

    public SkuBufferConfs getSkuBufferConfList() throws IOException {
        String json = this.getJson(CONF_PATH);
        SkuBufferConfs skuBufferConfs = JsonUtils2.json2Obj(json,SkuBufferConfs.class);
        return skuBufferConfs;
    }


    /**
     * 读取配置文件,得到Json字符串
     * **/
    private String getJson(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        InputStream in = null;
        BufferedReader reader = null;
        try {
            in = ReadBufferConfService.class.getClassLoader().getResourceAsStream(path);
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            logger.error("读取Buffer配置文件失败",e);
            throw e;
        } finally {
            try {
                in.close();
                reader.close();
            } catch (IOException e) {
                logger.error("读取Buffer配置文件失败",e);
            }
        }
        return sb.toString();
    }


    /**
     * 校验skuBufferConf是否合法
     *
     * @param skuBufferConf
     * @return 如果正确,返回true;否则返回false
     * **/
    public boolean validateSkuBufferConf(final SkuBufferConf skuBufferConf) {
        //如果结束时间小于等于开始时间,不合理
        if(skuBufferConf.getEnd().compareTo(skuBufferConf.getStart()) < 1){
            return false;
        }
        //如果倍数小于0,不合理
        if(skuBufferConf.getTimes().intValue() == 0){
            return false;
        }

        return true;
    }

}
