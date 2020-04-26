package com.itihub.rabbit.producer.broker;

import com.google.common.collect.Lists;
import com.itihub.rabbit.api.Message;

import java.util.List;

/**
 *  $MessageHolder 用于批量发送消息的消息存储
 */
public class MessageHolder {

    private List<Message> messageList = Lists.newArrayList();

    public static final ThreadLocal<MessageHolder> holder = new ThreadLocal<MessageHolder>(){
        @Override
        protected MessageHolder initialValue() {
            return new MessageHolder();
        }
    };

    public static void add(Message message){
        holder.get().messageList.add(message);
    }

    public static List<Message> clear(){
        List<Message> tmp = Lists.newArrayList(holder.get().messageList);
        holder.remove();
        return tmp;
    }
}
