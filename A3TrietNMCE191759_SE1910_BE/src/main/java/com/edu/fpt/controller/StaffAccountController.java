package com.edu.fpt.controller;

import com.edu.fpt.dto.projection.CustomerProjection;
import com.edu.fpt.dto.request.CustomerManagementRequest;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.service.CustomerService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/staff/accounts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffAccountController {
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CustomerProjection>>> getCustomers(
            @RequestParam(required = false, defaultValue = "") String keyword, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customers retrieved successfully",
                customerService.getAllUser(keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customer retrieved successfully",
                customerService.getCustomer(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerManagementRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Customer created successfully",
                        customerService.createCustomer(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Integer id, @Valid @RequestBody CustomerManagementRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customer updated successfully",
                customerService.updateCustomer(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Integer id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customer deleted successfully", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<CustomerResponse>> changeCustomerStatus(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Customer status updated successfully",
                customerService.changeStatusCustomer(id)));
    }
}
