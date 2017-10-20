package com.lsh.swagger;

import com.alibaba.dubbo.common.URL;
import com.lsh.swagger.register.RPCSwaggerRegister;
import com.lsh.swagger.register.SwaggerRegister;

import java.lang.reflect.Method;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class RPCSwaggerReader extends AbstractSwaggerReader{

    public RPCSwaggerReader(URL url) {
        this.setRegister(new RPCSwaggerRegister(url));
    }

    @Override
    protected Class[] getParameters(Method method) {
        Class[] paramsClasses = method.getParameterTypes();
        return paramsClasses;
    }

    @Override
    protected Class<?> getResponse(Method method) {
        return method.getReturnType();
    }

}
