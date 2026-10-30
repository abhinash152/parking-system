package com.pms.dao;

import com.pms.db.DBConnection;
import com.pms.model.EntryExit;

import java.math.BigDecimal;
import java.sql.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EntryExitDAO {

    public int recordEntry(int vehicleId, int slotId, Integer bookingId) {
        String sql = "INSERT INTO Entry_Exit (vehicle_id, slot_id, booking_id, entry_time) VALUES (?, ?, ?, NOW())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, vehicleId);
            ps.setInt(2, slotId);
            if (bookingId != null) ps.setInt(3, bookingId); else ps.setNull(3, Types.INTEGER);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public EntryExit getOpenSessionByVehicleNo(String vehicleNo) {
        String sql = "SELECT ee.* FROM Entry_Exit ee " +
                "JOIN Vehicles v ON ee.vehicle_id = v.vehicle_id " +
                "WHERE v.vehicle_no = ? AND ee.exit_time IS NULL " +
                "ORDER BY ee.entry_time DESC LIMIT 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleNo.toUpperCase());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean recordExit(int recordId) {
        String sql = "UPDATE Entry_Exit SET exit_time = NOW() WHERE record_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, recordId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BigDecimal calculateFee(String vehicleType, Timestamp entryTime, Timestamp exitTime) {
        long minutes = Duration.between(entryTime.toInstant(), exitTime.toInstant()).toMinutes();
        long hours = (long) Math.ceil(minutes / 60.0);
        if (hours < 1) hours = 1;

        BigDecimal ratePerHour;
        switch (vehicleType) {
            case "Two-Wheeler": ratePerHour = new BigDecimal("10"); break;
            case "Car": ratePerHour = new BigDecimal("20"); break;
            case "Heavy Vehicle": ratePerHour = new BigDecimal("40"); break;
            default: ratePerHour = new BigDecimal("20");
        }
        return ratePerHour.multiply(BigDecimal.valueOf(hours));
    }

    public List<EntryExit> getHistoryByVehicle(int vehicleId) {
        List<EntryExit> list = new ArrayList<>();
        String sql = "SELECT * FROM Entry_Exit WHERE vehicle_id = ? ORDER BY entry_time DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<EntryExit> getAllHistory() {
        List<EntryExit> list = new ArrayList<>();
        String sql = "SELECT * FROM Entry_Exit ORDER BY entry_time DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countTodayEntries() {
        String sql = "SELECT COUNT(*) FROM Entry_Exit WHERE DATE(entry_time) = CURDATE()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private EntryExit mapRow(ResultSet rs) throws SQLException {
        EntryExit e = new EntryExit();
        e.setRecordId(rs.getInt("record_id"));
        e.setVehicleId(rs.getInt("vehicle_id"));
        e.setSlotId(rs.getInt("slot_id"));
        int bId = rs.getInt("booking_id");
        e.setBookingId(rs.wasNull() ? null : bId);
        e.setEntryTime(rs.getTimestamp("entry_time"));
        e.setExitTime(rs.getTimestamp("exit_time"));
        return e;
    }
}