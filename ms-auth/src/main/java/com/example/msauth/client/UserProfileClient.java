package com.example.msauth.client;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.dto.UserProfileDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(value = "user-profile", url = "http://localhost:8082")
public interface UserProfileClient {

    @PostMapping("/api/user-profiles")
    ApiResponse<?> addNew(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @RequestBody UserProfileDto dto);

    @DeleteMapping("/api/user-profiles/by-user/{userId}")
    ApiResponse<?> deleteByUserId(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @PathVariable("userId") Long userId);


}
