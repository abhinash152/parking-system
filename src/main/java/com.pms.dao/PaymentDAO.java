package com.pms.dao;

import com.pms.db.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class PaymentDAO {

    public int recordPayment(int recordId, BigDecimal amount, String paymentMode) {
        String sql = "INSERT INTO Payment (record_id, amount, payment_mode, payment_time) VALUES (?, ?, ?, NOW())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, recordId);
            ps.setBigDecimal(2, amount);
            ps.setString(3, paymentMode);
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

    public BigDecimal getTodaysCollections() {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Payment WHERE DATE(payment_time) = CURDATE()";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getCollectionsBetween(Timestamp from, Timestamp to) {
        String sql = "SELECT COALESCE(SUM(amount), 0) FROM Payment WHERE payment_time BETWEEN ? AND ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setTimestamp(1, from);
            ps.setTimestamp(2, to);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBigDecimal(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}