package com.example.msnumber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MsNumberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsNumberApplication.class, args);
    }

}
