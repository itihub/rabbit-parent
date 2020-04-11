package com.itihub.rabbit.producer.broker;

import com.google.common.base.Preconditions;
import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.MessageProducer;
import com.itihub.rabbit.api.MessageType;
import com.itihub.rabbit.api.SendCallback;
import com.itihub.rabbit.exception.MessageRunTimeException;

import java.util.List;

/**
 * $ProducerClient 发送消息的实际实现类
 */
public class ProducerClient implements MessageProducer {

    private RabbitBroker rabbitBroker;

    @Override

    public void send(Message message, SendCallback sendCallback) {

    }

    @Override
    public void send(Message message) throws MessageRunTimeException {
        Preconditions.checkNotNull(message.getTopic());
        String messageType = message.getMessageType();
        switch (messageType) {
            case MessageType.RAPID:
                rabbitBroker.rapidSend(message);
                break;
            case MessageType.CONFIRM:
                rabbitBroker.confirmSend(message);
                break;
            case MessageType.RELIANT:
                rabbitBroker.reliantSend(message);
                break;
            default:
                break;
        }
    }

    @Override
    public void send(List<Message> messages) throws MessageRunTimeException {

    }
}
