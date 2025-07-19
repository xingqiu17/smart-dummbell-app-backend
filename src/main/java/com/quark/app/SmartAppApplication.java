package com.quark.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication   // 扫描 com.quark.app.* 及子包
public class SmartAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartAppApplication.class, args);
    }
}
