package com.java.meeting_room.repository;


import com.java.meeting_room.entity.MasterOffice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterOfficeRepository extends JpaRepository<MasterOffice, Integer> {
    Optional<MasterOffice> findByOfficeName(String officeName);

    @Query("SELECT m FROM MasterOffice m WHERE m.id = :id")
    Optional<MasterOffice> findMasterOfficeById(Integer id);
}
