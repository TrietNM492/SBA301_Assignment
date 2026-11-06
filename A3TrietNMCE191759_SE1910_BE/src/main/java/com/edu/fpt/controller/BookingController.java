package com.edu.fpt.controller;

import com.edu.fpt.dto.request.BookingRequest;
import com.edu.fpt.dto.response.BookingResponse;
import com.edu.fpt.security.CustomUserDetails;
import com.edu.fpt.service.BookingService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Booking created successfully",
                        bookingService.createBooking(user.user().id(), request)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(required = false) String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Bookings retrieved successfully",
                bookingService.getBookingByCustomer(keyword, user.user().id(), pageable)));
    }
}
