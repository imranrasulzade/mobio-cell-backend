package com.example.mspackage.service.impl;

import com.example.mspackage.entity.NumbersPackage;
import com.example.mspackage.entity.Package;
import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.exception.NotFoundException;
import com.example.mspackage.repositories.NumbersPackageRepository;
import com.example.mspackage.repositories.PackageRepository;
import com.example.mspackage.service.NumbersPackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void addDefaultPackageForNumber(Integer numberId) {
        log.info("addDefaultPackageForNumber start numberId:{}", numberId);
        Package packageEntity = packageRepository.findById(1L)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.PACKAGE_NOT_FOUND));
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
}
