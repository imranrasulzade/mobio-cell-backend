package com.example.msbalance.client;

import com.example.msbalance.response.ApiResponse;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "number-service", url = "http://localhost:8083")
public interface NumberClient {

    @GetMapping("/api/phone-numbers/{numberId}")
    ApiResponse<PhoneNumberPayload> getById(@PathVariable("numberId") Integer numberId);

    @Data
    class PhoneNumberPayload {
        private Long id;
        private String number;
        private Long userId;
        private Integer isMain;
    }
}
