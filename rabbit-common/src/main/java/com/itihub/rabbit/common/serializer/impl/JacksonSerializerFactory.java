package com.itihub.rabbit.common.serializer.impl;

import com.itihub.rabbit.api.Message;
import com.itihub.rabbit.common.serializer.Serializer;
import com.itihub.rabbit.common.serializer.SerializerFactory;

public class JacksonSerializerFactory implements SerializerFactory {

    public static final JacksonSerializerFactory INSTANCE = new JacksonSerializerFactory();

    @Override
    public Serializer create() {
        return JacksonSerializer.createParametricType(Message.class);
    }
}
