package com.lsh.atp.web.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by zhangqiang on 17/5/12.
 */
@Configuration
@ImportResource(locations = {"classpath*:spring/applicationContext-core.xml","classpath*:spring/applicationContext-redis.xml"})
public class SrpingXmlConfig {

    @Bean
    public PropertyPlaceholderConfigurer setProperties(){
        PropertyPlaceholderConfigurer p = new PropertyPlaceholderConfigurer();
        p.setLocations(new ClassPathResource("props/db.properties"),
                new ClassPathResource("props/redis.properties"),
                new ClassPathResource("props/elasticsearch.properties"),
                new ClassPathResource("props/web.properties"));
        return p;
    }
}
