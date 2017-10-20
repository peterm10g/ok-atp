package com.lsh.atp.core.config;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangqiang on 17/5/11.
 */
@Configuration
@EnableElasticsearchRepositories(basePackages = "com.lsh.atp.core.dao.es")
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String esHost;

    @Value("${elasticsearch.port}")
    private Integer esPort;

    @Value("${elasticsearch.cluster.name}")
    private String esClusterName;

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws UnknownHostException {
        if(esHost == null || esPort == null){
            return null;
        }
        //es settings
        Settings.Builder settingsBuilder = Settings.builder();
        if(StringUtils.isNotEmpty(esClusterName)){
            settingsBuilder.put("cluster.name",esClusterName);

        }
        Settings settings = settingsBuilder.build();

        TransportClient client = TransportClient.builder().settings(settings).build();

        TransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(esHost), esPort);
        client.addTransportAddress(address);
        return new ElasticsearchTemplate(client);
    }

    public static void main(String[] args) throws UnknownHostException {
        InetAddress address = InetAddress.getByName("192.168.60.48");
        System.out.println(address.getHostName());
    }
}
