package com.edu.fpt.service;

import com.edu.fpt.dto.request.BookingRequest;
import com.edu.fpt.dto.response.BookingResponse;
import com.edu.fpt.dto.response.CustomerResponse;
import com.edu.fpt.entity.*;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.BookingDetailRepository;
import com.edu.fpt.repository.BookingRepository;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.repository.RoomInformationRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingService {
    private static final List<Short> ACTIVE_STATUSES = List.of((short) 0, (short) 1);
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final CustomerRepository customerRepository;
    private final RoomInformationRepository roomInformationRepository;
    private final RoomService roomService;

    public PageResponse<BookingResponse> getAllBooking(String keyword, Pageable pageable) {
        return new PageResponse<>(bookingRepository.search(keyword == null ? "" : keyword.trim(), pageable)
                .map(this::toBookingResponse));
    }

    public BookingResponse getBookingDetail(Integer bookingId) {
        BookingReservation booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        return toBookingResponse(booking);
    }

    public PageResponse<BookingResponse> getBookingByCustomer(String keyword, Integer customerId, Pageable pageable) {
        return new PageResponse<>(bookingRepository.findCustomerBookings(customerId,
                keyword == null ? "" : keyword.trim(), pageable).map(this::toBookingResponse));
    }

    @Transactional
    public BookingResponse createBooking(Integer customerId, BookingRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        BookingReservation booking = new BookingReservation();
        booking.setBookingDate(request.getBookingDate() == null ? LocalDate.now() : request.getBookingDate());
        booking.setCustomerID(customer);
        booking.setBookingStatus((short) 0);
        booking.setTotalPrice(BigDecimal.ZERO);
        booking = bookingRepository.save(booking);

        BigDecimal total = BigDecimal.ZERO;
        Set<Integer> roomIds = new HashSet<>();
        for (BookingRequest.BookingDetailRequest detailRequest : request.getBookingDetails()) {
            if (!roomIds.add(detailRequest.getRoomId())) {
                throw new IllegalArgumentException("A room may only be included once in a booking");
            }
            if (!detailRequest.getEndDate().isAfter(detailRequest.getStartDate())) {
                throw new IllegalArgumentException("End date must be after start date");
            }
            RoomInformation room = roomInformationRepository.findById(detailRequest.getRoomId())
                    .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + detailRequest.getRoomId()));
            if (!Short.valueOf((short) 1).equals(room.getRoomStatus())) {
                throw new IllegalArgumentException("Room is inactive: " + room.getRoomNumber());
            }
            if (bookingDetailRepository.existsOverlappingBooking(room.getId(), ACTIVE_STATUSES,
                    detailRequest.getStartDate(), detailRequest.getEndDate())) {
                throw new IllegalArgumentException("Room is not available for the requested dates: " + room.getRoomNumber());
            }

            BigDecimal price = room.getRoomPricePerDay()
                    .multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(detailRequest.getStartDate(), detailRequest.getEndDate())));
            BookingDetail detail = new BookingDetail();
            BookingDetailId detailId = new BookingDetailId();
            detailId.setBookingReservationID(booking.getId());
            detailId.setRoomID(room.getId());
            detail.setId(detailId);
            detail.setBookingReservationID(booking);
            detail.setRoomID(room);
            detail.setStartDate(detailRequest.getStartDate());
            detail.setEndDate(detailRequest.getEndDate());
            detail.setActualPrice(price);
            bookingDetailRepository.save(detail);
            total = total.add(price);
        }
        booking.setTotalPrice(total);
        bookingRepository.save(booking);
        return toBookingResponse(booking);
    }

    @Transactional
    public BookingResponse changeBookingStatus(Integer bookingId, Short status) {
        if (status == null || status < 0 || status > 2) {
            throw new IllegalArgumentException("Booking status must be 0 (pending), 1 (approved), or 2 (cancelled)");
        }
        BookingReservation booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));
        booking.setBookingStatus(status);
        return toBookingResponse(bookingRepository.save(booking));
    }

    private BookingResponse toBookingResponse(BookingReservation booking) {
        Customer customer = booking.getCustomerID();
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(customer.getId())
                .customerFullName(customer.getCustomerFullName())
                .telephone(customer.getTelephone())
                .emailAddress(customer.getEmailAddress())
                .customerBirthday(customer.getCustomerBirthday())
                .customerStatus(customer.getCustomerStatus())
                .build();
        List<BookingResponse.BookingDetailResponse> details = new ArrayList<>();
        for (BookingDetail detail : bookingDetailRepository.findAllByBookingReservationID_Id(booking.getId())) {
            details.add(BookingResponse.BookingDetailResponse.builder()
                    .id(detail.getId())
                    .roomResponse(roomService.toRoomResponse(detail.getRoomID()))
                    .startDate(detail.getStartDate())
                    .endDate(detail.getEndDate())
                    .actualPrice(detail.getActualPrice())
                    .build());
        }
        return BookingResponse.builder()
                .id(booking.getId())
                .bookingDate(booking.getBookingDate())
                .totalPrice(booking.getTotalPrice())
                .bookingStatus(booking.getBookingStatus())
                .customerResponse(customerResponse)
                .bookingDetails(details)
                .build();
    }
}
