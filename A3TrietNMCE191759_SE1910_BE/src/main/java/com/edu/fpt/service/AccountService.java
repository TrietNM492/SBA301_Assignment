package com.edu.fpt.service;

import com.edu.fpt.dto.request.CustomerRequest;
import com.edu.fpt.dto.request.LoginRequest;
import com.edu.fpt.dto.request.RegisterRequest;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.dto.response.LoginResponse;
import com.edu.fpt.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final CustomerRepository customerRepository;

    @Transactional
    public void register(RegisterRequest request) {

    }

    public LoginResponse login(LoginRequest request) {

        return null;
    }

    @Transactional
    public CustomerResponse profile(CustomerRequest request) {

        return null;
    }


}
