package com.example.msuser.controller;

import com.example.msuser.dto.UserProfileDto;
import com.example.msuser.baseModels.ApiResponse;
import com.example.msuser.service.UserProfileService;
import jakarta.validation.Valid;
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
                          @Valid @RequestBody UserProfileDto dto) {
        return userProfileService.addNew(dto, lang);
    }

    @PutMapping
    ApiResponse<?> update(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
                          @Valid @RequestBody UserProfileDto dto) {
        return userProfileService.updateUserProfile(dto, lang);
    }

    @DeleteMapping("/by-user/{userId}")
    ApiResponse<?> deleteByUserId(@RequestHeader(value = HttpHeaders.ACCEPT_LANGUAGE, defaultValue = "az") String lang,
                                  @PathVariable Long userId) {
        return userProfileService.deleteByUserId(userId, lang);
    }
}
