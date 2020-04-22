package com.itihub.rabbit.task.annotaion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * LiteJobConfiguration属性详细说明
 * @author Jizhe
 */
@Target({})
@Retention(RetentionPolicy.RUNTIME)
public @interface LiteJobConfiguration {

    /**
     * 监控作业运行时状态
     * 每次作业执行时间和间隔时间均非常短的情况，建议不监控作业运行时状态以提升效率。因为是瞬时状态，所以无必要监控。请用户自行增加数据堆积监控。并且不能保证数据重复选取，应在作业中实现幂等性。
     * 每次作业执行时间和间隔时间均较长的情况，建议监控作业运行时状态，可保证数据不会重复选取。
     */
    boolean monitorExecution() default true;

    /**
     * 作业监控端口
     * 建议配置作业监控端口, 方便开发者dump作业信息。
     * 使用方法: echo “dump” | nc 127.0.0.1 9888
     */
    int monitorPort() default -1;	//must

    /**
     * 最大允许的本机与注册中心的时间误差秒数
     * 如果时间误差超过配置秒数则作业启动时将抛异常
     * 配置为-1表示不校验时间误差
     */
    int maxTimeDiffSeconds() default -1;	//must

    /**
     * 作业分片策略实现类全路径
     * 默认使用平均分配策略
     * 基于平均分配算法的分片策略:com.dangdang.ddframe.job.lite.api.strategy.impl.AverageAllocationJobShardingStrategy
     * 根据作业名的哈希值奇偶数决定IP升降序算法的分片策略:com.dangdang.ddframe.job.lite.api.strategy.impl.OdevitySortByNameJobShardingStrategy
     * 根据作业名的哈希值对服务器列表进行轮转的分片策略:com.dangdang.ddframe.job.lite.api.strategy.impl.RotateServerByNameJobShardingStrategy
     */
    String jobShardingStrategyClass() default "";	//must

    /**
     * 修复作业服务器不一致状态服务调度间隔时间，配置为小于1的任意值表示不执行修复
     * 单位：分钟
     */
    int reconcileIntervalMinutes() default 10;	//must

    /**
     * 是否覆盖Zookeeper上的Job相关配置
     */
    boolean overwrite() default true;

    boolean disabled() default false;	//must

    /**
     * 作业事件追踪的数据源Bean引用
     */
    String eventTraceRdbDataSource() default "";	//must




}
