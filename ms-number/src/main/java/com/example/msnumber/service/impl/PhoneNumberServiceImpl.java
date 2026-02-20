package com.example.msnumber.service.impl;

import com.example.msnumber.baseModels.ApiResponse;
import com.example.msnumber.entity.PhoneNumber;
import com.example.msnumber.enums.ExceptionCode;
import com.example.msnumber.exception.AlreadyExistsException;
import com.example.msnumber.exception.NotFoundException;
import com.example.msnumber.mapper.PhoneNumberMapper;
import com.example.msnumber.queue.OutboxService;
import com.example.msnumber.repositories.PhoneNumberRepository;
import com.example.msnumber.request.PhoneNumberRequest;
import com.example.msnumber.response.PhoneNumberResponse;
import com.example.msnumber.service.PhoneNumberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;
    private final PhoneNumberMapper phoneNumberMapper;
    private final OutboxService outboxService;

    @Override
    public ApiResponse<?> findById(Long numberId) {
        PhoneNumber phoneNumber = phoneNumberRepository.findById(numberId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.PHONE_NUMBER_NOT_FOUND));
        return ApiResponse.success(phoneNumberMapper.toResponse(phoneNumber));
    }

    @Override
    public ApiResponse<?> findByUserId(Long userId) {
        List<PhoneNumberResponse> numberList = phoneNumberRepository
                .findAllByUserIdAndStatusOrderByIsMainDesc(userId, 1)
                .stream().map(phoneNumberMapper::toResponse).toList();
        return ApiResponse.success(numberList);
    }

    @Override
    @Transactional
    public ApiResponse<?> addPhoneForUser(PhoneNumberRequest req, String lang) {
        log.info("addPhoneForUser: {}", req);
        String normalizedNumber = req.getNumber().trim();
        Boolean exists = phoneNumberRepository.existsByNumber(normalizedNumber);
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
        req.setNumber(normalizedNumber);
        var saved = phoneNumberRepository.save(phoneNumberMapper.toEntity(req));
        var payload = saved.getId();
        outboxService.enqueue(payload, "init_new.number", 1, String.valueOf(payload));
        outboxService.enqueue(payload, "default.package", 1, String.valueOf(payload));
        log.info("Outbox events queued for phoneNumberId={}", payload);
        return ApiResponse.success(phoneNumberMapper.toResponse(saved));
    }

    @Override
    @Transactional
    public ApiResponse<?> deleteByUserId(Long userId, String lang) {
        Long deletedCount = phoneNumberRepository.deleteByUserId(userId);
        log.info("deleteByUserId userId={}, deletedCount={}", userId, deletedCount);
        return ApiResponse.success("success", null);
    }


}
