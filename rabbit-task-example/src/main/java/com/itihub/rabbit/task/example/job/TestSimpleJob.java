package com.itihub.rabbit.task.example.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.itihub.rabbit.task.annotaion.ElasticJobConfig;
import com.itihub.rabbit.task.annotaion.JobCoreConfiguration;
import com.itihub.rabbit.task.annotaion.ListenerConfiguration;
import com.itihub.rabbit.task.annotaion.LiteJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ElasticJobConfig(
        coreConfig = @JobCoreConfiguration(name = "testSimpleJob", cron = "0/5 * * * * ?"
                , shardingTotalCount = 2, shardingItemParameters = "0=beijing,1=shanghai"
                , jobParameter = "source1=public,source2=private", failover = true
                , description = "test job"),
        liteJobConfig = @LiteJobConfiguration(monitorPort = 8889),
        listenerConfig = @ListenerConfiguration(clazz = "com.itihub.rabbit.task.example.listener.SimpleJobListener.class")
)
@Slf4j
public class TestSimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("============== TestSimpleJob");
    }
}
