package com.pms.model;

public class Vehicle {
    private int vehicleId;
    private int userId;
    private String vehicleNo;
    private String vehicleType; // Two-Wheeler / Car / Heavy Vehicle

    public Vehicle() {}

    public Vehicle(int vehicleId, int userId, String vehicleNo, String vehicleType) {
        this.vehicleId = vehicleId;
        this.userId = userId;
        this.vehicleNo = vehicleNo;
        this.vehicleType = vehicleType;
    }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getVehicleNo() { return vehicleNo; }
    public void setVehicleNo(String vehicleNo) { this.vehicleNo = vehicleNo; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
}
