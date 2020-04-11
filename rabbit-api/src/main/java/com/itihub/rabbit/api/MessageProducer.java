package com.itihub.rabbit.api;

import com.itihub.rabbit.exception.MessageRunTimeException;

import java.util.List;

/**
 * $MessageProducer 消息提供者开放接口
 */
public interface MessageProducer {

    /**
     * 消息的发送附带SendCallback回调执行响应的业务逻辑处理
     * @param message
     * @param sendCallback 回调
     */
    void send(Message message, SendCallback sendCallback);

    /**
     * 消息发送
     * @param message
     * @throws MessageRunTimeException
     */
    void send(Message message) throws MessageRunTimeException;

    /**
     * 消息批量发送
     * @param messages
     * @throws MessageRunTimeException
     */
    void send(List<Message> messages) throws MessageRunTimeException;


}
