package com.itihub.rabbit.producer.broker;

import com.itihub.rabbit.api.Message;

/**
 * $RabbitBroker 定义发送不同类型消息接口
 */
public interface RabbitBroker {

    /**
     * 迅速消息
     * 特点：异步发送，无ACK
     * @param message
     */
    void rapidSend(Message message);

    /**
     * 确认消息
     * 特点：异步发送，有ACK
     * @param message
     */
    void confirmSend(Message message);

    /**
     * 可靠性消息
     * @param message
     */
    void reliantSend(Message message);

    void sendMessages();
}
