package com.itihub.rabbit.task.annotaion;


import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElasticJob 作业注解配置
 *      作业配置分为3级，分别是JobCoreConfiguration，JobTypeConfiguration和LiteJobConfiguration。
 *      LiteJobConfiguration使用JobTypeConfiguration，JobTypeConfiguration使用JobCoreConfiguration，层层嵌套。
 *      JobTypeConfiguration根据不同实现类型分为SimpleJobConfiguration，DataflowJobConfiguration和ScriptJobConfiguration。
 * @author Jizhe
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJobConfig {

    /**
     * DataflowJob 作业配置属性
     * 是否流式处理数据
     * 如果流式处理数据, 则fetchData不返回空结果将持续执行作业
     * 如果非流式处理数据, 则处理数据完成后作业结束
     */
    boolean streamingProcess() default false;

    /**
     * ScriptJob 作业配置项
     * 脚本型作业执行命令行
     */
    String scriptCommandLine() default "";

    /**
     * JobCoreConfig 相关配置
     */
    @AliasFor(
            annotation = JobCoreConfiguration.class, attribute = "core"
    )
    JobCoreConfiguration coreConfig();

    /**
     * LiteJobConfig 相关配置
     */
    @AliasFor(
            annotation = LiteJobConfiguration.class, attribute = "lite"
    )
    LiteJobConfiguration liteJobConfig() default @LiteJobConfiguration;

    /**
     * 作业监听配置
     */
    @AliasFor(
            annotation = ListenerConfiguration.class, attribute = "listener"
    )
    ListenerConfiguration listenerConfig() default @ListenerConfiguration;
}
