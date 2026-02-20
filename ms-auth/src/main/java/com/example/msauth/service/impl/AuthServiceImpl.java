package com.example.msauth.service.impl;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.client.NumberClient;
import com.example.msauth.client.UserProfileClient;
import com.example.msauth.dto.UserProfileDto;
import com.example.msauth.entity.User;
import com.example.msauth.enums.ExceptionCode;
import com.example.msauth.exception.AlreadyExistsException;
import com.example.msauth.exception.InvalidRequestException;
import com.example.msauth.exception.NotFoundException;
import com.example.msauth.request.PhoneNumberRequest;
import com.example.msauth.request.SignInRequest;
import com.example.msauth.request.SignUpRequest;
import com.example.msauth.response.UserResponse;
import com.example.msauth.service.AuthService;
import com.example.msauth.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserProfileClient userProfileClient;
    private final NumberClient numberClient;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, UserProfileClient userProfileClient, NumberClient numberClient,
                           AuthenticationManager authenticationManager, JwtService jwtService, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userProfileClient = userProfileClient;
        this.numberClient = numberClient;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse<?> signIn(SignInRequest request, String lang) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getPhone(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByPhone(request.getPhone())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.USER_NOT_FOUND));

        String token = jwtService.createToken(user);
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("userId", user.getId());
        response.put("phone", user.getPhone());

        return ApiResponse.success(response);
    }


    @Override
    public ApiResponse<UserResponse> signUp(SignUpRequest request, String lang) {
        log.info("signUp request: {}", request);
        validateRequest(request);
        String normalizedPhone = request.getPhone().trim();

        if (userRepository.existsByPhone(normalizedPhone)) {
            throw new AlreadyExistsException(ExceptionCode.USER_ALREADY_EXISTS);
        }

        User user = User.builder()
                .phone(normalizedPhone)
                .password(passwordEncoder.encode(request.getPassword()))
                .status(0)
                .role(resolveRole(request.getRoleId()))
                .build();

        User saved = userRepository.save(user);
        boolean profileCreated = false;
        boolean numberCreated = false;

        try {
            UserProfileDto profileDto = buildProfileDto(request, saved.getId());
            ensureSuccess(userProfileClient.addNew(lang, profileDto));
            profileCreated = true;

            PhoneNumberRequest phoneDto = buildPhoneNumberRequest(normalizedPhone, saved.getId());
            ensureSuccess(numberClient.addPhoneForUser(lang, phoneDto));
            numberCreated = true;

            saved.setStatus(1); // active
            userRepository.save(saved);
            log.info("User created: {}", saved);
            return ApiResponse.success(new UserResponse(saved.getId(), saved.getPhone(), request.getRoleId()));

        } catch (RuntimeException ex) {
            compensateUserCreation(saved.getId(), lang, profileCreated, numberCreated);
            userRepository.deleteById(saved.getId());
            throw ex;
        }
    }

    private void validateRequest(SignUpRequest request) {
        if (!Objects.equals(request.getPassword(), request.getConfirmPassword())) {
            throw new InvalidRequestException(ExceptionCode.PASSWORD_CANNOT_MATCH);
        }
    }

    private UserProfileDto buildProfileDto(SignUpRequest request, Long userId) {
        return UserProfileDto.builder()
                .email(request.getEmail())
                .birthDate(request.getBirthDate())
                .lastName(request.getLastName())
                .firstName(request.getFirstName())
                .userId(userId)
                .roleId(request.getRoleId())
                .build();
    }

    private PhoneNumberRequest buildPhoneNumberRequest(String phone, Long userId) {
        PhoneNumberRequest dto = new PhoneNumberRequest();
        dto.setNumber(phone);
        dto.setStatus(1);
        dto.setIsMain(1);
        dto.setUserId(userId);
        return dto;
    }

    private void ensureSuccess(ApiResponse<?> response) {
        if (response == null || !response.isSuccess()) {
            String message = (response != null && response.getMessage() != null)
                    ? response.getMessage()
                    : "Remote service error";
            throw new IllegalStateException(message);
        }
    }

    private String resolveRole(Integer roleId) {
        return roleId != null && roleId == 1 ? "ADMIN" : "USER";
    }

    private void compensateUserCreation(Long userId, String lang, boolean profileCreated, boolean numberCreated) {
        if (numberCreated) {
            try {
                numberClient.deleteByUserId(lang, userId);
            } catch (RuntimeException compensationEx) {
                log.error("Number compensation failed for userId={}", userId, compensationEx);
            }
        }
        if (profileCreated) {
            try {
                userProfileClient.deleteByUserId(lang, userId);
            } catch (RuntimeException compensationEx) {
                log.error("Profile compensation failed for userId={}", userId, compensationEx);
            }
        }
    }
}
