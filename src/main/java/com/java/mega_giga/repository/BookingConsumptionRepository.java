package com.java.mega_giga.repository;

import com.java.mega_giga.entity.BookingConsumption;
import com.java.mega_giga.entity.BookingConsumptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingConsumptionRepository extends JpaRepository<BookingConsumption, BookingConsumptionId> {
}
