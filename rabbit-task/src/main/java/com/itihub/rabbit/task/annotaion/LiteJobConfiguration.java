package com.itihub.rabbit.task.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Jizhe
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface LiteJobConfiguration {

    /**
     * 监控作业运行时状态
     */
    boolean monitorExecution() default true;

    /**
     * 作业监控端口
     */
    int monitorPort() default -1;	//must

    /**
     * 最大允许的本机与注册中心的时间误差秒数
     * 如果时间误差超过配置秒数则作业启动时将抛异常配置为-1表示不校验时间误差
     */
    int maxTimeDiffSeconds() default -1;	//must

    /**
     * 作业分片策略实现类全路径默认使用平均分配策略
     */
    String jobShardingStrategyClass() default "";	//must

    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复 单位：分钟
     * @return
     */
    int reconcileIntervalMinutes() default 10;	//must

    /**
     * 是否覆盖Zookeeper配置
     * @return
     */
    boolean overwrite() default true;

    boolean disabled() default false;	//must

    /**
     * 事件跟踪Rdb数据源
     */
    String eventTraceRdbDataSource() default "";	//must




}
