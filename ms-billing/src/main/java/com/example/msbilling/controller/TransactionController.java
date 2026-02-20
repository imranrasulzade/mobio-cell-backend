package com.example.msbilling.controller;

import com.example.msbilling.repositories.TransactionRepository;
import com.example.msbilling.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionRepository transactionRepository;

    @GetMapping("/by-number/{numberId}")
    @PreAuthorize("hasAnyRole('ADMIN','INTERNAL')")
    public ApiResponse<?> getByNumber(@PathVariable Integer numberId,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size) {
        return ApiResponse.success(transactionRepository.findAllByNumberIdOrderByCreatedAtDesc(numberId, PageRequest.of(page, size)));
    }
}
