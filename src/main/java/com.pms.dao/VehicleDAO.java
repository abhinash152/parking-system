package com.pms.dao;

import com.pms.db.DBConnection;
import com.pms.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public int registerVehicle(int userId, String vehicleNo, String vehicleType) {
        String sql = "INSERT INTO Vehicles (user_id, vehicle_no, vehicle_type) VALUES (?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, vehicleNo.toUpperCase());
            ps.setString(3, vehicleType);
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

    public Vehicle getByVehicleNo(String vehicleNo) {
        String sql = "SELECT * FROM Vehicles WHERE vehicle_no = ?";
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

    public List<Vehicle> getVehiclesByUser(int userId) {
        List<Vehicle> list = new ArrayList<>();
        String sql = "SELECT * FROM Vehicles WHERE user_id = ?";
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

    public boolean updateVehicle(int vehicleId, String vehicleNo, String vehicleType) {
        String sql = "UPDATE Vehicles SET vehicle_no = ?, vehicle_type = ? WHERE vehicle_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vehicleNo.toUpperCase());
            ps.setString(2, vehicleType);
            ps.setInt(3, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteVehicle(int vehicleId) {
        String sql = "DELETE FROM Vehicles WHERE vehicle_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Vehicle mapRow(ResultSet rs) throws SQLException {
        Vehicle v = new Vehicle();
        v.setVehicleId(rs.getInt("vehicle_id"));
        v.setUserId(rs.getInt("user_id"));
        v.setVehicleNo(rs.getString("vehicle_no"));
        v.setVehicleType(rs.getString("vehicle_type"));
        return v;
    }
}