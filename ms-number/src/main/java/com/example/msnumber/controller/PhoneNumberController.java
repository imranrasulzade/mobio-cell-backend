package com.example.msnumber.controller;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.request.PhoneNumberRequest;
import com.example.msnumber.service.PhoneNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone-numbers")
@RequiredArgsConstructor
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    @PostMapping
    public ApiResponse<?> addPhoneForUser(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @RequestBody PhoneNumberRequest req) {
        return phoneNumberService.addPhoneForUser(req, lang);
    }
}
