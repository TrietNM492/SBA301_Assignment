package com.edu.fpt.repository;

import com.edu.fpt.entity.RoomInformation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInformationRepository extends JpaRepository<RoomInformation, Integer> {
    Page<RoomInformation> findByRoomNumberContainingIgnoreCase(String roomNumber, Pageable pageable);
}
