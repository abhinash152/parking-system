package com.pms.model;

public class Slot {
    private int slotId;
    private int floorNo;
    private String slotNo;
    private String status; // VACANT / OCCUPIED / RESERVED

    public Slot() {}

    public Slot(int slotId, int floorNo, String slotNo, String status) {
        this.slotId = slotId;
        this.floorNo = floorNo;
        this.slotNo = slotNo;
        this.status = status;
    }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }
    public int getFloorNo() { return floorNo; }
    public void setFloorNo(int floorNo) { this.floorNo = floorNo; }
    public String getSlotNo() { return slotNo; }
    public void setSlotNo(String slotNo) { this.slotNo = slotNo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "Floor " + floorNo + " - " + slotNo + " (" + status + ")";
    }
}