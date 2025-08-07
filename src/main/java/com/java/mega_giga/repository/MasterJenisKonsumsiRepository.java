package com.java.mega_giga.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.java.mega_giga.entity.MasterJenisKonsumsi;

@Repository
public interface MasterJenisKonsumsiRepository extends JpaRepository<MasterJenisKonsumsi, Integer> {
    
    List<MasterJenisKonsumsi> findByNameIn(Set<String> names);

}



