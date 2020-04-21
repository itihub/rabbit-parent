package com.itihub.rabbit.example.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.itihub.rabbit.example.listener.SimpleJobListener;
import com.itihub.rabbit.task.annotaion.ElasticJonConfig;
import com.itihub.rabbit.task.annotaion.JobCoreConfiguration;
import com.itihub.rabbit.task.annotaion.ListenerConfiguration;
import com.itihub.rabbit.task.annotaion.LiteJobConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@ElasticJonConfig(
        coreConfig = @JobCoreConfiguration(name = "testSimpleJob", cron = "0/5 * * * * ?"
                , shardingTotalCount = 2, shardingItemParameters = "0=beijing,1=shanghai"
                , jobParameter = "source1=public,source2=private", failover = true
                , description = "test job"),
        liteJobConfig = @LiteJobConfiguration(monitorPort = 8889),
        listenerConfig = @ListenerConfiguration(clazz = SimpleJobListener.class)
)
@Slf4j
public class TestSimpleJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("============== TestSimpleJob");
    }
}
