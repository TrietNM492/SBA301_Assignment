package com.edu.fpt.repository;

import com.edu.fpt.entity.BookingReservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingReservation, Integer> {
    @Query("select b from BookingReservation b join b.customerID c " +
            "where :keyword = '' or lower(c.customerFullName) like lower(concat('%', :keyword, '%')) " +
            "or lower(c.emailAddress) like lower(concat('%', :keyword, '%'))")
    Page<BookingReservation> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("select b from BookingReservation b where b.customerID.id = :customerId " +
            "and (:keyword = '' or cast(b.id as string) like concat('%', :keyword, '%'))")
    Page<BookingReservation> findCustomerBookings(@Param("customerId") Integer customerId,
                                                   @Param("keyword") String keyword,
                                                   Pageable pageable);
}
