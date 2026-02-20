package com.example.mspackage.service.impl;

import com.example.mspackage.entity.NumbersPackage;
import com.example.mspackage.entity.Package;
import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.exception.NotFoundException;
import com.example.mspackage.repositories.NumbersPackageRepository;
import com.example.mspackage.repositories.PackageRepository;
import com.example.mspackage.response.ActiveTariffResponse;
import com.example.mspackage.service.NumbersPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Slf4j
@Service
public class NumbersPackageServiceImpl implements NumbersPackageService {
    private final PackageRepository packageRepository;
    private final NumbersPackageRepository numbersPackageRepository;

    public NumbersPackageServiceImpl(PackageRepository packageRepository, NumbersPackageRepository numbersPackageRepository) {
        this.packageRepository = packageRepository;
        this.numbersPackageRepository = numbersPackageRepository;
    }

    @Override
    @Transactional
    public void addDefaultPackageForNumber(Integer numberId) {
        log.info("addDefaultPackageForNumber start numberId:{}", numberId);
        Package packageEntity = packageRepository.findFirstByIsDefault(1)
                .orElseGet(this::createFallbackDefaultPackage);
        boolean exists = numbersPackageRepository
                .existsActivePackage(numberId, packageEntity.getId(), 1);
        if (exists) {
            log.info("Default package already assigned for numberId={}, skipping duplicate event", numberId);
            return;
        }
        NumbersPackage numbersPackage = new NumbersPackage();
        numbersPackage.setPhoneNumberId(numberId);
        numbersPackage.setJoinAt(LocalDateTime.now());
        numbersPackage.setAPackage(packageEntity);
        numbersPackage.setStatus(1);
        numbersPackage.setIsActive(1);
        numbersPackage.setExpiresAt(LocalDateTime.now().plusDays(packageEntity.getValidityDays()));
        numbersPackageRepository.save(numbersPackage);
        log.info("addDefaultPackageForNumber success for numberId={}", numberId);
    }

    @Override
    @Transactional(readOnly = true)
    public ActiveTariffResponse getActiveTariff(Integer numberId) {
        NumbersPackage numbersPackage = numbersPackageRepository.findActiveByPhoneNumber(numberId, LocalDateTime.now())
                .orElseThrow(() -> new NotFoundException(ExceptionCode.PACKAGE_NOT_FOUND));
        Package activePackage = numbersPackage.getAPackage();
        return ActiveTariffResponse.builder()
                .numberId(numberId)
                .packageId(activePackage.getId())
                .packageName(activePackage.getName())
                .minuteRate(activePackage.getMinuteRate())
                .expiresAt(numbersPackage.getExpiresAt())
                .build();
    }

    private Package createFallbackDefaultPackage() {
        log.warn("Default package not found. Creating fallback default package.");
        packageRepository.clearDefaultFlags();
        Package fallback = new Package();
        fallback.setName("Default Starter");
        fallback.setPrice(new BigDecimal("0.00"));
        fallback.setValidityDays(30);
        fallback.setMinuteRate(new BigDecimal("0.0500"));
        fallback.setIsDefault(1);
        return packageRepository.save(fallback);
    }
}
