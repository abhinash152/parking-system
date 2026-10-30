package com.pms.model;

import java.sql.Timestamp;

public class Booking {
    private int bookingId;
    private int userId;
    private int slotId;
    private String qrCode;
    private Timestamp bookingTime;
    private Timestamp validFrom;
    private Timestamp validTo;
    private String bookingStatus; // ACTIVE / USED / CANCELLED / EXPIRED

    public Booking() {}

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }
    public String getQrCode() { return qrCode; }
    public void setQrCode(String qrCode) { this.qrCode = qrCode; }
    public Timestamp getBookingTime() { return bookingTime; }
    public void setBookingTime(Timestamp bookingTime) { this.bookingTime = bookingTime; }
    public Timestamp getValidFrom() { return validFrom; }
    public void setValidFrom(Timestamp validFrom) { this.validFrom = validFrom; }
    public Timestamp getValidTo() { return validTo; }
    public void setValidTo(Timestamp validTo) { this.validTo = validTo; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
}