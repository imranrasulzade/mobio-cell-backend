package com.example.msbalance.controller;

import com.example.msbalance.response.ApiResponse;
import com.example.msbalance.request.BalanceTopUpRequest;
import com.example.msbalance.request.ConsumeMinutesRequest;
import com.example.msbalance.service.BalanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/balances")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{numberId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/balances/{numberId}', 'GET')")
    public ApiResponse<?> getBalance(@PathVariable Integer numberId, Authentication authentication) {
        return ApiResponse.success(balanceService.getBalance(numberId, authentication));
    }

    @GetMapping("/{numberId}/history")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/balances/{numberId}/history', 'GET')")
    public ApiResponse<?> getHistory(@PathVariable Integer numberId,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "20") int size,
                                     Authentication authentication) {
        return ApiResponse.success(balanceService.getHistory(numberId, page, size, authentication));
    }

    @PostMapping("/{numberId}/topup")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/balances/{numberId}/topup', 'POST')")
    public ApiResponse<?> topUp(@PathVariable Integer numberId,
                                @Valid @RequestBody BalanceTopUpRequest request,
                                Authentication authentication) {
        return ApiResponse.success(balanceService.topUp(numberId, request, authentication));
    }

    @PostMapping("/{numberId}/consume-minutes")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/balances/{numberId}/consume-minutes', 'POST')")
    public ApiResponse<?> consumeMinutes(@PathVariable Integer numberId,
                                         @Valid @RequestBody ConsumeMinutesRequest request,
                                         Authentication authentication) {
        return ApiResponse.success(balanceService.consumeMinutes(numberId, request, authentication));
    }

    @DeleteMapping("/{numberId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/balances/{numberId}', 'DELETE')")
    public ApiResponse<?> deleteBalance(@PathVariable Integer numberId) {
        balanceService.deleteBalance(numberId);
        return ApiResponse.success("success", null);
    }
}
