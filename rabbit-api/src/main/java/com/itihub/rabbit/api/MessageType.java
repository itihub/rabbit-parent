package com.itihub.rabbit.api;

/**
 * 消息可靠性投递级别
 */
public final class MessageType {

    /**
     * 迅速消息
     *      不需要保证消息的可靠性
     *      也不需要做confirm确认
     */
    public static final String RAPID = "0";

    /**
     * 确认消息
     *      不需要保证消息的可靠性
     *      需要做消息的confirm确认
     */
    public static final String CONFIRM = "1";

    /**
     * 可靠性消息
     *      保证消息的100%可靠投递，不允许丢消息
     *      PS：保障数据库和所发的消息是原子性的（最终一致）
     */
    public static final String RELIANT = "2";
}
