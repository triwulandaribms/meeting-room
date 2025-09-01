package com.java.meeting_room.model.response;

import java.util.Map;

public record SummaryResponseDto(
        long totalData,
        int perPage,
        int totalPage,
        int currentPage,
        Map<String, Object> list
) {}
