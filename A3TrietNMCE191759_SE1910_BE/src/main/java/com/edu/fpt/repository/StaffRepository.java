package com.edu.fpt.repository;

import com.edu.fpt.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {

    Optional<Staff> findByEmailAddressIgnoreCase(String emailAddress);
}

