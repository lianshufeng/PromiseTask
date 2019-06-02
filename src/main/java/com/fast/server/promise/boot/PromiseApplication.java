package com.fast.server.promise.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan("com.fast.server.promise.core")
@SpringBootApplication
public class PromiseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PromiseApplication.class, args);
    }

}
