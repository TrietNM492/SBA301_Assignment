package com.edu.fpt.service;

import com.edu.fpt.dto.request.CustomerRequest;
import com.edu.fpt.dto.request.LoginRequest;
import com.edu.fpt.dto.request.RegisterRequest;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.dto.response.LoginResponse;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.entity.Customer;
import com.edu.fpt.exception.EmailAlreadyExistException;
import com.edu.fpt.exception.InvalidCredentialsException;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.security.CustomUserDetails;
import com.edu.fpt.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public void register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getRePassword())) {
            throw new InvalidCredentialsException("Password and confirmation do not match");
        }
        if (customerRepository.findByEmailAddressIgnoreCase(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistException("Email already exists");
        }
        Customer customer = new Customer();
        customer.setEmailAddress(request.getEmail().trim());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCustomerFullName(request.getCustomerFullName().trim());
        customer.setTelephone(request.getTelephone().trim());
        customer.setCustomerBirthday(request.getCustomerBirthday());
        customer.setCustomerStatus((short) 1);
        customerRepository.save(customer);
    }

    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail().trim();
        String type = customerRepository.findByEmailAddressIgnoreCase(email).isPresent()
                ? "CUSTOMER" : "STAFF";
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(type + ":" + email, request.getPassword()));
            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
            return LoginResponse.builder()
                    .accessToken(jwtService.generateToken(user))
                    .customerResponse("CUSTOMER".equals(type) ? toCustomerResponse(user.user().id()) : null)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException("Invalid email or password");
        }
    }

    public String logout() {
        return "Logged out. Discard the access token on the client.";
    }

    @Transactional
    public CustomerResponse changeProfile(Integer userId, CustomerRequest request) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customer.setCustomerFullName(request.getCustomerFullName().trim());
        customer.setTelephone(request.getTelephone());
        customer.setCustomerBirthday(request.getCustomerBirthday());
        return toCustomerResponse(customerRepository.save(customer).getId());
    }


    public CustomerResponse getProfile(Integer userId) {
        return toCustomerResponse(userId);
    }

    private CustomerResponse toCustomerResponse(Integer userId) {
        Customer customer = customerRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return CustomerResponse.builder()
                .id(customer.getId())
                .customerFullName(customer.getCustomerFullName())
                .telephone(customer.getTelephone())
                .emailAddress(customer.getEmailAddress())
                .customerBirthday(customer.getCustomerBirthday())
                .customerStatus(customer.getCustomerStatus())
                .build();
    }

}
