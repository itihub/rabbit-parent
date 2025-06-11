# 使用 OpenJDK 作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 将项目的 jar 包复制到容器中
COPY target/rabbit-task-example-1.0-SNAPSHOT.jar app.jar

# 暴露应用端口
EXPOSE 8080

# 启动 Spring Boot 应用
ENTRYPOINT ["java", "-jar", "app.jar"]