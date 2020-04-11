package com.itihub.rabbit.producer.config.database;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

/**
 * $BrokerMessageTableConfiguration 进行数据库表的创建
 */
@Configuration
public class BrokerMessageTableConfiguration {

    @Autowired
    private DataSource rabbitProducerDataSource;

    /**
     * 加载sql文件
     */
    @Value("classpath:rabbit-producer-message-schema.sql")
    private Resource resource;

    @Bean
    public DataSourceInitializer initDataSourceInitializer(){
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(rabbitProducerDataSource);
        initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }

    /**
     * 数据填充器
     * @return
     */
    private DatabasePopulator databasePopulator(){
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(resource);
        return populator;
    }


}
