package com.example.mspackage.service;

import com.example.mspackage.baseModels.ApiResponse;
import com.example.mspackage.request.PackageRequest;

import java.util.Optional;

public interface PackageService {

    ApiResponse<?> createPackage(PackageRequest packageRequest);

    ApiResponse<?> deletePackage(Long id);

    ApiResponse<?> updatePackage(Long id, PackageRequest packageRequest);

    ApiResponse<?> getPackage(Long id);

    ApiResponse<?> getPackages(Optional<Integer> page, Optional<Integer> size);

}
