package com.example.mspackage.service;

import com.example.mspackage.response.ActiveTariffResponse;

public interface NumbersPackageService {
    void addDefaultPackageForNumber(Integer numberId);
    ActiveTariffResponse getActiveTariff(Integer numberId);
}
