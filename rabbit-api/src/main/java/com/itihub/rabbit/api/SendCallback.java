package com.itihub.rabbit.api;

/**
 * $SendCallback 回调函数处理
 */
public interface SendCallback {

    void onSuccess();

    void onFailure();
}
