package com.itihub.rabbit.task.parser;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.script.ScriptJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.JobTypeConfiguration;
import com.dangdang.ddframe.job.config.dataflow.DataflowJobConfiguration;
import com.dangdang.ddframe.job.config.script.ScriptJobConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.event.rdb.JobEventRdbConfiguration;
import com.dangdang.ddframe.job.executor.handler.JobProperties;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.itihub.rabbit.task.annotaion.ElasticJobConfig;
import com.itihub.rabbit.task.annotaion.ListenerConfiguration;
import com.itihub.rabbit.task.autoconfigure.ElasticJobZookeeperProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 自定义注解解析器
 */
public class ElasticJobConfigParser implements ApplicationListener<ApplicationReadyEvent> {

    private ElasticJobZookeeperProperties elasticJobZookeeperProperties;

    private ZookeeperRegistryCenter zookeeperRegistryCenter;


    public ElasticJobConfigParser(ElasticJobZookeeperProperties elasticJobZookeeperProperties, ZookeeperRegistryCenter zookeeperRegistryCenter) {
        this.elasticJobZookeeperProperties = elasticJobZookeeperProperties;
        this.zookeeperRegistryCenter = zookeeperRegistryCenter;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        ConfigurableApplicationContext applicationContext = applicationReadyEvent.getApplicationContext();

        // 获取 带@ElasticJonConfig 注解的beans
        try {
            Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(ElasticJobConfig.class);
            for (Iterator<?> iterator = beansWithAnnotation.values().iterator(); iterator.hasNext(); ) {
                Object elasticJobConfBean = iterator.next();
                Class<?> clazz = elasticJobConfBean.getClass();
                if (clazz.getName().indexOf("$") > 0) {
                    String clazzName = clazz.getName();
                    clazz = Class.forName(clazzName.substring(0, clazzName.indexOf("$")));
                }

                // 获取接口类型 用于判断是什么类型的Job
                String jobTypeName = null;
                for (Class<?> anInterface : clazz.getInterfaces()) {
                    if (ElasticJob.class.isAssignableFrom(anInterface)) {
                        jobTypeName = anInterface.getSimpleName();
                        break;
                    }
                }

                ElasticJobConfig annotation = clazz.getAnnotation(ElasticJobConfig.class);

                String scriptCommandLine = annotation.scriptCommandLine();
                boolean streamingProcess = annotation.streamingProcess();

                /**
                 * 一级配置
                 */
                com.itihub.rabbit.task.annotaion.JobCoreConfiguration jobCoreConfiguration = annotation.coreConfig();
                JobCoreConfiguration coreConfig = getJobCoreConfiguration(jobCoreConfiguration);

                /**
                 * 二级配置
                 * 根据Job类型配置JobTypeConfiguration
                 */
                JobTypeConfiguration jobTypeConfig = null;
                if (SimpleJob.class.isAssignableFrom(clazz)) {
                    jobTypeConfig = new SimpleJobConfiguration(coreConfig, clazz.getName());
                } else if (DataflowJob.class.isAssignableFrom(clazz)) {
                    jobTypeConfig = new DataflowJobConfiguration(coreConfig, clazz.getName(), streamingProcess);
                } else if (ScriptJob.class.isAssignableFrom(clazz)) {
                    jobTypeConfig = new ScriptJobConfiguration(coreConfig, scriptCommandLine);
                }

                /**
                 * 三级配置
                 */
                com.itihub.rabbit.task.annotaion.LiteJobConfiguration liteJobConfiguration = annotation.liteJobConfig();
                String eventTraceRdbDataSource = liteJobConfiguration.eventTraceRdbDataSource();
                LiteJobConfiguration liteJobConfig = getLiteJobConfiguration(liteJobConfiguration, jobTypeConfig);

                // 创建Spring的BeanDefinition用来创建一个SpringJobScheduler Bean
                BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                factory.setInitMethodName("init");  // 初始化方法声明
                factory.setScope("prototype");      // 模式 单例or多例

                // 添加Bean构造器所需参数
                if (!ScriptJob.class.isInstance(clazz)) {
                    // 1.判断非脚本类型的Job 将Job实例放入构造器参数中
                    factory.addConstructorArgValue(elasticJobConfBean);
                }
                // 2. 添加ZookeeperRegistryCenter
                factory.addConstructorArgValue(this.zookeeperRegistryCenter);
                // 3. 添加LiteJobConf
                factory.addConstructorArgValue(liteJobConfig);

                //	4.如果有eventTraceRdbDataSource 则也进行添加
                if (StringUtils.hasText(eventTraceRdbDataSource)) {
                    BeanDefinitionBuilder rdbFactory = BeanDefinitionBuilder.rootBeanDefinition(JobEventRdbConfiguration.class);
                    rdbFactory.addConstructorArgReference(eventTraceRdbDataSource);
                    factory.addConstructorArgValue(rdbFactory.getBeanDefinition());
                }

                //  5.添加监听
                ListenerConfiguration listenerConfig = annotation.listenerConfig();
                List<?> elasticJobListeners = getTargetElasticJobListeners(listenerConfig);
                factory.addConstructorArgValue(elasticJobListeners);

                // 	接下来就是把factory 也就是 SpringJobScheduler注入到Spring容器中
                DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();

                // 声明实例化Bean名称
                String registerBeanName = jobTypeName + "SpringJobScheduler";
                defaultListableBeanFactory.registerBeanDefinition(registerBeanName, factory.getBeanDefinition());
                SpringJobScheduler scheduler = (SpringJobScheduler) applicationContext.getBean(registerBeanName);
                // 初始化SpringJobScheduler
                scheduler.init();
            }
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private JobCoreConfiguration getJobCoreConfiguration(com.itihub.rabbit.task.annotaion.JobCoreConfiguration jobCoreConfiguration){
        String jobName = this.elasticJobZookeeperProperties.getNamespace() + "." + jobCoreConfiguration.name();
        String cron = jobCoreConfiguration.cron();
        String shardingItemParameters = jobCoreConfiguration.shardingItemParameters();
        String description = jobCoreConfiguration.description();
        String jobParameter = jobCoreConfiguration.jobParameter();
        boolean failover = jobCoreConfiguration.failover();
        boolean misfire = jobCoreConfiguration.misfire();
        int shardingTotalCount = jobCoreConfiguration.shardingTotalCount();
        String jobExceptionHandler = jobCoreConfiguration.jobExceptionHandler();
        String executorServiceHandler = jobCoreConfiguration.executorServiceHandler();
        /**
         * 一级配置
         */
        JobCoreConfiguration coreConfig = JobCoreConfiguration
                .newBuilder(jobName, cron, shardingTotalCount)
                .shardingItemParameters(shardingItemParameters)
                .description(description)
                .failover(failover)
                .jobParameter(jobParameter)
                .misfire(misfire)
                .jobProperties(JobProperties.JobPropertiesEnum.JOB_EXCEPTION_HANDLER.getKey(), jobExceptionHandler)
                .jobProperties(JobProperties.JobPropertiesEnum.EXECUTOR_SERVICE_HANDLER.getKey(), executorServiceHandler)
                .build();
        return coreConfig;
    }

    private LiteJobConfiguration getLiteJobConfiguration(com.itihub.rabbit.task.annotaion.LiteJobConfiguration liteJobConfiguration
            , JobTypeConfiguration jobTypeConfig){
        String jobShardingStrategyClass = liteJobConfiguration.jobShardingStrategyClass();
        boolean overwrite = liteJobConfiguration.overwrite();
        boolean disabled = liteJobConfiguration.disabled();
        boolean monitorExecution = liteJobConfiguration.monitorExecution();
        int monitorPort = liteJobConfiguration.monitorPort();
        int maxTimeDiffSeconds = liteJobConfiguration.maxTimeDiffSeconds();
        int reconcileIntervalMinutes = liteJobConfiguration.reconcileIntervalMinutes();

        LiteJobConfiguration liteJobConfig = LiteJobConfiguration
                .newBuilder(jobTypeConfig)
                .overwrite(overwrite)
                .disabled(disabled)
                .monitorExecution(monitorExecution)
                .monitorPort(monitorPort)
                .maxTimeDiffSeconds(maxTimeDiffSeconds)
                .jobShardingStrategyClass(jobShardingStrategyClass)
                .reconcileIntervalMinutes(reconcileIntervalMinutes)
                .build();
        return liteJobConfig;
    }

    private List<BeanDefinition> getTargetElasticJobListeners(com.itihub.rabbit.task.annotaion.ListenerConfiguration listenerConfiguration) {
        List<BeanDefinition> result = new ManagedList<BeanDefinition>(2);
        Class listeners = listenerConfiguration.clazz();
        if (null != listeners) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(listeners);
            factory.setScope("prototype");
            result.add(factory.getBeanDefinition());
        }

        String distributedListeners = listenerConfiguration.distributedListener();
        long startedTimeoutMilliseconds = listenerConfiguration.startedTimeoutMilliseconds();
        long completedTimeoutMilliseconds = listenerConfiguration.completedTimeoutMilliseconds();

        if (StringUtils.hasText(distributedListeners)) {
            BeanDefinitionBuilder factory = BeanDefinitionBuilder.rootBeanDefinition(distributedListeners);
            factory.setScope("prototype");
            factory.addConstructorArgValue(Long.valueOf(startedTimeoutMilliseconds));
            factory.addConstructorArgValue(Long.valueOf(completedTimeoutMilliseconds));
            result.add(factory.getBeanDefinition());
        }
        return result;
    }

}
