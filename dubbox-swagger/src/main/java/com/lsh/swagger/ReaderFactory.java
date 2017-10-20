package com.lsh.swagger;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.annotation.Service;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class ReaderFactory {

    public static SwaggerReader getReader(Class<?> clazz,URL url) {
        Service service = clazz.getAnnotation(Service.class);
        String[] protocol = service.protocol();

        if(protocol.length == 0){
            return new RPCSwaggerReader(url);
        }else if(protocol[0].equals(Protocol.REST.getProtocol())){
            return new RESTSwaggerReader();
        }else if(protocol[0].equals(Protocol.RPC.getProtocol()) || protocol[0].equals("dubbo")){
            return new RPCSwaggerReader(url);
        }
        return null;
    }
}
