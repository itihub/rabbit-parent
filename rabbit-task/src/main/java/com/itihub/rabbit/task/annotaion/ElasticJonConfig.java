package com.itihub.rabbit.task.annotaion;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ElasticJon 注解配置
 * @author JiZhe
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJonConfig {

    /**
     * 是否流式处理数据
     */
    boolean streamingProcess() default false;

    /**
     * 脚本型作业执行命令行
     */
    String scriptCommandLine() default "";

    /**
     * JobCoreConfig 相关配置
     */
//    @AliasFor(
//            annotation = JobCoreConfiguration.class
//    )
    JobCoreConfiguration coreConfig();

    /**
     * LiteJobConfig 相关配置
     */
//    @AliasFor(
//            annotation = LiteJobConfiguration.class
//    )
    LiteJobConfiguration liteJobConfig();

//    @AliasFor(
//            annotation = ListenerConfiguration.class
//    )
    ListenerConfiguration listenerConfig();
}
