package com.itihub.rabbit.producer.broker;

import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.api.SendCallback;

import java.util.concurrent.ExecutionException;

/**
 * $RabbitBroker 定义发送不同类型消息接口
 */
public interface RabbitBroker {

    /**
     * 消息的发送附带SendCallback函数
     * 特点：异步发送，有ACK，回调函数
     * @param message
     * @param sendCallback
     */
    void sendCallback(Message message, SendCallback sendCallback);

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
     * 特点：消息落库，重试机制，有ACK
     * @param message
     */
    void reliantSend(Message message);

    /**
     * 批量发送
     * 特点：基于ThreadLocal实现的批量发送
     */
    void sendMessages();
}
