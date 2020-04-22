
# Elastic Job Demo  
ElasticJob传统方式配置Job，使用手动配置，每加入一个新的Job都得手动进行配置。  

# Elastic Job 配置  
```com.itihub.esjob.config.elasticejob.ElasticJobRegistryCenterConfig.class```Elastic Job注册中心配置  
```com.itihub.esjob.config.elasticejob.ElasticJobEventConfig.class```Elastic Job作业事件追踪配置  

# 此项目演示了两种Job作业类型开发与手动配置:  
+ SimpleJob
```com.itihub.esjob.task.MySimpleJob.class```SimpleJob作业开发  
```com.itihub.esjob.config.elasticejob.MySimpleJobConfig.class```SimpleJob作业配置  
+ DataflowJob  
```com.itihub.esjob.task.MyDataflowJob.class```DataflowJob作业开发  
```com.itihub.esjob.config.elasticejob.DataflowJobConfig.class```DataflowJob作业配置  
