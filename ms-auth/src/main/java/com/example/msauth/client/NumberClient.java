package com.example.msauth.client;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.request.PhoneNumberRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "number", url = "http://localhost:8083")
public interface NumberClient {

    @PostMapping("/api/phone-numbers")
    ApiResponse<?> addPhoneForUser(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @RequestBody PhoneNumberRequest req);
}
