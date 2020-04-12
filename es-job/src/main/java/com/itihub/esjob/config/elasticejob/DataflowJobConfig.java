package com.itihub.esjob.config.elasticejob;

import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.itihub.esjob.task.MyDataflowJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataflowJobConfig {

    @Autowired
    private ZookeeperRegistryCenter registryCenter;

    @Autowired
    private JobEventConfiguration jobEventConfiguration;

    @Bean
    public DataflowJob dataflowJob(){
        return new MyDataflowJob();
    }

    @Bean(initMethod = "init")
    public JobScheduler dataflowJobScheduler(final DataflowJob dataflowJob,
                                                @Value("${dataflow-job.core.cron}") final String cron,
                                                @Value("${dataflow-job.core.shardingTotalCount}") final int shardingTotalCount,
                                                @Value("${dataflow-job.core.shardingItemParameters}") final String shardingItemParameters){
        return new SpringJobScheduler(dataflowJob, registryCenter
                , getLiteJobConfiguration(dataflowJob.getClass(), cron, shardingTotalCount, shardingItemParameters)
                , jobEventConfiguration);
    }

    private LiteJobConfiguration getLiteJobConfiguration(Class<? extends DataflowJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters){

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(jobClass.getName(), cron, shardingTotalCount)
                .shardingItemParameters(shardingItemParameters).build();

        DataflowJobConfiguration dataflowJobConfiguration = new DataflowJobConfiguration(jobCoreConfiguration
                , jobClass.getCanonicalName()
                // 流式处理 不适用cron表达式
                , true);

        return LiteJobConfiguration.newBuilder(dataflowJobConfiguration).build();
    }

}
