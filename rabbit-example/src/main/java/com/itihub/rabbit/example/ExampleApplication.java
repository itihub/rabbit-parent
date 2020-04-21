package com.itihub.rabbit.example;

import com.itihub.rabbit.task.annotaion.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EnableElasticJob
//@ComponentScan(basePackages = {"com.itihub.rabbit.task.*"})
public class ExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }
}
