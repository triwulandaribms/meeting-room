
package com.java.mega_giga.model.response;

import java.time.OffsetDateTime;
import java.util.List;

public class BookingResponseDto {
    private OffsetDateTime bookingDate;
    private String officeName;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private List<ConsumptionDto> listConsumption;
    private String roomName;
    private Integer participants;


    public static class ConsumptionDto {
        private String name;

        public ConsumptionDto() {}

        public ConsumptionDto(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }


    public OffsetDateTime getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(OffsetDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public OffsetDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(OffsetDateTime startTime) {
        this.startTime = startTime;
    }

    public OffsetDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(OffsetDateTime endTime) {
        this.endTime = endTime;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public List<ConsumptionDto> getListConsumption() {
        return listConsumption;
    }

    public void setListConsumption(List<ConsumptionDto> listConsumption) {
        this.listConsumption = listConsumption;
    }
}
