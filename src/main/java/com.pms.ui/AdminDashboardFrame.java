
package com.pms.ui;

import com.pms.dao.EntryExitDAO;
import com.pms.dao.PaymentDAO;
import com.pms.dao.SlotDAO;
import com.pms.model.Slot;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    public AdminDashboardFrame() {
        setTitle("Admin Dashboard");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(540, 560);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Admin Dashboard"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(10, 10));
        content.setBackground(UITheme.BACKGROUND);
        content.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        content.add(buildStatsPanel(), BorderLayout.NORTH);

        JPanel buttons = new JPanel(new GridLayout(0, 1, 8, 10));
        buttons.setBackground(UITheme.BACKGROUND);
        buttons.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton slotsBtn = new JButton("Manage Slots / Floors");
        JButton entryBtn = new JButton("Vehicle Entry (operator)");
        JButton exitBtn = new JButton("Vehicle Exit & Fee (operator)");
        JButton historyBtn = new JButton("Full Parking History");
        JButton reportsBtn = new JButton("Reports (Day/Month-wise)");
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(200, 60, 60));
        logoutBtn.setForeground(Color.WHITE);

        buttons.add(slotsBtn);
        buttons.add(entryBtn);
        buttons.add(exitBtn);
        buttons.add(historyBtn);
        buttons.add(reportsBtn);
        buttons.add(logoutBtn);
        content.add(buttons, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        slotsBtn.addActionListener(e -> new ManageSlotsFrame().setVisible(true));
        entryBtn.addActionListener(e -> new VehicleEntryFrame().setVisible(true));
        exitBtn.addActionListener(e -> new VehicleExitFrame().setVisible(true));
        historyBtn.addActionListener(e -> new AdminHistoryFrame().setVisible(true));
        reportsBtn.addActionListener(e -> new ReportsFrame().setVisible(true));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private JPanel buildStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(0, 2, 10, 10));
        stats.setBackground(Color.WHITE);
        stats.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UITheme.PRIMARY_LIGHT, 1),
                BorderFactory.createTitledBorder("Today at a glance")
        ));

        List<Slot> allSlots = new SlotDAO().getAllSlots();
        long vacant = allSlots.stream().filter(s -> "VACANT".equals(s.getStatus())).count();
        long occupied = allSlots.stream().filter(s -> "OCCUPIED".equals(s.getStatus())).count();
        long reserved = allSlots.stream().filter(s -> "RESERVED".equals(s.getStatus())).count();

        int todayEntries = new EntryExitDAO().countTodayEntries();
        BigDecimal todayCollections = new PaymentDAO().getTodaysCollections();

        stats.add(new JLabel("Total Slots:"));
        stats.add(boldLabel(String.valueOf(allSlots.size())));
        stats.add(new JLabel("Vacant:"));
        stats.add(boldLabel(String.valueOf(vacant)));
        stats.add(new JLabel("Occupied:"));
        stats.add(boldLabel(String.valueOf(occupied)));
        stats.add(new JLabel("Reserved (booked):"));
        stats.add(boldLabel(String.valueOf(reserved)));
        stats.add(new JLabel("Today's Entries:"));
        stats.add(boldLabel(String.valueOf(todayEntries)));
        stats.add(new JLabel("Today's Collections:"));
        stats.add(boldLabel("Rs. " + todayCollections));

        return stats;
    }

    private JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(UITheme.PRIMARY_DARK);
        l.setFont(UITheme.FONT_HEADING);
        return l;
    }
}

