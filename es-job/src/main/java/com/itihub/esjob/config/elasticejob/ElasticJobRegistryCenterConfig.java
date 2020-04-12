package com.itihub.esjob.config.elasticejob;

import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ElasticJob注册中心配置
 */
@Configuration
@ConditionalOnExpression("'${elastic-job.zookeeper.serverLists}'.length() > 0")
public class ElasticJobRegistryCenterConfig {

    /**
     * 将Zookeeper注册到Spring容器中
     * @return
     */
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter registryCenter(
            @Value("${elastic-job.zookeeper.serverLists}") final String serverLists
            , @Value("${elastic-job.zookeeper.namespace}") final String namespace
            , @Value("${elastic-job.zookeeper.connectionTimeoutMilliseconds}") final int connectionTimeoutMilliseconds
            , @Value("${elastic-job.zookeeper.sessionTimeoutMilliseconds}") final int sessionTimeoutMilliseconds
            , @Value("${elastic-job.zookeeper.maxRetries}") final int maxRetries){

        ZookeeperConfiguration configuration = new ZookeeperConfiguration(serverLists, namespace);
        configuration.setConnectionTimeoutMilliseconds(connectionTimeoutMilliseconds);
        configuration.setSessionTimeoutMilliseconds(sessionTimeoutMilliseconds);
        configuration.setMaxRetries(maxRetries);
        return new ZookeeperRegistryCenter(configuration);
    }
}
