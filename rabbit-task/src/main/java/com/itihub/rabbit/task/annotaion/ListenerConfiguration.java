package com.itihub.rabbit.task.annotaion;

import com.dangdang.ddframe.job.lite.api.listener.AbstractDistributeOnceElasticJobListener;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ListenerConfiguration 作业监听配置
 * @author Jizhe
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerConfiguration {

    /**
     * 前置后置任务分布式监听实现类，需继承AbstractDistributeOnceElasticJobListener类
     */
//    Class<? extends AbstractDistributeOnceElasticJobListener> clazz() default ;
//    Class clazz();

    String clazz() default "";

    String distributedListener() default "";

    /**
     * 最后一个作业执行前的执行方法的超时时间
     * 单位：毫秒
     */
    long startedTimeoutMilliseconds() default Long.MAX_VALUE;	//must

    /**
     * 最后一个作业执行后的执行方法的超时时间
     * 单位：毫秒
     */
    long completedTimeoutMilliseconds() default Long.MAX_VALUE;		//must
}
