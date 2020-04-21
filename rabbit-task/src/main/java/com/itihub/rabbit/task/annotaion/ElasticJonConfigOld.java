package com.itihub.rabbit.task.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ElasticJonConfigOld {

    boolean streamingProcess() default false;

    String scriptCommandLine() default "";

    // elastic job 名称
    String name() default "";

    String cron() default "";

    int shardingTotalCount() default 1;

    String shardingItemParameters() default "";

    String jobParameter() default "";

    boolean failover() default false;

    boolean misfire() default true;

    String description() default "";

    boolean overwrite() default true;






    boolean monitorExecution() default false;

    int monitorPort() default -1;	//must

    int maxTimeDiffSeconds() default -1;	//must

    String jobShardingStrategyClass() default "";	//must

    int reconcileIntervalMinutes() default 10;	//must

    String eventTraceRdbDataSource() default "";	//must

    String listener() default "";	//must

    boolean disabled() default false;	//must

    String distributedListener() default "";

    long startedTimeoutMilliseconds() default Long.MAX_VALUE;	//must

    long completedTimeoutMilliseconds() default Long.MAX_VALUE;		//must

    String jobExceptionHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultJobExceptionHandler";

    String executorServiceHandler() default "com.dangdang.ddframe.job.executor.handler.impl.DefaultExecutorServiceHandler";
}
