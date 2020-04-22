package com.itihub.rabbit.common.convert;

import com.google.common.base.Preconditions;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * 基于GenericMessageConverter的代理
 * 使用了装饰者模式或代理模式
 */
public class RabbitMessageConverter implements MessageConverter {

    private GenericMessageConverter delegate;

    /** 默认过期时间 **/
    private final String defaultExpire = String.valueOf(24 * 60 * 60 * 1000);

    public RabbitMessageConverter(GenericMessageConverter genericMessageConverter) {
        Preconditions.checkNotNull(genericMessageConverter);
        this.delegate = genericMessageConverter;
    }

    @Override
    public Message toMessage(Object o, MessageProperties messageProperties) throws MessageConversionException {
        // 代理模式 之前加入自己业务逻辑
        messageProperties.setExpiration(defaultExpire);
        return this.delegate.toMessage(o, messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        com.itihub.rabbit.api.Message msg = (com.itihub.rabbit.api.Message) this.delegate.fromMessage(message);
        return msg;
    }
}
