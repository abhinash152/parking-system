package com.pms.dao;

import com.pms.db.DBConnection;
import com.pms.exception.SlotNotAvailableException;
import com.pms.model.Slot;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SlotDAO {

    public List<Slot> getAllSlots() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM Slots ORDER BY floor_no, slot_no";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Slot> getVacantSlots() {
        List<Slot> list = new ArrayList<>();
        String sql = "SELECT * FROM Slots WHERE status = 'VACANT' ORDER BY floor_no, slot_no";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Slot autoAllocateSlot() throws SlotNotAvailableException {
        List<Slot> vacant = getVacantSlots();
        if (vacant.isEmpty()) {
            throw new SlotNotAvailableException("No vacant slot available in the facility right now.");
        }
        Slot chosen = vacant.get(0);
        updateStatus(chosen.getSlotId(), "OCCUPIED");
        chosen.setStatus("OCCUPIED");
        return chosen;
    }

    public boolean updateStatus(int slotId, String status) {
        String sql = "UPDATE Slots SET status = ? WHERE slot_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, slotId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Slot getById(int slotId) {
        String sql = "SELECT * FROM Slots WHERE slot_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, slotId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addSlot(int floorNo, String slotNo) {
        String sql = "INSERT INTO Slots (floor_no, slot_no, status) VALUES (?, ?, 'VACANT')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, floorNo);
            ps.setString(2, slotNo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Slot mapRow(ResultSet rs) throws SQLException {
        Slot s = new Slot();
        s.setSlotId(rs.getInt("slot_id"));
        s.setFloorNo(rs.getInt("floor_no"));
        s.setSlotNo(rs.getString("slot_no"));
        s.setStatus(rs.getString("status"));
        return s;
    }
}