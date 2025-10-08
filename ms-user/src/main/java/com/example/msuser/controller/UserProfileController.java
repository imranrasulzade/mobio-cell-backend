package com.example.msuser.controller;

import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.response.ApiResponse;
import com.example.msuser.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-profiles")
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/{userId}")
    ApiResponse<?> getByUserId(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) String lang,
                               @PathVariable Long userId) {
        return userProfileService.getByUserId(userId, lang);
    }

    @PostMapping
    ApiResponse<?> addNew(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE) String lang,
                          @RequestBody UserProfileDto dto) {
        return userProfileService.addNew(dto, lang);
    }
}
