package com.itihub.esjob.config.elasticejob;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.itihub.esjob.listener.SimpleJobListener;
import com.itihub.esjob.task.MySimpleJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySimpleJobConfig {

    @Autowired
    private ZookeeperRegistryCenter registryCenter;

    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    /**
     * 具体定时任务执行逻辑
     * @return
     */
    @Bean
    public SimpleJob simpleJob(){
        return new MySimpleJob();
    }


    /**
     * ElasticJob 与Spring集成配置
     * @param simpleJob
     * @return
     */
    @Bean(initMethod = "init")
    public JobScheduler simpleJobScheduler(final SimpleJob simpleJob,
                                           @Value("${simple-job.core.cron}") final String core,
                                           @Value("${simple-job.core.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${simple-job.core.shardingItemParameters}") final String shardingItemParameters,
                                           @Value("${simple-job.core.jobParameter}") final String jobParameter,
                                           @Value("${simple-job.core.failover}") final boolean failover,
                                           @Value("${simple-job.lite.monitorExecution}") final boolean monitorExecution,
                                           @Value("${simple-job.lite.monitorPort}") final int monitorPort,
                                           @Value("${simple-job.lite.maxTimeDiffSeconds}") final int maxTimeDiffSeconds,
                                           @Value("${simple-job.lite.jobShardingStrategyClass}") final String jobShardingStrategyClass,
                                           @Value("${simple-job.lite.overwrite}") final boolean overwrite){
        LiteJobConfiguration liteJobConfiguration
                = getLiteJobConfiguration(simpleJob.getClass()
                                        , core
                                        , shardingTotalCount
                                        , failover
                                        , jobParameter
                                        , jobShardingStrategyClass
                                        , monitorExecution
                                        , monitorPort
                                        , maxTimeDiffSeconds
                                        , jobShardingStrategyClass
                                        , overwrite);
        return new SpringJobScheduler(simpleJob, registryCenter
                , liteJobConfiguration
                , jobEventConfiguration
                , new SimpleJobListener());
    }


    public LiteJobConfiguration getLiteJobConfiguration(Class<? extends SimpleJob> jobClass,
                                                        String corn,
                                                        int shardingTotalCount,
                                                        boolean failover,
                                                        String jobParameter,
                                                        String shardingItemParameters,
                                                        boolean monitorExecution,
                                                        int monitorPort,
                                                        int maxTimeDiffSeconds,
                                                        String jobShardingStrategyClass,
                                                        boolean overwrite){

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration
                .newBuilder(jobClass.getName(), corn, shardingTotalCount)
                .misfire(true)
                .failover(failover)
                .jobParameter(jobParameter)
                .shardingItemParameters(shardingItemParameters)
                .build();
        SimpleJobConfiguration simpleJobConfiguration = new SimpleJobConfiguration(jobCoreConfiguration
                , jobClass.getCanonicalName());
        LiteJobConfiguration liteJobConfiguration = LiteJobConfiguration
                .newBuilder(simpleJobConfiguration)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .overwrite(overwrite)
                .build();

        return liteJobConfiguration;
    }
}
