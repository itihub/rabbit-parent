package com.itihub.rabbit.task.annotaion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jizhe
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface JobCoreConfiguration {

    /**
     * 作业名称
     */
    String name();

    /**
     * cron表达式，用于控制作业触发时间
     */
    String cron() default "";

    /**
     * 作业分片总数
     */
    int shardingTotalCount() default 1;

    /**
     * 分片序列号和参数用等号分隔，多个键值对用逗号分隔
     * 分片序列号从0开始，不可大于或等于作业分片总数 如:0=a,1=b,2=c
     */
    String shardingItemParameters() default "";

    /**
     * 作业自定义参数
     */
    String jobParameter() default "";

    /**
     * 是否开启任务执行失效转移，开启表示如果作业在一次任务执行中途宕机，允许将该次未完成的任务在另一作业节点上补偿执行
     */
    boolean failover() default false;

    /**
     * 是否开启错过任务重新执行
     */
    boolean misfire() default true;

    /**
     * 作业描述信息
     */
    String description() default "";

    /**
     * Job异常处理
     */
    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";


}
