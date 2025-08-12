package com.java.meeting_room.repository;

import com.java.meeting_room.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b LEFT JOIN b.bookingConsumptions bc " +
            "WHERE b.deletedAt IS NULL AND b.bookingDate BETWEEN :startDate AND :endDate " +
            "ORDER BY b.officeName, b.roomName")
    Page<Booking> findAllWithConsumptionsBetween(
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate,
            Pageable pageable);

}
