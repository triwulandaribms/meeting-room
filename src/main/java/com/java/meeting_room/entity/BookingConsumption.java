package com.java.meeting_room.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "BookingConsumption")
@IdClass(BookingConsumptionId.class)
public class BookingConsumption {

    @Id
    @ManyToOne
    @JoinColumn(name = "id_booking", referencedColumnName = "id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_booking_consumption_booking"))
    private Booking booking;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_jenis_konsumsi", referencedColumnName = "id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_booking_consumption_jenis_konsumsi"))
    private MasterJenisKonsumsi jenisKonsumsi;

    public BookingConsumption() {
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public MasterJenisKonsumsi getJenisKonsumsi() {
        return jenisKonsumsi;
    }

    public void setJenisKonsumsi(MasterJenisKonsumsi jenisKonsumsi) {
        this.jenisKonsumsi = jenisKonsumsi;
    }
}
