package com.itihub.esjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackages = {"com.itihub.esjob.*",
        "com.itihub.esjob.service.*",
      "com.itihub.esjob.task.*"}
)
public class EsJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(EsJobApplication.class, args);
    }
}
