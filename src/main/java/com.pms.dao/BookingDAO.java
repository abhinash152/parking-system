package com.pms.dao;

import com.pms.db.DBConnection;
import com.pms.exception.SlotNotAvailableException;
import com.pms.model.Booking;
import com.pms.model.Slot;
import com.pms.util.QRUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private final SlotDAO slotDAO = new SlotDAO();

    public Booking createBooking(int userId, int slotId, Timestamp validFrom, Timestamp validTo)
            throws SlotNotAvailableException {

        Slot slot = slotDAO.getById(slotId);
        if (slot == null || !"VACANT".equals(slot.getStatus())) {
            throw new SlotNotAvailableException("Selected slot is no longer available for booking.");
        }

        String qrCode = QRUtil.generateBookingCode(slotId, userId);
        String sql = "INSERT INTO Booking (user_id, slot_id, qr_code, booking_time, valid_from, valid_to, booking_status) " +
                "VALUES (?, ?, ?, NOW(), ?, ?, 'ACTIVE')";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setInt(2, slotId);
            ps.setString(3, qrCode);
            ps.setTimestamp(4, validFrom);
            ps.setTimestamp(5, validTo);
            int rows = ps.executeUpdate();
            if (rows > 0) {
                slotDAO.updateStatus(slotId, "RESERVED");
                ResultSet keys = ps.getGeneratedKeys();
                int bookingId = keys.next() ? keys.getInt(1) : -1;
                return getById(bookingId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Booking getByQrCode(String qrCode) {
        String sql = "SELECT * FROM Booking WHERE qr_code = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, qrCode);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Booking getById(int bookingId) {
        String sql = "SELECT * FROM Booking WHERE booking_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Booking> getActiveBookingsByUser(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM Booking WHERE user_id = ? AND booking_status = 'ACTIVE'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean markUsed(int bookingId) {
        return updateStatus(bookingId, "USED");
    }

    public boolean cancelBooking(int bookingId, int slotId) {
        boolean ok = updateStatus(bookingId, "CANCELLED");
        if (ok) slotDAO.updateStatus(slotId, "VACANT");
        return ok;
    }

    private boolean updateStatus(int bookingId, String status) {
        String sql = "UPDATE Booking SET booking_status = ? WHERE booking_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setBookingId(rs.getInt("booking_id"));
        b.setUserId(rs.getInt("user_id"));
        b.setSlotId(rs.getInt("slot_id"));
        b.setQrCode(rs.getString("qr_code"));
        b.setBookingTime(rs.getTimestamp("booking_time"));
        b.setValidFrom(rs.getTimestamp("valid_from"));
        b.setValidTo(rs.getTimestamp("valid_to"));
        b.setBookingStatus(rs.getString("booking_status"));
        return b;
    }
}