package com.example.mspackage.controller;

import com.example.mspackage.baseModels.ApiResponse;
import com.example.mspackage.request.PackageRequest;
import com.example.mspackage.service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/package")
public class PackageController {

    private final PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<?> createPackage(@RequestBody PackageRequest request) {
        return packageService.createPackage(request);
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updatePackage(@PathVariable Long id, @RequestBody PackageRequest request) {
        return packageService.updatePackage(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deletePackage(@PathVariable Long id) {
        return packageService.deletePackage(id);
    }

    @GetMapping("{id}")
    public ApiResponse<?> getPackage(@PathVariable Long id) {
        return packageService.getPackage(id);
    }

    @GetMapping
    public ApiResponse<?> getPackages(@RequestParam Optional<Integer> page,
                                      @RequestParam Optional<Integer> size) {
        return packageService.getPackages(page, size);
    }

}
