package com.edu.fpt.controller;

import com.edu.fpt.dto.request.BookingStatusRequest;
import com.edu.fpt.dto.response.BookingResponse;
import com.edu.fpt.service.BookingService;
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
@RequestMapping("/api/v1/staff/bookings")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffBookingController {
    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<BookingResponse>>> getBookings(
            @RequestParam(required = false) String keyword, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Bookings retrieved successfully",
                bookingService.getAllBooking(keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getBooking(@PathVariable Integer id) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Booking retrieved successfully",
                bookingService.getBookingDetail(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<BookingResponse>> changeBookingStatus(
            @PathVariable Integer id, @Valid @RequestBody BookingStatusRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Booking status updated successfully",
                bookingService.changeBookingStatus(id, request.getBookingStatus())));
    }
}
