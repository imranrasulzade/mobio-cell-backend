package com.example.msnumber.controller;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.request.PhoneNumberRequest;
import com.example.msnumber.service.PhoneNumberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/phone-numbers")
@RequiredArgsConstructor
public class PhoneNumberController {
    private final PhoneNumberService phoneNumberService;

    @GetMapping("/{numberId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/phone-numbers/{numberId}', 'GET')")
    public ApiResponse<?> getById(@PathVariable Long numberId) {
        return phoneNumberService.findById(numberId);
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/phone-numbers/by-user/{userId}', 'GET')")
    public ApiResponse<?> getByUserId(@PathVariable Long userId) {
        return phoneNumberService.findByUserId(userId);
    }

    @PostMapping
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/phone-numbers', 'POST')")
    public ApiResponse<?> addPhoneForUser(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @Valid @RequestBody PhoneNumberRequest req) {
        return phoneNumberService.addPhoneForUser(req, lang);
    }

    @PostMapping("/me")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/phone-numbers/me', 'POST')")
    public ApiResponse<?> addMyNumber(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @RequestBody PhoneNumberRequest req,
            Authentication authentication) {
        Long userId = Long.valueOf(String.valueOf(authentication.getPrincipal()));
        req.setUserId(userId);
        if (req.getStatus() == null) {
            req.setStatus(1);
        }
        if (req.getIsMain() == null) {
            req.setIsMain(0);
        }
        return phoneNumberService.addPhoneForUser(req, lang);
    }

    @DeleteMapping("/by-user/{userId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/phone-numbers/by-user/{userId}', 'DELETE')")
    public ApiResponse<?> deleteByUserId(
            @RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
            @PathVariable Long userId) {
        return phoneNumberService.deleteByUserId(userId, lang);
    }
}
