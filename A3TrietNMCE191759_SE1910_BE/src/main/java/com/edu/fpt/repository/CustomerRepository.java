package com.edu.fpt.repository;

import com.edu.fpt.dto.projection.CustomerProjection;
import com.edu.fpt.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Optional<Customer> findByEmailAddressIgnoreCase(String emailAddress);

    @Modifying
    @Query(value = "update Customer c set c.customerStatus = :customerStatus where c.id = :customerId")
    int changeCustomerStatus(@Param("customerId") Integer customerId, @Param("customerStatus") Short customerStatus);

    @Query(value = "select c.id as id, " +
            "c.customerFullName as customerFullName, " +
            "c.telephone as telephone, " +
            "c.emailAddress as emailAddress, " +
            "c.customerBirthday as customerBirthday, " +
            "c.customerStatus as customerStatus " +
            "from Customer c " +
            "where lower(c.customerFullName) like lower(concat('%', :keyword, '%')) " +
            "or lower(c.emailAddress) like lower(concat('%', :keyword, '%'))")
    Page<CustomerProjection> findByCustomerFullNameAndEmailAddressContainingIgnoreCase(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
