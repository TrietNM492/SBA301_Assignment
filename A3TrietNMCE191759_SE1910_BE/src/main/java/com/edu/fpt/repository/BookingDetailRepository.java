package com.edu.fpt.repository;

import com.edu.fpt.entity.BookingDetail;
import com.edu.fpt.entity.BookingDetailId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, BookingDetailId> {
    boolean existsByRoomID_Id(Integer roomId);

    List<BookingDetail> findAllByBookingReservationID_Id(Integer bookingId);

    @Query("select count(bd) > 0 from BookingDetail bd " +
            "where bd.roomID.id = :roomId " +
            "and bd.bookingReservationID.bookingStatus in :activeStatuses " +
            "and bd.startDate < :endDate and bd.endDate > :startDate")
    boolean existsOverlappingBooking(@Param("roomId") Integer roomId,
                                     @Param("activeStatuses") List<Short> activeStatuses,
                                     @Param("startDate") LocalDate startDate,
                                     @Param("endDate") LocalDate endDate);
}
