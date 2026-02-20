package com.example.mspackage.service.impl;

import com.example.mspackage.baseModels.ApiResponse;
import com.example.mspackage.entity.Package;
import com.example.mspackage.enums.ExceptionCode;
import com.example.mspackage.exception.NotFoundException;
import com.example.mspackage.mapper.PackageMapper;
import com.example.mspackage.repositories.PackageRepository;
import com.example.mspackage.request.PackageRequest;
import com.example.mspackage.response.PackageResponse;
import com.example.mspackage.response.PageResponse;
import com.example.mspackage.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Slf4j
@Service
public class PackageServiceImpl implements PackageService {

    private final PackageRepository packageRepository;

    public PackageServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    @Transactional
    public ApiResponse<?> createPackage(PackageRequest packageRequest) {
        log.info("createPackage start");
        if (packageRequest.getIsDefault() != null && packageRequest.getIsDefault() == 1) {
            packageRepository.clearDefaultFlags();
        }
        Package packageEntity = PackageMapper.toEntity(packageRequest);
        var saved = packageRepository.save(packageEntity);
        log.info("createPackage end");
        return ApiResponse.success(saved);
    }

    @Override
    public ApiResponse<?> deletePackage(Long id) {
        log.info("deletePackage start");
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.PACKAGE_NOT_FOUND));
        packageRepository.delete(existingPackage);
        log.info("deletePackage end");
        return ApiResponse.success("success", null);
    }

    @Override
    @Transactional
    public ApiResponse<?> updatePackage(Long id, PackageRequest packageRequest) {
        log.info("updatePackage start id={}", id);
        Package existingPackage = packageRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(ExceptionCode.PACKAGE_NOT_FOUND));
        if (packageRequest.getIsDefault() != null && packageRequest.getIsDefault() == 1) {
            packageRepository.clearDefaultFlags();
        }
        Package packageEntity = PackageMapper.toExistingEntity(packageRequest, existingPackage);
        var saved = packageRepository.save(packageEntity);
        log.info("updatePackage end id={}", id);
        return ApiResponse.success(saved);
    }

    @Override
    public ApiResponse<?> getPackage(Long id) {
        log.info("getPackage start");
        Package packageEntity = packageRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.PACKAGE_NOT_FOUND));
        PackageResponse response = PackageMapper.toResponse(packageEntity);
        log.info("getPackage end");
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse<?> getPackages(Optional<Integer> page, Optional<Integer> size) {
        log.info("getPackageList start");
        PageRequest pageRequest = PageRequest.of(page.orElse(0), size.orElse(20));
        Page<?> pageable = packageRepository.findAll(pageRequest);
        PageResponse pageResponse = new PageResponse();
        pageResponse.setPage(pageable.getPageable().getPageNumber());
        pageResponse.setSize(pageable.getSize());
        pageResponse.setData(pageable.getContent());
        pageResponse.setTotalElements(pageable.getTotalElements());
        log.info("getPackageList end");
        return ApiResponse.success(pageResponse);
    }

}
