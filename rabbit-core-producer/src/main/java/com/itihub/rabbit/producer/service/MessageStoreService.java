package com.itihub.rabbit.producer.service;

import com.itihub.rabbit.producer.constant.BrokerMessageStatus;
import com.itihub.rabbit.producer.entity.BrokerMessage;
import com.itihub.rabbit.producer.mapper.BrokerMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

public class MessageStoreService {

    private BrokerMessageMapper brokerMessageMapper;

    public MessageStoreService(BrokerMessageMapper brokerMessageMapper) {
        this.brokerMessageMapper = brokerMessageMapper;
    }

    public int insert(BrokerMessage brokerMessage){
        return brokerMessageMapper.insert(brokerMessage);
    }

    public BrokerMessage selectByMessageId(String messageId){
        return brokerMessageMapper.selectByPrimaryKey(messageId);
    }

    public void success(String messageId) {
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_OK.getCode(), new Date());
    }

    public void failure(String messageId){
        brokerMessageMapper.changeBrokerMessageStatus(messageId, BrokerMessageStatus.SEND_FAIL.getCode(), new Date());
    }

    public List<BrokerMessage> fetchTimeOutMessage4Retry(BrokerMessageStatus status) {
        return brokerMessageMapper.queryBrokerMessageStatus4Timeout(status.getCode());
    }

    public int updatedTryCount(String messageId){
        return brokerMessageMapper.update4TryCount(messageId, new Date());
    }
}
