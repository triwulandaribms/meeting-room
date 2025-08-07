package com.java.mega_giga.repository;


import com.java.mega_giga.entity.MasterOffice;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterOfficeRepository extends JpaRepository<MasterOffice, Integer> {
    Optional<MasterOffice> findByOfficeName(String officeName);
}
