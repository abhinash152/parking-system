package com.pms.ui;

import com.pms.model.User;

import javax.swing.*;
import java.awt.*;

public class UserDashboardFrame extends JFrame {

    private final User currentUser;

    public UserDashboardFrame(User user) {
        this.currentUser = user;

        setTitle("User Dashboard - " + user.getFullName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 520);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Welcome, " + user.getFullName() + "!"), BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 12));
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        JButton registerVehicleBtn = new JButton("Register a Vehicle");
        JButton bookSlotBtn = new JButton("Book a Slot in Advance (QR Pass)");
        JButton entryBtn = new JButton("Vehicle Entry (Walk-in)");
        JButton exitBtn = new JButton("Vehicle Exit & Pay");
        JButton historyBtn = new JButton("My Parking History");
        JButton changePwBtn = new JButton("Change Password");
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(200, 60, 60));
        logoutBtn.setForeground(Color.WHITE);

        panel.add(registerVehicleBtn);
        panel.add(bookSlotBtn);
        panel.add(entryBtn);
        panel.add(exitBtn);
        panel.add(historyBtn);
        panel.add(changePwBtn);
        panel.add(logoutBtn);

        add(panel, BorderLayout.CENTER);

        registerVehicleBtn.addActionListener(e -> new VehicleRegisterFrame(currentUser).setVisible(true));
        bookSlotBtn.addActionListener(e -> new SlotBookingFrame(currentUser).setVisible(true));
        entryBtn.addActionListener(e -> new VehicleEntryFrame().setVisible(true));
        exitBtn.addActionListener(e -> new VehicleExitFrame().setVisible(true));
        historyBtn.addActionListener(e -> new ParkingHistoryFrame(currentUser).setVisible(true));
        changePwBtn.addActionListener(e -> new ChangePasswordFrame(currentUser).setVisible(true));
        logoutBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }
}