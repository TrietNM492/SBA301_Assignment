package com.edu.fpt.controller;

import com.edu.fpt.dto.request.AccountRegister;
import com.edu.fpt.dto.request.AccountRequest;
import com.edu.fpt.dto.request.ChangePasswordRequest;
import com.edu.fpt.dto.request.ProfileRequest;
import com.edu.fpt.dto.response.AccountReponse;
import com.edu.fpt.dto.response.ProfileResponse;
import com.edu.fpt.service.SystemAccountService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final SystemAccountService systemAccountService;

    @GetMapping("/accounts")
    public ResponseEntity<ApiResponse<PageResponse<AccountReponse>>> getAccounts(
            @RequestParam(defaultValue = "") String key,
            Pageable pageable) {
        PageResponse<AccountReponse> accounts = systemAccountService.viewAccounts(key, pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Accounts retrieved successfully", accounts));
    }

    @PostMapping("/accounts")
    public ResponseEntity<ApiResponse<AccountReponse>> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountReponse account = systemAccountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Account created successfully", account));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AccountReponse>> register(@Valid @RequestBody AccountRegister request) {
        AccountReponse account = systemAccountService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Account registered successfully", account));
    }

    @GetMapping("/accounts/{userId}/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> viewProfile(
            @PathVariable Long userId,
            Authentication authentication) {
        ensureCanManageAccount(userId, authentication);
        ProfileResponse profile = systemAccountService.viewProfile(userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile retrieved successfully", profile));
    }

    @PutMapping("/accounts/{userId}/name")
    public ResponseEntity<ApiResponse<ProfileResponse>> changeUserName(
            @PathVariable Long userId,
            Authentication authentication,
            @Valid @RequestBody ProfileRequest request) {
        ensureCanManageAccount(userId, authentication);
        ProfileResponse profile = systemAccountService.changeUserName(request, userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Account name updated successfully", profile));
    }

    @PatchMapping("/accounts/{userId}/password")
    public ResponseEntity<ApiResponse<ProfileResponse>> changeUserPassword(
            @PathVariable Long userId,
            Authentication authentication,
            @Valid @RequestBody ChangePasswordRequest request) {
        ensureCanManageAccount(userId, authentication);
        ProfileResponse profile = systemAccountService.changeUserPassword(request, userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Password changed successfully", profile));
    }

    private void ensureCanManageAccount(Long userId, Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        boolean isOwnAccount = authentication.getPrincipal() instanceof Long accountId
                && accountId.equals(userId);
        if (!isAdmin && !isOwnAccount) {
            throw new AccessDeniedException("You can only manage your own account.");
        }
    }
}
