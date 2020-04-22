package com.itihub.rabbit.producer.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.itihub.rabbit.producer.broker.RabbitBroker;
import com.itihub.rabbit.producer.constant.BrokerMessageStatus;
import com.itihub.rabbit.producer.entity.BrokerMessage;
import com.itihub.rabbit.producer.service.MessageStoreService;
import com.itihub.rabbit.task.annotaion.ElasticJobConfig;
import com.itihub.rabbit.task.annotaion.JobCoreConfiguration;
import com.itihub.rabbit.task.annotaion.LiteJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ElasticJobConfig(
        coreConfig = @JobCoreConfiguration(
            name = "com.itihub.rabbit.producer.task.RetryMessageDataflowJob",
            cron = "0/10 * * * * ?",
            description = "可靠性投递消息补偿任务",
            shardingTotalCount = 1
        ),
        liteJobConfig = @LiteJobConfiguration(overwrite = true)

)
public class RetryMessageDataflowJob implements DataflowJob<BrokerMessage> {

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private RabbitBroker rabbitBroker;

    private static final int MAX_RETRY_COUNT = 3;

    /**
     * 获取需要重试投递消息的数据
     */
    @Override
    public List<BrokerMessage> fetchData(ShardingContext shardingContext) {
        List<BrokerMessage> brokerMessageList = messageStoreService.fetchTimeOutMessage4Retry(BrokerMessageStatus.SENDING);
        log.info("--------@@抓取数据集合,数量：{}", brokerMessageList.size());

        return brokerMessageList;
    }

    /**
     * 补偿投递消息
     * @param shardingContext
     * @param dataList
     */
    @Override
    public void processData(ShardingContext shardingContext, List<BrokerMessage> dataList) {
        dataList.forEach(brokerMessage -> {
            // 判断消息投递次数是否已经超过最大努力重试次数
            if (MAX_RETRY_COUNT <= brokerMessage.getTryCount()){
                messageStoreService.failure(brokerMessage.getMessageId());
                log.warn("消息最大努力重试最终失败，消息ID：{}", brokerMessage.getMessageId());
            }else {
                // 重试 更新try count字段
                messageStoreService.updatedTryCount(brokerMessage.getMessageId());
                rabbitBroker.reliantSend(brokerMessage.getMessage());
            }
        });
    }
}
