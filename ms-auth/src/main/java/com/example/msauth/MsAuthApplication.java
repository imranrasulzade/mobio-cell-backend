package com.example.msauth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableFeignClients
@RequiredArgsConstructor
public class MsAuthApplication implements CommandLineRunner {

    private final BCryptPasswordEncoder encoder;

    public static void main(String[] args) {
        SpringApplication.run(MsAuthApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(encoder.encode("+994107133033"));
    }
}
