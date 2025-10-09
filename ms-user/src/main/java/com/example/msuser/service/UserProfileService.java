package com.example.msuser.service;

import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.baseModels.ApiResponse;

public interface UserProfileService {
    ApiResponse<?> addNew(UserProfileDto dto, String lang);
    ApiResponse<?> getByUserId(Long userId, String lang);
    ApiResponse<?> updateUserProfile(UserProfileDto dto, String lang);
}
