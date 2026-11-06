package com.edu.fpt.service;

import com.edu.fpt.dto.projection.CustomerProjection;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.dto.request.CustomerManagementRequest;
import com.edu.fpt.entity.Customer;
import com.edu.fpt.exception.EmailAlreadyExistException;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public CustomerResponse getCustomer(Integer customerId) {
        return toCustomerResponse(customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found")));
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerManagementRequest request) {
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required for a new customer");
        }
        if (customerRepository.findByEmailAddressIgnoreCase(request.getEmailAddress()).isPresent()) {
            throw new EmailAlreadyExistException("Email already exists");
        }
        Customer customer = new Customer();
        applyRequest(customer, request);
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setCustomerStatus(request.getCustomerStatus() == null ? (short) 1 : request.getCustomerStatus());
        return toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse updateCustomer(Integer customerId, CustomerManagementRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        customerRepository.findByEmailAddressIgnoreCase(request.getEmailAddress())
                .filter(existing -> !existing.getId().equals(customerId))
                .ifPresent(existing -> { throw new EmailAlreadyExistException("Email already exists"); });
        applyRequest(customer, request);
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            customer.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getCustomerStatus() != null) {
            customer.setCustomerStatus(request.getCustomerStatus());
        }
        return toCustomerResponse(customerRepository.save(customer));
    }

    @Transactional
    public CustomerResponse changeStatusCustomer(Integer customerId) {
        Customer customerFounded = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found!"));
        CustomerResponse response = toCustomerResponse(customerFounded);
        Short newStatus = (short) (customerFounded.getCustomerStatus() == 1 ? 0 : 1);
        int updated = customerRepository.changeCustomerStatus(customerId, newStatus);
        if (updated < 1) {
            throw new RuntimeException("Update User Status Error");
        }
        response.setCustomerStatus(newStatus);

        return response;
    }

    public PageResponse<CustomerProjection> getAllUser(String keyword, Pageable pageable) {
        Page<CustomerProjection> customers = customerRepository.findByCustomerFullNameAndEmailAddressContainingIgnoreCase(keyword, pageable);
        return new PageResponse<>(customers);
    }

    @Transactional
    public void deleteCustomer(Integer customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer Not Found!"));
        int updated = customerRepository.changeCustomerStatus(customerId, (short) 0);
        if (updated < 1) {
            throw new RuntimeException("Update User Status Error");
        }
    }


    private CustomerResponse toCustomerResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .customerFullName(customer.getCustomerFullName())
                .customerBirthday(customer.getCustomerBirthday())
                .telephone(customer.getTelephone())
                .emailAddress(customer.getEmailAddress())
                .customerStatus(customer.getCustomerStatus())
                .build();
    }

    private void applyRequest(Customer customer, CustomerManagementRequest request) {
        customer.setCustomerFullName(request.getCustomerFullName().trim());
        customer.setTelephone(request.getTelephone().trim());
        customer.setEmailAddress(request.getEmailAddress().trim());
        customer.setCustomerBirthday(request.getCustomerBirthday());
    }




}
