package com.itihub.rabbit.exception;

/**
 * Message运行时异常
 * @author Jizhe
 */
public class MessageRunTimeException extends RuntimeException {

    public MessageRunTimeException() {
        super();
    }

    public MessageRunTimeException(String message){
        super(message);
    }

    public MessageRunTimeException(String message, Throwable cause){
        super(message, cause);
    }

    public MessageRunTimeException(Throwable cause){
        super(cause);
    }
}
