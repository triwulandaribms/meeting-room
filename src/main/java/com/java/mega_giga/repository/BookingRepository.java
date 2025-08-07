package com.java.mega_giga.repository;

import com.java.mega_giga.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("""
    SELECT b FROM Booking b
    LEFT JOIN FETCH b.bookingConsumptions bc
    WHERE b.bookingDate BETWEEN :startDate AND :endDate
""")
List<Booking> findAllWithConsumptionsBetween(
    @Param("startDate") OffsetDateTime startDate,
    @Param("endDate") OffsetDateTime endDate
);

}
