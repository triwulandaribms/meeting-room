package com.java.meeting_room.repository;

import com.java.meeting_room.entity.Office;
import com.java.meeting_room.model.response.officeDto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {
    
    @Query("SELECT o FROM Office o WHERE o.id = :id")
    Optional<Office> findOfficeById(Integer id);

    @Query("SELECT new com.java.meeting_room.model.response.officeDto( " +
           "o.id, mo.id, o.roomName) " +
           "FROM Office o " +
           "JOIN o.masterOffice mo " +
           "WHERE o.deletedAt IS NULL")
    List<officeDto> findAllOfficeActive();
}
