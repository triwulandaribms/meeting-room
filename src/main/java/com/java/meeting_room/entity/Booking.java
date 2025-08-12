package com.java.meeting_room.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "Booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "bookingDate", nullable = false)
    private OffsetDateTime bookingDate;

    @Column(name = "startTime", nullable = false)
    private OffsetDateTime startTime;

    @Column(name = "endTime", nullable = false)
    private OffsetDateTime endTime;

    @Column(name = "participants", nullable = false)
    private Integer participants;

    @Column(name = "roomName", nullable = false)
    private String roomName;

    @Column(name = "officeName", nullable = false)
    private String officeName;

    @Column(name = "create_at")
    private OffsetDateTime createAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    public Booking() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getParticipants() {
        return participants;
    }

    public void setParticipants(Integer participants) {
        this.participants = participants;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public OffsetDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(OffsetDateTime createAt) {
        this.createAt = createAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private java.util.List<BookingConsumption> bookingConsumptions;

    public java.util.List<BookingConsumption> getBookingConsumptions() {
        return bookingConsumptions;
    }

    public void setBookingConsumptions(java.util.List<BookingConsumption> bookingConsumptions) {
        this.bookingConsumptions = bookingConsumptions;
    }

}
