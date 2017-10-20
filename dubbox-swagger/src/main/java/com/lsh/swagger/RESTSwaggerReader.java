package com.lsh.swagger;

import java.lang.reflect.Method;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class RESTSwaggerReader extends AbstractSwaggerReader{

    @Override
    protected Class[] getParameters(Method method) {
        return null;
    }

    @Override
    protected Class<?> getResponse(Method method) {
        return null;
    }
}
