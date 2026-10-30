package com.pms.ui;

import com.pms.dao.EntryExitDAO;
import com.pms.dao.VehicleDAO;
import com.pms.model.EntryExit;
import com.pms.model.User;
import com.pms.model.Vehicle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ParkingHistoryFrame extends JFrame {

    public ParkingHistoryFrame(User user) {
        setTitle("My Parking History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(680, 440);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("My Parking History"), BorderLayout.NORTH);

        String[] columns = {"Record ID", "Vehicle No.", "Slot ID", "Entry Time", "Exit Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        VehicleDAO vehicleDAO = new VehicleDAO();
        EntryExitDAO entryExitDAO = new EntryExitDAO();
        List<Vehicle> vehicles = vehicleDAO.getVehiclesByUser(user.getUserId());

        for (Vehicle v : vehicles) {
            List<EntryExit> sessions = entryExitDAO.getHistoryByVehicle(v.getVehicleId());
            for (EntryExit s : sessions) {
                model.addRow(new Object[]{
                        s.getRecordId(), v.getVehicleNo(), s.getSlotId(),
                        s.getEntryTime(), s.getExitTime() == null ? "Still parked" : s.getExitTime()
                });
            }
        }

        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setBackground(UITheme.PRIMARY);
        table.getTableHeader().setForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(scrollPane, BorderLayout.CENTER);
    }
}
