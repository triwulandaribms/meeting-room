package com.java.mega_giga.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record BookingReq(
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime bookingDate,

    String officeName,

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime startTime,
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime endTime,


    Integer participants,

    String roomName
) {}
