package com.example.msuser.service.impl;

import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.entity.UserProfile;
import com.example.msuser.enums.ExceptionCode;
import com.example.msuser.exception.AlreadyExistsException;
import com.example.msuser.exception.NotFoundException;
import com.example.msuser.mapper.UserProfileMapper;
import com.example.msuser.repositories.UserProfileRepository;
import com.example.msuser.baseModels.ApiResponse;
import com.example.msuser.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileMapper userProfileMapper;
    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileMapper userProfileMapper, UserProfileRepository userProfileRepository) {
        this.userProfileMapper = userProfileMapper;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public ApiResponse<?> addNew(UserProfileDto dto, String lang) {
        var exists = userProfileRepository.existsByUserId((dto.getUserId()));
        if (exists) {
            throw new AlreadyExistsException(ExceptionCode.USER_PROFILE_ALREADY_EXISTS);
        }
        UserProfile userProfile = userProfileMapper.toEntity(dto);
        var saved = userProfileRepository.save(userProfile);
        Map<String, Long> map = new HashMap<>();
        map.put("id", saved.getId());
        return ApiResponse.success(map);
    }

    @Override
    public ApiResponse<?> getByUserId(Long userId, String lang) {
        UserProfile userProfile = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_PROFILE_NOT_FOUND));
        UserProfileDto userProfileDto = userProfileMapper.toDto(userProfile);
        return ApiResponse.success(userProfileDto);
    }

    @Override
    @Transactional
    public ApiResponse<?> updateUserProfile(UserProfileDto dto, String lang) {
        UserProfile existing = userProfileRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_PROFILE_NOT_FOUND));
        existing.setFirstName(dto.getFirstName());
        existing.setLastName(dto.getLastName());
        existing.setBirthDate(dto.getBirthDate());
        existing.setEmail(dto.getEmail());
        existing.setRoleId(dto.getRoleId());
        UserProfile saved = userProfileRepository.save(existing);
        return ApiResponse.success(userProfileMapper.toDto(saved));
    }

    @Override
    @Transactional
    public ApiResponse<?> deleteByUserId(Long userId, String lang) {
        UserProfile existing = userProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_PROFILE_NOT_FOUND));
        userProfileRepository.delete(existing);
        return ApiResponse.success("success", null);
    }
}
