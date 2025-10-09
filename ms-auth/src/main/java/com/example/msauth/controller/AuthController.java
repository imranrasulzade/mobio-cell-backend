package com.example.msauth.controller;

import com.example.msauth.baseModels.ApiResponse;
import com.example.msauth.request.SignInRequest;
import com.example.msauth.request.SignUpRequest;
import com.example.msauth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-in")
    public ApiResponse<?> signIn(@RequestHeader(name = "Accept-Language", defaultValue = "az") String lang,
                                 @RequestBody SignInRequest request) {
        return authService.signIn(request, lang);
    }

    @PostMapping("/sign-up")
    public ApiResponse<?> signUp(@RequestHeader(name = "Accept-Language", defaultValue = "az") String lang,
                                 @RequestBody SignUpRequest request) {
        return authService.signUp(request, lang);
    }
}
