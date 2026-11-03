package com.edu.fpt.repository;

import com.edu.fpt.entity.RoomInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInformationRepository extends JpaRepository<RoomInformation, Integer> {
}
