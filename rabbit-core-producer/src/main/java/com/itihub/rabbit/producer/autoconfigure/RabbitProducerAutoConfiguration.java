package com.itihub.rabbit.producer.autoconfigure;

import com.itihub.rabbit.producer.broker.ProducerClient;
import com.itihub.rabbit.producer.broker.RabbitBroker;
import com.itihub.rabbit.producer.broker.RabbitBrokerImpl;
import com.itihub.rabbit.producer.broker.RabbitTemplateContainer;
import com.itihub.rabbit.producer.config.database.RabbitProduceMyBatisConfiguration;
import com.itihub.rabbit.producer.mapper.BrokerMessageMapper;
import com.itihub.rabbit.producer.service.MessageStoreService;
import com.itihub.rabbit.producer.task.RetryMessageDataflowJob;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * $RabbitProducerAutoConfiguration 自动装配
 */
@Slf4j
@Configuration
@AutoConfigureAfter(value = {RabbitProduceMyBatisConfiguration.class})
public class RabbitProducerAutoConfiguration {

    public RabbitProducerAutoConfiguration() {
        log.info("Initializing RabbitProducerAutoConfiguration");
    }

    @Autowired
    private SqlSessionTemplate rabbitProducerSqlSessionTemplate;


    @Bean
    public BrokerMessageMapper brokerMessageMapper(){
        return rabbitProducerSqlSessionTemplate.getMapper(BrokerMessageMapper.class);
    }

    @Bean
    public MessageStoreService messageStoreService(){
        return new MessageStoreService(brokerMessageMapper());
    }

    @Bean
    public RabbitTemplateContainer rabbitTemplateContainer(){
        return new RabbitTemplateContainer(messageStoreService());
    }

    @Bean
    public RabbitBroker rabbitBroker(){
        return new RabbitBrokerImpl(rabbitTemplateContainer(), messageStoreService());
    }

    @Bean
    public ProducerClient producerClient(){
        return new ProducerClient(rabbitBroker());
    }

    @Bean
    public RetryMessageDataflowJob retryMessageDataflowJob(){
        return new RetryMessageDataflowJob(messageStoreService(), rabbitBroker());
    }

}
