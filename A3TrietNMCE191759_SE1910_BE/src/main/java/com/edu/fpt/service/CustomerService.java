package com.edu.fpt.service;

import com.edu.fpt.dto.projection.CustomerProjection;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.entity.Customer;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

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




}
