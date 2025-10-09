package com.example.msnumber.service.impl;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.entity.PhoneNumber;
import com.example.msnumber.enums.ExceptionCode;
import com.example.msnumber.exception.AlreadyExistsException;
import com.example.msnumber.mapper.PhoneNumberMapper;
import com.example.msnumber.repositories.PhoneNumberRepository;
import com.example.msnumber.request.PhoneNumberRequest;
import com.example.msnumber.response.PhoneNumberResponse;
import com.example.msnumber.service.PhoneNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;
    private final PhoneNumberMapper phoneNumberMapper;

    @Override
    public ApiResponse<?> findByUserId(Long userId) {
        List<PhoneNumberResponse> numberList = phoneNumberRepository
                .findAllByUserIdAndStatusOrderByIsMainDesc(userId, 1)
                .stream().map(phoneNumberMapper::toResponse).toList();
        return ApiResponse.success(numberList);
    }

    @Override
    public ApiResponse<?> addPhoneForUser(PhoneNumberRequest req, String lang) {
        Boolean exists = phoneNumberRepository.existsByNumber(req.getNumber());
        if (exists) {
            throw new AlreadyExistsException(ExceptionCode.PHONE_NUMBER_ALREADY_EXISTS);
        }
        if (req.getIsMain() == 1) {
            Optional<PhoneNumber> existingMainNumber = phoneNumberRepository
                    .findByUserIdAndIsMain(req.getUserId(),  1);
            existingMainNumber.ifPresent(phoneNumber -> {
                phoneNumber.setIsMain(0);
                phoneNumberRepository.save(phoneNumber);
            });
        }
        var saved = phoneNumberRepository.save(phoneNumberMapper.toEntity(req));
        return ApiResponse.success(phoneNumberMapper.toResponse(saved));
    }


}
