package com.example.msauth.service;

import com.example.msauth.request.SignInRequest;
import com.example.msauth.request.SignUpRequest;
import com.example.msauth.response.ApiResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthService {
    ApiResponse<?> signIn(@RequestBody SignInRequest request, String lang);

    ApiResponse<?> signUp(@RequestBody SignUpRequest request, String lang);
}
