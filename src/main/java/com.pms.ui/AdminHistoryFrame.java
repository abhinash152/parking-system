package com.pms.ui;

import com.pms.dao.EntryExitDAO;
import com.pms.model.EntryExit;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AdminHistoryFrame extends JFrame {

    public AdminHistoryFrame() {
        setTitle("Full Parking History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 420);
        setLocationRelativeTo(null);

        String[] columns = {"Record ID", "Vehicle ID", "Slot ID", "Booking ID", "Entry Time", "Exit Time"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        List<EntryExit> allHistory = new EntryExitDAO().getAllHistory();
        for (EntryExit s : allHistory) {
            model.addRow(new Object[]{
                    s.getRecordId(), s.getVehicleId(), s.getSlotId(),
                    s.getBookingId() == null ? "-" : s.getBookingId(),
                    s.getEntryTime(),
                    s.getExitTime() == null ? "Still parked" : s.getExitTime()
            });
        }

        JTable table = new JTable(model);
        add(new JScrollPane(table));
    }
}