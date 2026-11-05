package com.edu.fpt.service;

import com.edu.fpt.dto.request.BookingRequest;
import com.edu.fpt.dto.response.BookingResponse;
import com.edu.fpt.repository.BookingDetailRepository;
import com.edu.fpt.repository.BookingRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;

    public Page<BookingResponse> getAllBooking(String keyword, Pageable pageable) {

        return null;
    }


    public BookingResponse getBookingDetail(Integer bookingId) {

        return null;
    }

    public Page<BookingResponse> getBookingByCustomer(String keyword, Integer customerId) {

        return null;
    }

    @Transactional
    public BookingResponse createBooking(Integer customerId, BookingRequest request) {

        return null;
    }

}
