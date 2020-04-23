package com.itihub.rabbit.producer.autoconfigure;

import com.itihub.rabbit.producer.broker.ProducerClient;
import com.itihub.rabbit.producer.broker.RabbitBroker;
import com.itihub.rabbit.producer.broker.RabbitBrokerImpl;
import com.itihub.rabbit.producer.broker.RabbitTemplateContainer;
import com.itihub.rabbit.producer.mapper.BrokerMessageMapper;
import com.itihub.rabbit.producer.service.MessageStoreService;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * $RabbitProducerAutoConfiguration 自动装配
 */
@Configuration
public class RabbitProducerAutoConfiguration {

    @Autowired
    private BrokerMessageMapper brokerMessageMapper;

    @Bean
    public MessageStoreService messageStoreService(){
        return new MessageStoreService(brokerMessageMapper);
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

}
