package com.edu.fpt.repository;

import com.edu.fpt.entity.BookingReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingReservation, Integer> {


}
