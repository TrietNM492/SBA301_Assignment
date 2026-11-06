package com.edu.fpt.config;

import com.edu.fpt.entity.BookingDetail;
import com.edu.fpt.entity.BookingDetailId;
import com.edu.fpt.entity.BookingReservation;
import com.edu.fpt.entity.Customer;
import com.edu.fpt.entity.RoomInformation;
import com.edu.fpt.entity.RoomType;
import com.edu.fpt.entity.Staff;
import com.edu.fpt.repository.BookingDetailRepository;
import com.edu.fpt.repository.BookingRepository;
import com.edu.fpt.repository.CustomerRepository;
import com.edu.fpt.repository.RoomInformationRepository;
import com.edu.fpt.repository.RoomTypeRepository;
import com.edu.fpt.repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final StaffRepository staffRepository;
    private final CustomerRepository customerRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final RoomInformationRepository roomInformationRepository;
    private final BookingRepository bookingRepository;
    private final BookingDetailRepository bookingDetailRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (staffRepository.count() == 0) {
            Staff cascasStaff = new Staff();
            cascasStaff.setEmailAddress("cascas@gmail.com");
            cascasStaff.setPassword(passwordEncoder.encode("123123"));
            cascasStaff.setRole((short) 1);
            cascasStaff.setStaffStatus((short) 1);
            cascasStaff.setStaffFullName("CasCake");
            cascasStaff.setHireDate(LocalDate.now());
            cascasStaff.setTelephone("0123456789");
            staffRepository.save(cascasStaff);

        }
        if (customerRepository.count() == 0) {
            Customer nahalux = new Customer();
            nahalux.setCustomerFullName("Nahalux");
            nahalux.setEmailAddress("nihihi@gmail.com");
            nahalux.setPassword(passwordEncoder.encode("123123"));
            nahalux.setTelephone("0987654321");
            nahalux.setCustomerStatus((short) 1);
            nahalux.setCustomerBirthday(LocalDate.of(2026, 11, 11));
            customerRepository.save(nahalux);
        }

        seedRoomTypes();
        seedRooms();
        seedBookingDetails(seedBookings());
    }

    private void seedRoomTypes() {
        if (roomTypeRepository.count() == 0) {
            roomTypeRepository.saveAll(List.of(
                    createRoomType("Standard", "Comfortable room for short stays", "Non-smoking"),
                    createRoomType("Deluxe", "Spacious room with upgraded amenities", "City view"),
                    createRoomType("Suite", "Premium room with a separate living area", "Includes lounge access"),
                    createRoomType("Family", "Large room suitable for families", "Extra beds available")
            ));
        }
    }

    private RoomType createRoomType(String name, String description, String note) {
        RoomType roomType = new RoomType();
        roomType.setRoomTypeName(name);
        roomType.setTypeDescription(description);
        roomType.setTypeNote(note);
        return roomType;
    }

    private void seedRooms() {
        if (roomInformationRepository.count() == 0) {
            List<RoomType> roomTypes = roomTypeRepository.findAll(Sort.by("id"));
            if (roomTypes.isEmpty()) {
                throw new IllegalStateException("Cannot seed rooms without a room type");
            }

            roomInformationRepository.saveAll(List.of(
                    createRoom("101", "Standard room on the first floor", 2, "100.00", roomTypes.get(0)),
                    createRoom("201", "Deluxe room with a city view", 2, "150.00", roomTypes.get(1 % roomTypes.size())),
                    createRoom("301", "Suite with a separate living area", 2, "250.00", roomTypes.get(2 % roomTypes.size())),
                    createRoom("401", "Family room with extra space", 4, "180.00", roomTypes.get(3 % roomTypes.size())),
                    createRoom("102", "Quiet standard room near the garden", 2, "110.00", roomTypes.get(0)),
                    createRoom("202", "Deluxe room with a balcony", 3, "165.00", roomTypes.get(1 % roomTypes.size()))
            ));
        }
    }

    private RoomInformation createRoom(String number, String description, int capacity,
                                       String price, RoomType roomType) {
        RoomInformation room = new RoomInformation();
        room.setRoomNumber(number);
        room.setRoomDetailDescription(description);
        room.setRoomMaxCapacity(capacity);
        room.setRoomTypeID(roomType);
        room.setRoomStatus((short) 1);
        room.setRoomPricePerDay(new BigDecimal(price));
        return room;
    }

    private List<BookingReservation> seedBookings() {
        if (bookingRepository.count() == 0) {
            Customer customer = customerRepository.findAll(Sort.by("id")).stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Cannot seed bookings without a customer"));
            List<BookingReservation> bookings = new ArrayList<>();
            LocalDate today = LocalDate.now();

            for (int i = 0; i < 4; i++) {
                BookingReservation booking = new BookingReservation();
                LocalDate startDate = today.minusMonths(i + 1L).minusDays(3);
                booking.setBookingDate(startDate.minusDays(10));
                booking.setCustomerID(customer);
                booking.setBookingStatus((short) 1);
                booking.setTotalPrice(BigDecimal.ZERO);
                bookings.add(booking);
            }
            return bookingRepository.saveAll(bookings);
        }
        return List.of();
    }

    private void seedBookingDetails(List<BookingReservation> bookings) {
        if (bookingDetailRepository.count() == 0 && !bookings.isEmpty()) {
            List<RoomInformation> rooms = roomInformationRepository.findAll(Sort.by("id"));
            if (rooms.isEmpty()) {
                throw new IllegalStateException("Cannot seed booking details without rooms");
            }

            List<BookingDetail> details = new ArrayList<>();
            int detailCount = Math.min(4, Math.min(bookings.size(), rooms.size()));
            for (int i = 0; i < detailCount; i++) {
                BookingReservation booking = bookings.get(i);
                RoomInformation room = rooms.get(i);
                LocalDate startDate = booking.getBookingDate().plusDays(10);
                LocalDate endDate = startDate.plusDays(i + 2L);
                BigDecimal actualPrice = room.getRoomPricePerDay()
                        .multiply(BigDecimal.valueOf(ChronoUnit.DAYS.between(startDate, endDate)));

                BookingDetailId detailId = new BookingDetailId();
                detailId.setBookingReservationID(booking.getId());
                detailId.setRoomID(room.getId());

                BookingDetail detail = new BookingDetail();
                detail.setId(detailId);
                detail.setBookingReservationID(booking);
                detail.setRoomID(room);
                detail.setStartDate(startDate);
                detail.setEndDate(endDate);
                detail.setActualPrice(actualPrice);
                details.add(detail);

                booking.setTotalPrice(actualPrice);
            }
            bookingDetailRepository.saveAll(details);
            bookingRepository.saveAll(bookings.subList(0, detailCount));
        }
    }
}
