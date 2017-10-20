package com.lsh.swagger;

import com.lsh.swagger.register.SwaggerRegister;

import java.lang.reflect.Method;

/**
 * Created by zhangqiang on 17/5/2.
 */
public abstract class AbstractSwaggerReader implements SwaggerReader{

    private SwaggerRegister register;

    @Override
    public void registy(Class<?> clazz) {
        String interfaceName = null;
        //inteface
        if (clazz.getInterfaces().length > 0) {
            interfaceName = clazz.getInterfaces()[0].getName();
        } else {
            throw new IllegalStateException("Failed to export remote service class " + clazz.getName() + ", cause: The @Service undefined interfaceClass or interfaceName, and the service class unimplemented any interfaces.");
        }

        Method[] methods = clazz.getDeclaredMethods();
        for(Method method : methods){
            Class[] params = getParameters(method);
            if(params != null){
                register.registy(interfaceName,method,params);
            }
        }
    }

    protected abstract Class[] getParameters(Method method);

    protected abstract Class<?> getResponse(Method method);

    public SwaggerRegister getRegister() {
        return register;
    }

    public void setRegister(SwaggerRegister register) {
        this.register = register;
    }


}
