package com.example.mspackage.mapper;

import com.example.mspackage.entity.Package;
import com.example.mspackage.request.PackageRequest;
import com.example.mspackage.response.PackageResponse;

public class PackageMapper {

    public static Package toEntity(PackageRequest packageRequest) {
        Package entity = new Package();
        return toExistingEntity(packageRequest, entity);
    }

    public static Package toExistingEntity(PackageRequest request, Package entity) {
        entity.setValidityDays(request.getValidityDays());
        entity.setName(request.getName());
        entity.setPrice(request.getPrice());
        entity.setMinuteRate(request.getMinuteRate());
        entity.setIsDefault(request.getIsDefault() != null && request.getIsDefault() == 1 ? 1 : 0);
        return entity;
    }

    public static PackageResponse toResponse(Package entity) {
        PackageResponse response = new PackageResponse();
        response.setValidityDays(entity.getValidityDays());
        response.setName(entity.getName());
        response.setPrice(entity.getPrice());
        response.setId(entity.getId());
        response.setMinuteRate(entity.getMinuteRate());
        response.setIsDefault(entity.getIsDefault());
        return response;
    }

}
