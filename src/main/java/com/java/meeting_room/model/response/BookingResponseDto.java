package com.java.meeting_room.model.response;

import java.time.OffsetDateTime;
import java.util.List;

public record BookingResponseDto(
        OffsetDateTime bookingDate,
        String officeName,
        OffsetDateTime startTime,
        OffsetDateTime endTime,
        List<ConsumptionDto> listConsumption,
        String roomName,
        Integer participants
) {
    public record ConsumptionDto(String name) {}
}
