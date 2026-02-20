package com.example.msbalance.client;

import com.example.msbalance.response.ApiResponse;
import lombok.Data;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@FeignClient(value = "package-service", url = "http://localhost:8085")
public interface PackageClient {

    @GetMapping("/api/package/active/by-number/{numberId}")
    ApiResponse<ActiveTariffPayload> getActiveTariff(@PathVariable("numberId") Integer numberId);

    @Data
    class ActiveTariffPayload {
        private Integer numberId;
        private Long packageId;
        private String packageName;
        private BigDecimal minuteRate;
        private LocalDateTime expiresAt;
    }
}
