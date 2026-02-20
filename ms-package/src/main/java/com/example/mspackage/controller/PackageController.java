package com.example.mspackage.controller;

import com.example.mspackage.baseModels.ApiResponse;
import com.example.mspackage.request.PackageRequest;
import com.example.mspackage.service.NumbersPackageService;
import com.example.mspackage.service.PackageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/package")
public class PackageController {

    private final PackageService packageService;
    private final NumbersPackageService numbersPackageService;

    public PackageController(PackageService packageService, NumbersPackageService numbersPackageService) {
        this.packageService = packageService;
        this.numbersPackageService = numbersPackageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package', 'POST')")
    public ApiResponse<?> createPackage(@Valid @RequestBody PackageRequest request) {
        return packageService.createPackage(request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package/{id}', 'PUT')")
    public ApiResponse<?> updatePackage(@PathVariable Long id, @Valid @RequestBody PackageRequest request) {
        return packageService.updatePackage(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package/{id}', 'DELETE')")
    public ApiResponse<?> deletePackage(@PathVariable Long id) {
        return packageService.deletePackage(id);
    }

    @GetMapping("{id}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package/{id}', 'GET')")
    public ApiResponse<?> getPackage(@PathVariable Long id) {
        return packageService.getPackage(id);
    }

    @GetMapping
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package', 'GET')")
    public ApiResponse<?> getPackages(@RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size) {
        return packageService.getPackages(page, size);
    }

    @GetMapping("/active/by-number/{numberId}")
    @PreAuthorize("@rbacService.hasAccess(authentication, '/api/package/active/by-number/{numberId}', 'GET')")
    public ApiResponse<?> getActiveTariffByNumber(@PathVariable Integer numberId) {
        return ApiResponse.success(numbersPackageService.getActiveTariff(numberId));
    }

}
