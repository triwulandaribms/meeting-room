package com.java.meeting_room.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.java.meeting_room.entity.MasterJenisKonsumsi;
import com.java.meeting_room.model.request.MasterJenisKonsumsiReq;

@Repository
public interface MasterJenisKonsumsiRepository extends JpaRepository<MasterJenisKonsumsi, Integer> {

    List<MasterJenisKonsumsi> findByNameIn(Set<String> names);

    @Query("SELECT jk.name AS name, jk.maxPrice AS maxPrice FROM MasterJenisKonsumsi jk")
    List<MasterJenisKonsumsiReq> findAllNameAndPrice();

    @Query("SELECT jk.name FROM MasterJenisKonsumsi jk WHERE LOWER(jk.name) = LOWER(:name) AND jk.deletedAt IS NULL")
    List<String> cekName(@Param("name") String name);
    
    @Query("SELECT jk FROM MasterJenisKonsumsi jk WHERE jk.deletedAt IS NULL")
    List<MasterJenisKonsumsi> findAllActive();

    @Query("SELECT jk FROM MasterJenisKonsumsi jk WHERE jk.id = :id AND jk.deletedAt IS NULL")
    Optional<MasterJenisKonsumsi> findActiveById(@Param("id") Integer id);

}
