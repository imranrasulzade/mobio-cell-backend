package com.example.msbalance.service;

import com.example.msbalance.client.NumberClient;
import com.example.msbalance.client.PackageClient;
import com.example.msbalance.entity.Balance;
import com.example.msbalance.entity.BalanceHistory;
import com.example.msbalance.exception.InvalidRequestException;
import com.example.msbalance.exception.NotFoundException;
import com.example.msbalance.model.BaseEvent;
import com.example.msbalance.queue.EventPublisher;
import com.example.msbalance.repositories.BalanceHistoryRepository;
import com.example.msbalance.repositories.BalanceRepository;
import com.example.msbalance.request.BalanceTopUpRequest;
import com.example.msbalance.request.ConsumeMinutesRequest;
import com.example.msbalance.response.BalanceDetailsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;
    private final EventPublisher eventPublisher;
    private final TariffService tariffService;
    private final NumberClient numberClient;

    public BalanceService(BalanceRepository balanceRepository,
                          BalanceHistoryRepository balanceHistoryRepository,
                          EventPublisher eventPublisher,
                          TariffService tariffService,
                          NumberClient numberClient) {
        this.balanceRepository = balanceRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
        this.eventPublisher = eventPublisher;
        this.tariffService = tariffService;
        this.numberClient = numberClient;
    }

    @Transactional
    public void addInitialBalance(Integer numberId) {
        if (balanceRepository.existsByPhoneNumberId(numberId)) {
            log.info("Initial balance already exists for numberId={}, skipping duplicate event", numberId);
            return;
        }
        Balance balance = new Balance();
        balance.setPhoneNumberId(numberId);
        balance.setAmount(new BigDecimal("0.00"));
        balanceRepository.save(balance);
        writeHistory(numberId, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, "INIT", "Initial balance created", null);
        log.info("Added initial balance for numberId={}", numberId);
    }

    @Transactional(readOnly = true)
    public BalanceDetailsResponse getBalance(Integer numberId, Authentication authentication) {
        authorizeNumberAccess(numberId, authentication);
        Balance balance = requireBalance(numberId);
        return BalanceDetailsResponse.builder()
                .numberId(numberId)
                .amount(balance.getAmount())
                .lastUpdated(balance.getLastUpdated())
                .build();
    }

    @Transactional(readOnly = true)
    public Page<BalanceHistory> getHistory(Integer numberId, int page, int size, Authentication authentication) {
        authorizeNumberAccess(numberId, authentication);
        Pageable pageable = PageRequest.of(page, size);
        return balanceHistoryRepository.findAllByPhoneNumberIdOrderByCreatedAtDesc(numberId, pageable);
    }

    @Transactional
    public BalanceDetailsResponse topUp(Integer numberId, BalanceTopUpRequest request, Authentication authentication) {
        authorizeNumberAccess(numberId, authentication);
        Balance balance = requireBalance(numberId);
        BigDecimal changed = request.getAmount().setScale(2, RoundingMode.HALF_UP);
        BigDecimal oldAmount = balance.getAmount();
        BigDecimal newAmount = oldAmount.add(changed).setScale(2, RoundingMode.HALF_UP);
        balance.setAmount(newAmount);
        balanceRepository.save(balance);
        writeHistory(numberId, oldAmount, changed, newAmount, "TOPUP", request.getDescription(), null);
        publishChange(numberId, oldAmount, changed, newAmount, "TOPUP", request.getDescription(), null, null);
        return BalanceDetailsResponse.builder().numberId(numberId).amount(newAmount).lastUpdated(balance.getLastUpdated()).build();
    }

    @Transactional
    @CacheEvict(cacheNames = "activeTariff", key = "#numberId")
    public BalanceDetailsResponse consumeMinutes(Integer numberId, ConsumeMinutesRequest request, Authentication authentication) {
        authorizeNumberAccess(numberId, authentication);
        Balance balance = requireBalance(numberId);
        PackageClient.ActiveTariffPayload tariff = tariffService.getActiveTariff(numberId);
        BigDecimal minuteRate = tariff.getMinuteRate();
        if (minuteRate == null || minuteRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRequestException("Minute rate is not configured for active package");
        }

        BigDecimal oldAmount = balance.getAmount();
        BigDecimal changed = minuteRate.multiply(BigDecimal.valueOf(request.getMinutes())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal newAmount = oldAmount.subtract(changed).setScale(2, RoundingMode.HALF_UP);
        if (newAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Insufficient balance");
        }

        balance.setAmount(newAmount);
        balanceRepository.save(balance);
        writeHistory(numberId, oldAmount, changed.negate(), newAmount, "MINUTE_CONSUME", request.getDescription(), null);
        publishChange(numberId, oldAmount, changed.negate(), newAmount, "MINUTE_CONSUME", request.getDescription(), request.getMinutes(), minuteRate);
        return BalanceDetailsResponse.builder().numberId(numberId).amount(newAmount).lastUpdated(balance.getLastUpdated()).build();
    }

    @Transactional
    public void deleteBalance(Integer numberId) {
        long deleted = balanceRepository.deleteByPhoneNumberId(numberId);
        if (deleted == 0) {
            throw new NotFoundException("Balance not found");
        }
    }

    private Balance requireBalance(Integer numberId) {
        return balanceRepository.findByPhoneNumberId(numberId)
                .orElseThrow(() -> new NotFoundException("Balance not found for numberId=" + numberId));
    }

    private void writeHistory(Integer numberId, BigDecimal oldAmount, BigDecimal changedAmount, BigDecimal newAmount,
                              String operationType, String description, Integer transactionId) {
        BalanceHistory history = BalanceHistory.builder()
                .phoneNumberId(numberId)
                .oldAmount(oldAmount)
                .changedAmount(changedAmount)
                .newAmount(newAmount)
                .operationType(operationType)
                .description(description)
                .transactionId(transactionId)
                .build();
        balanceHistoryRepository.save(history);
    }

    private void publishChange(Integer numberId, BigDecimal oldAmount, BigDecimal changedAmount, BigDecimal newAmount,
                               String operationType, String description, Integer minutes, BigDecimal minuteRate) {
        Map<String, Object> payload = Map.of(
                "numberId", numberId,
                "oldAmount", oldAmount,
                "changedAmount", changedAmount,
                "newAmount", newAmount,
                "operationType", operationType,
                "description", description == null ? "" : description,
                "minutes", minutes == null ? 0 : minutes,
                "minuteRate", minuteRate == null ? BigDecimal.ZERO : minuteRate
        );
        BaseEvent<Map<String, Object>> event = BaseEvent.of("balance.changed", 1, payload);
        eventPublisher.publishToBilling(event);
        eventPublisher.publishToNotification(event);
    }

    private void authorizeNumberAccess(Integer numberId, Authentication authentication) {
        if (authentication == null || authentication.getAuthorities() == null) {
            return;
        }
        Set<String> authorities = authentication.getAuthorities().stream().map(a -> a.getAuthority()).collect(java.util.stream.Collectors.toSet());
        if (authorities.contains("ROLE_ADMIN") || authorities.contains("ROLE_INTERNAL")) {
            return;
        }
        if (!authorities.contains("ROLE_USER")) {
            throw new InvalidRequestException("Unauthorized role");
        }
        Long currentUserId;
        try {
            currentUserId = Long.valueOf(String.valueOf(authentication.getPrincipal()));
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException("Cannot resolve current user");
        }

        var response = numberClient.getById(numberId);
        NumberClient.PhoneNumberPayload numberPayload = response != null ? response.getData() : null;
        if (numberPayload == null || numberPayload.getUserId() == null || !numberPayload.getUserId().equals(currentUserId)) {
            throw new InvalidRequestException("Number does not belong to current user");
        }
    }

}
