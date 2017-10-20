package com.lsh.atp.worker.kafka;

import com.dmall.dmg.sdk.core.ConnectionFactory;
import com.dmall.dmg.sdk.core.consumer.MessageHandler;
import com.dmall.dmg.sdk.core.consumer.kafka.KafkaConsumerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Created by zhangqiang on 17/3/24.
 */
@Configuration
public class DmgConsumerConf {

    Logger logger = LoggerFactory.getLogger(DmgConsumerConf.class);

    @Value("${clusterId}")
    private Long clusterId;

    @Value("${appKey}")
    private String appKey;

    @Value("${secretKey}")
    private String secretKey;

    @Value("${authUrl}")
    private String authUrl;

    @Value("${group.id}")
    private String groupId;

    @Value("${num.consumers}")
    private String consumersNum;

    @Value("${topic.ls.stock}")
    private String topicName;

    @Value("${auto.offset.reset}")
    private String autoOffsetReset;

    @Value("${zookeeper.connection.timeout.ms}")
    private String connTimeout;

    @Value("${zookeeper.session.timeout.ms}")
    private String sessionTimeout;

    @Value("${auto.commit.enable}")
    private String commitEnable;

    @Value("${rebalance.max.retries}")
    private String retries;

    @Value("${rebalance.backoff.ms}")
    private String backoff;

    @Value("${fetch.message.max.bytes}")
    private String maxBytes;

    @Value("${kafka.switch}")
    private int kafkaSwitch;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageHandler messageHandler;

    @Bean
    public ConnectionFactory getDmgConnectionFactory(){
        logger.info("开始初始化 DmgConnectionFactory bean");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setClusterId(clusterId);
        connectionFactory.setAppKey(appKey);
        connectionFactory.setSecretKey(secretKey);
        connectionFactory.setAuthUrl(authUrl);
        return connectionFactory;
    }

    @Bean
    public KafkaConsumerContainer getKafkaConsumerContainer(){
        if(kafkaSwitch == 0){       //开关
            return null;
        }
        logger.info("开始初始化 KafkaConsumerContaine bean");

        KafkaConsumerContainer kafkaConsumerContainer = new KafkaConsumerContainer();
        kafkaConsumerContainer.setConnectionFactory(connectionFactory);
        kafkaConsumerContainer.setHandler(messageHandler);

        Properties p = new Properties();
        p.put("group.id",groupId);
        p.put("topic.name",topicName);
        p.put("num.consumers",consumersNum);
        p.put("auto.offset.reset",autoOffsetReset);
        p.put("zookeeper.connection.timeout.ms",connTimeout);
        p.put("zookeeper.session.timeout.ms",sessionTimeout);
        p.put("auto.commit.enable",commitEnable);
        p.put("rebalance.max.retries",retries);
        p.put("rebalance.backoff.ms",backoff);
        p.put("fetch.message.max.bytes",maxBytes);
        kafkaConsumerContainer.setProperties(p);
        return kafkaConsumerContainer;
    }
}
