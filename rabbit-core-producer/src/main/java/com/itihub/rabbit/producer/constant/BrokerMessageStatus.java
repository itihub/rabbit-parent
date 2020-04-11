package com.itihub.rabbit.producer.constant;

/**
 * $BrokerMessageStatus 消息发送状态
 */
public enum BrokerMessageStatus {

    SENDING("0"),
    SEND_OK("1"),
    SEND_FAIL("2"),
    SEND_FAIL_A_MOMENT("3"),
    ;

    private String code;

    BrokerMessageStatus(String code){
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
