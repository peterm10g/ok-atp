package com.lsh.swagger.register;

import java.lang.reflect.Method;

/**
 * Created by zhangqiang on 17/5/2.
 */
public interface SwaggerRegister {

    void registy(String name, Method method, Class[] params);
}
