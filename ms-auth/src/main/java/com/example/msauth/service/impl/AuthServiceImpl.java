package com.example.msauth.service.impl;

import com.example.msauth.dto.UserProfileDto;
import com.example.msauth.entity.User;
import com.example.msauth.request.SignInRequest;
import com.example.msauth.request.SignUpRequest;
import com.example.msauth.response.ApiResponse;
import com.example.msauth.service.AuthService;
import com.example.msauth.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    public AuthServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ApiResponse<?> signIn(SignInRequest request, String lang) {
        return null;
    }

    @Override
    public ApiResponse<?> signUp(SignUpRequest request, String lang) {
        ApiResponse<?> response = new ApiResponse<>();
        try {
            User user = User.builder()
                    .phone(request.getPhone())
                    .password(request.getPassword()) // TODO encode pass
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
            userProfileClient.

        }
    }
}
