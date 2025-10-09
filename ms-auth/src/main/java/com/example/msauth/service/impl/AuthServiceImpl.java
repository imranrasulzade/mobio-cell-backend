package com.example.msauth.service.impl;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.client.NumberClient;
import com.example.msauth.client.UserProfileClient;
import com.example.msauth.dto.UserProfileDto;
import com.example.msauth.entity.User;
import com.example.msauth.enums.ExceptionCode;
import com.example.msauth.exception.AlreadyExistsException;
import com.example.msauth.exception.InvalidRequestException;
import com.example.msauth.request.PhoneNumberRequest;
import com.example.msauth.request.SignInRequest;
import com.example.msauth.request.SignUpRequest;
import com.example.msauth.response.UserResponse;
import com.example.msauth.service.AuthService;
import com.example.msauth.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserProfileClient userProfileClient;
    private final NumberClient numberClient;

    public AuthServiceImpl(UserRepository userRepository, UserProfileClient userProfileClient, NumberClient numberClient) {
        this.userRepository = userRepository;
        this.userProfileClient = userProfileClient;
        this.numberClient = numberClient;
    }

    @Override
    public ApiResponse<?> signIn(SignInRequest request, String lang) {
        return null;
    }

    @Override
    public ApiResponse<?> signUp(SignUpRequest request, String lang) {
        try {
            if (!request.getPassword().equals(request.getConfirmPassword())){
                throw new InvalidRequestException(ExceptionCode.PASSWORD_CANNOT_MATCH);
            }
            Boolean exists = userRepository.existsByPhone(request.getPhone());
            if (exists) {
                throw new AlreadyExistsException(ExceptionCode.USER_ALREADY_EXISTS);
            }
            User user = User.builder()
                    .phone(request.getPhone())
                    .password(request.getPassword()) // TODO encode pass
                    .status(0)
                    .build();
            User saved = userRepository.save(user);
            UserProfileDto profile = UserProfileDto.builder()
                    .email(request.getEmail())
                    .birthDate(request.getBirthDate())
                    .lastName(request.getLastName())
                    .firstName(request.getFirstName())
                    .userId(saved.getId())
                    .roleId(request.getRoleId())
                    .build();
            ApiResponse<?> userProfileResponse = userProfileClient.addNew(lang,  profile);
            if (!userProfileResponse.isSuccess()) {
                return userProfileResponse;
            }
            PhoneNumberRequest numberRequest = new PhoneNumberRequest();
            numberRequest.setNumber(request.getPhone());
            numberRequest.setStatus(1);
            numberRequest.setIsMain(1);
            numberRequest.setUserId(saved.getId());
            ApiResponse<?> numberResponse = numberClient.addPhoneForUser(lang, numberRequest);
            if (!numberResponse.isSuccess()) {
                return numberResponse;
            }
            saved.setStatus(1);
            var updated = userRepository.save(saved);
            return ApiResponse.success(new UserResponse(updated.getId(), updated.getPhone(), profile.getRoleId()));
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
