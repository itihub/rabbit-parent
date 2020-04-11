package com.itihub.rabbit.common.serializer;

/**
 * 序列化反序列化接口
 */
public interface Serializer {

    byte[] serializerRaw(Object data);

    String serializer(Object data);

    <T> T deserializer(String content);

    <T> T deserializer(byte[] content);
}
