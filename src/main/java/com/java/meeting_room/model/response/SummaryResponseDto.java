package com.java.meeting_room.model.response;

import java.util.Map;

public class SummaryResponseDto {
    private long totalData;
    private int perPage;
    private int totalPage;
    private int currentPage;
    private Map<String, Object> list; 

    public SummaryResponseDto(long totalData, int perPage, int totalPage, int currentPage, Map<String, Object> list) {
        this.totalData = totalData;
        this.perPage = perPage;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.list = list;
    }

    public long getTotalData() {
        return totalData;
    }

    public int getPerPage() {
        return perPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public Map<String, Object> getList() {
        return list;
    }
}
