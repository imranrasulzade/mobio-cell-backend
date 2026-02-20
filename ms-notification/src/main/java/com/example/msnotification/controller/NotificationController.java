package com.example.msnotification.controller;

import com.example.msnotification.repositories.NotificationRepository;
import com.example.msnotification.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;

    @GetMapping("/by-number/{numberId}")
    @PreAuthorize("hasAnyRole('ADMIN','INTERNAL')")
    public ApiResponse<?> getByNumber(@PathVariable Integer numberId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(notificationRepository.findAllByNumberIdOrderBySentAtDesc(numberId, PageRequest.of(page, size)));
    }
}
