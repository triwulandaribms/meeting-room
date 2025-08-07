package com.java.mega_giga.entity;

import java.io.Serializable;
import java.util.Objects;

public class BookingConsumptionId implements Serializable {
    private Integer booking; 
    private Integer jenisKonsumsi; 

    public BookingConsumptionId() {}

    public BookingConsumptionId(Integer booking, Integer jenisKonsumsi) {
        this.booking = booking;
        this.jenisKonsumsi = jenisKonsumsi;
    }

    public Integer getBooking() {
        return booking;
    }

    public void setBooking(Integer booking) {
        this.booking = booking;
    }

    public Integer getJenisKonsumsi() {
        return jenisKonsumsi;
    }

    public void setJenisKonsumsi(Integer jenisKonsumsi) {
        this.jenisKonsumsi = jenisKonsumsi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BookingConsumptionId)) return false;
        BookingConsumptionId that = (BookingConsumptionId) o;
        return Objects.equals(booking, that.booking) && Objects.equals(jenisKonsumsi, that.jenisKonsumsi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(booking, jenisKonsumsi);
    }
}
