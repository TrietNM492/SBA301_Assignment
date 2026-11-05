package com.edu.fpt.repository;

import com.edu.fpt.entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingDetailRepository extends JpaRepository<BookingDetail, Integer> {
    @Query(value = "select bk from BookingDetail bk where bk.roomID.id = :roomId and bk.bookingReservationID.bookingStatus = :bookingStatus and :now between bk.startDate and bk.endDate")
    boolean existsByRoomIDBookingNow(@Param("roomId") Integer roomId, @Param("bookingStatus") Short bookingStatus, @Param("now") LocalDate now);

}
