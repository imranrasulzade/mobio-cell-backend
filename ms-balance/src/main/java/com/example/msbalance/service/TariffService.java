package com.example.msbalance.service;

import com.example.msbalance.client.PackageClient;
import com.example.msbalance.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TariffService {

    private final PackageClient packageClient;

    @Cacheable(cacheNames = "activeTariff", key = "#numberId")
    public PackageClient.ActiveTariffPayload getActiveTariff(Integer numberId) {
        var response = packageClient.getActiveTariff(numberId);
        if (response == null || !response.isSuccess() || response.getData() == null) {
            throw new NotFoundException("Active tariff not found for numberId=" + numberId);
        }
        return response.getData();
    }
}
