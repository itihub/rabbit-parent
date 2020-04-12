package com.itihub.esjob.config.elasticejob;

import com.dangdang.ddframe.job.event.JobEventConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * ElasticJob 事件配置
 * 记录时间追踪到DB
 */
@Configuration
public class ElasticJobEventConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public JobEventConfiguration jobEventConfiguration(){
        return new JobEventRdbConfiguration(dataSource);
    }
}
