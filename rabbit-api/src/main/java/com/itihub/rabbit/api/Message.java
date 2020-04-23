package com.itihub.rabbit.api;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 自定义消息对象
 */
@Data
public class Message implements Serializable {

    private static final long serialVersionUID = 6921022283095100573L;

    /** 消息唯一ID **/
    private String messageId;

    /** 消息的主题 **/
    private String topic;

    /** 消息的路由规则 **/
    private String routingKey = "";

    /** 消息的附加属性 **/
    private Map<String, Object> attributes = new HashMap<>();

    /** 延迟消息的参数配置 PS:需要RabbitMQ安装延迟插件 **/
    private int delayMills;

    /** 消息类型 默认confirm消息 **/
    private String messageType = MessageType.CONFIRM;

    public Message() {
    }

    public Message(String messageId, String topic, String routingKey, Map<String, Object> attributes, int delayMills, String messageType) {
        this.messageId = messageId;
        this.topic = topic;
        this.routingKey = routingKey;
        this.attributes = attributes;
        this.delayMills = delayMills;
        this.messageType = messageType;
    }
}
