package com.itihub.rabbit.producer.broker;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.MessageType;
import com.itihub.rabbit.common.convert.GenericMessageConverter;
import com.itihub.rabbit.common.convert.RabbitMessageConverter;
import com.itihub.rabbit.common.serializer.Serializer;
import com.itihub.rabbit.common.serializer.SerializerFactory;
import com.itihub.rabbit.common.serializer.impl.JacksonSerializerFactory;
import com.itihub.rabbit.exception.MessageRunTimeException;
import com.itihub.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * $RabbitTemplateContainer 池化封装
 * 池化优点
 *      1.提高发送的效率
 *      2.可以根据不同的需求制定化不同的RabbitTemplate。比如每一个topic都有自己的RoutingKey规则
 */
@Slf4j
@Component
public class RabbitTemplateContainer implements RabbitTemplate.ConfirmCallback {

    /**
     * RabbitTemplate存储
     *  key 是topic
     *  value 是该topic的RabbitTemplate存储
     */
    private Map<String, RabbitTemplate> rabbitMap = Maps.newConcurrentMap();

    private Splitter splitter = Splitter.on("#");

    private SerializerFactory serializerFactory = JacksonSerializerFactory.INSTANCE;
    

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private MessageStoreService messageStoreService;

    public RabbitTemplate getTemplate(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String topic = message.getTopic();
        RabbitTemplate rabbitTemplate = rabbitMap.get(topic);
        if (rabbitTemplate != null){
            return rabbitTemplate;
        }
        log.info("#RabbitTemplateContainer.getTemplate# topic: {} is not exists, create one", topic);

        RabbitTemplate newRabbitTemplate = new RabbitTemplate(connectionFactory);
        newRabbitTemplate.setExchange(topic);
        newRabbitTemplate.setRoutingKey(message.getRoutingKey());
        newRabbitTemplate.setRetryTemplate(new RetryTemplate());
        
        // 添加对于自定义Message对象序列化反序列和converter对象
        Serializer serializer = serializerFactory.create();
        GenericMessageConverter gmc = new GenericMessageConverter(serializer);
        // 装饰GenericMessageConverter
        RabbitMessageConverter rmc = new RabbitMessageConverter(gmc);
        newRabbitTemplate.setMessageConverter(rmc);

        String messageType = message.getMessageType();
        if (!MessageType.RAPID.equals(messageType)){
            newRabbitTemplate.setConfirmCallback(this);
        }
        rabbitMap.put(topic, newRabbitTemplate);
        return rabbitMap.get(topic);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String s) {
        // 具体的消息应答
        List<String> strings = splitter.splitToList(correlationData.getId());
        String messageId = strings.get(0);
        Long sendTime = Long.parseLong(strings.get(1));

        if (ack){
            // 当Broker返回ACK成功时，更新数据库记录数据状态为SEND_OK
            messageStoreService.success(messageId);
            log.info("send message is ok, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }else {
            log.error("send message if Fail, confirm messageId: {}, sendTime: {}", messageId, sendTime);
        }
    }
}
