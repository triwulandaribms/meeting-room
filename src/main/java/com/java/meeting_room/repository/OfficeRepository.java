package com.java.meeting_room.repository;

import com.java.meeting_room.entity.Office;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
    
    @Query("SELECT o FROM Office o WHERE o.id = :id")
    Optional<Office> findOfficeById(Integer id);
}
