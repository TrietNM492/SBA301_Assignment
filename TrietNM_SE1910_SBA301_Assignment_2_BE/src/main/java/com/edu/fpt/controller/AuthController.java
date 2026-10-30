package com.edu.fpt.controller;

import com.edu.fpt.dto.request.LoginRequest;
import com.edu.fpt.dto.response.AccountReponse;
import com.edu.fpt.service.SystemAccountService;
import com.edu.fpt.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final SystemAccountService systemAccountService;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AccountReponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        AccountReponse account = systemAccountService.login(request);
        if (account.accountRole() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account has no supported role.");
        }
        if (httpRequest.getSession(false) != null) {
            httpRequest.changeSessionId();
        }
        String role = switch (account.accountRole()) {
            case 1 -> "ADMIN";
            case 2 -> "STAFF";
            default -> throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This account has no supported role.");
        };
        var authentication = UsernamePasswordAuthenticationToken.authenticated(
                account.id(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role))
        );
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        securityContextRepository.saveContext(context, httpRequest, httpResponse);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Login successful.", account));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Logout successful.", null));
    }
}
