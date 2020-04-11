package com.itihub.rabbit.producer.broker;

import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.MessageType;
import com.itihub.rabbit.producer.constant.BrokerMessageConst;
import com.itihub.rabbit.producer.constant.BrokerMessageStatus;
import com.itihub.rabbit.producer.entity.BrokerMessage;
import com.itihub.rabbit.producer.service.MessageStoreService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * $RabbitBrokerImpl 实现发送不同类型的消息
 */
@Slf4j
public class RabbitBrokerImpl implements RabbitBroker {

    @Autowired
    private RabbitTemplateContainer rabbitTemplateContainer;

    @Autowired
    private MessageStoreService messageStoreService;

    @Override
    public void rapidSend(Message message) {
        message.setMessageType(MessageType.RAPID);
        sendKernel(message);
    }

    @Override
    public void confirmSend(Message message) {
        message.setMessageType(MessageType.CONFIRM);
        sendKernel(message);
    }

    @Override
    public void reliantSend(Message message) {
        message.setMessageType(MessageType.RELIANT);

        // 1. 待发送的消息落库记录
        Date now = new Date();
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setStatus(BrokerMessageStatus.SENDING.getCode());
        // try_count 在第一次发送时不需要被赋值 默认0;
        brokerMessage.setNextRetry(DateUtils.addMinutes(now, BrokerMessageConst.TIMEOUT));
        brokerMessage.setCreateTime(now);
        brokerMessage.setUpdateTime(now);
        brokerMessage.setMessage(message);
        messageStoreService.insert(brokerMessage);

        // 2. 发送消息到MQ
        sendKernel(message);
    }

    @Override
    public void sendMessages() {

    }

    /**
     * 发送消息的核心方法
     * 异步线程池进行发送消息
     * @param message
     */
    private void sendKernel(Message message) {
        AsyncBaseQueue.submit((Runnable) ()-> {
            CorrelationData correlationData = new CorrelationData(String.format("%s#%s"
                    , message.getMessageId()
                    , System.currentTimeMillis()));
            String topic = message.getTopic();
            String routingKey = message.getRoutingKey();
            RabbitTemplate rabbitTemplate = rabbitTemplateContainer.getTemplate(message);
            rabbitTemplate.convertAndSend(topic, routingKey, message, correlationData);
            log.info("#RabbitBrokerImpl.sendKernel# send to rabbitmq messageId: {}", message.getMessageId());
        });

    }
}
