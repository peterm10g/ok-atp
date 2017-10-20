package com.lsh.swagger;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ProtocolConfig;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created by zhangqiang on 17/5/2.
 */
public class SwaggerConfig implements BeanFactoryAware {

    private String zkUrl;

    private Integer zkPort;

    private String resourcePackage;

    public static Map<Protocol,Set<ProtocolConfig>> protocolConfigMap = new HashMap(4);

    public SwaggerConfig(){

    }

    public SwaggerConfig(String zkUrl, Integer zkPort, String resourcePackage) {
        this.zkUrl = zkUrl;
        this.zkPort = zkPort;
        this.resourcePackage = resourcePackage;
    }

    public void register(){
        Set<Class<?>> classes = getAllServiceClass();
        if(!CollectionUtils.isEmpty(classes)){
            for(Class<?> clazz : classes){
                //判断clazz接口协议
                SwaggerReader reader = ReaderFactory.getReader(clazz,new URL(null,zkUrl,zkPort));
                if(reader != null){
                    reader.registy(clazz);
                }
            }
        }
    }

    private Set<Class<?>> getAllServiceClass() {
        ConfigurationBuilder config = new ConfigurationBuilder();

        if (resourcePackage != null && !"".equals(resourcePackage)) {
            //多个package通过,来分割
            String[] parts = resourcePackage.split(",");
            for (String pkg : parts) {
                if (!"".equals(pkg)) {
                    config.addUrls(ClasspathHelper.forPackage(pkg));
                }
            }
        }

        config.setScanners(new ResourcesScanner(), new TypeAnnotationsScanner(), new SubTypesScanner());

        final Reflections reflections = new Reflections(config);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(com.alibaba.dubbo.config.annotation.Service.class);
        return classes;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        String[] protocolBeans = ((DefaultListableBeanFactory)beanFactory).getBeanNamesForType(ProtocolConfig.class);
        for(String id : protocolBeans){
            ProtocolConfig config = (ProtocolConfig) beanFactory.getBean(id);
            String name = config.getName();
            Protocol protocol;
            if(name.startsWith("rest")){
                protocol = Protocol.REST;
            }else{
                protocol = Protocol.RPC;
            }

            if(protocolConfigMap.containsKey(protocol)){
                protocolConfigMap.get(protocol).add(config);
            }else{
                Set<ProtocolConfig> set = new HashSet<>();
                set.add(config);
                protocolConfigMap.put(protocol,set);
            }
        }
        register();
    }

    public String getZkUrl() {
        return zkUrl;
    }

    public void setZkUrl(String zkUrl) {
        this.zkUrl = zkUrl;
    }

    public Integer getZkPort() {
        return zkPort;
    }

    public void setZkPort(Integer zkPort) {
        this.zkPort = zkPort;
    }

    public String getResourcePackage() {
        return resourcePackage;
    }

    public void setResourcePackage(String resourcePackage) {
        this.resourcePackage = resourcePackage;
    }
}
