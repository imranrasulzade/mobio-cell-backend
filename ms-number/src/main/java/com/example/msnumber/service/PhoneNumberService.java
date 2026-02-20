package com.example.msnumber.service;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.request.PhoneNumberRequest;

public interface PhoneNumberService {
    ApiResponse<?> findById(Long numberId);
    ApiResponse<?> findByUserId(Long userId);
    ApiResponse<?> addPhoneForUser(PhoneNumberRequest request, String lang);
    ApiResponse<?> deleteByUserId(Long userId, String lang);
}
