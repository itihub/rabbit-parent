package com.itihub.rabbit.task.example.listener;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.executor.ShardingContexts;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import lombok.extern.slf4j.Slf4j;

/**
 * ElasticJob自定义监听器
 */
@Slf4j
public class SimpleJobListener implements ElasticJobListener {
    @Override
    public void beforeJobExecuted(ShardingContexts shardingContexts) {
        log.info("----------- 执行任务之前{}", JSON.toJSONString(shardingContexts, true));
    }

    @Override
    public void afterJobExecuted(ShardingContexts shardingContexts) {
        log.info("----------- 执行任务之后{}", JSON.toJSONString(shardingContexts, true));
    }
}
