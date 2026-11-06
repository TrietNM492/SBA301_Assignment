package com.edu.fpt.controller;

import com.edu.fpt.dto.request.CustomerRequest;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.security.CustomUserDetails;
import com.edu.fpt.service.AccountService;
import com.edu.fpt.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> getProfile(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile retrieved successfully",
                accountService.getProfile(user.user().id())));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> changeProfile(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody CustomerRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile updated successfully",
                accountService.changeProfile(user.user().id(), request)));
    }
}
