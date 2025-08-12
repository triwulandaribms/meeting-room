package com.java.meeting_room.repository;

import com.java.meeting_room.entity.BookingConsumption;
import com.java.meeting_room.entity.BookingConsumptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingConsumptionRepository extends JpaRepository<BookingConsumption, BookingConsumptionId> {
}
