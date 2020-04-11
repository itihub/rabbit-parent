package com.itihub.rabbit.producer.entity;

import com.itihub.rabbit.api.Message;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * $BrokerMessage 消息记录表实体映射
 */
@Data
public class BrokerMessage implements Serializable {

    private static final long serialVersionUID = 4207642000013702321L;

    private String messageId;

    private Message message;

    private Integer tryCount = 0;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;



}
