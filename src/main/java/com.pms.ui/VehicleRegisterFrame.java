package com.pms.ui;

import com.pms.dao.VehicleDAO;
import com.pms.model.User;

import javax.swing.*;
import java.awt.*;

public class VehicleRegisterFrame extends JFrame {

    public VehicleRegisterFrame(User user) {
        setTitle("Register Vehicle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Add a Vehicle"), BorderLayout.NORTH);

        JTextField vehicleNoField = new JTextField(15);
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"Two-Wheeler", "Car", "Heavy Vehicle"});

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1; panel.add(vehicleNoField, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Vehicle Type:"), gbc);
        gbc.gridx = 1; panel.add(typeBox, gbc);

        JButton saveBtn = new JButton("Save Vehicle");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> {
            String vehicleNo = vehicleNoField.getText().trim();
            if (vehicleNo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Enter a vehicle number.", "Missing data", JOptionPane.WARNING_MESSAGE);
                return;
            }
            VehicleDAO dao = new VehicleDAO();
            int id = dao.registerVehicle(user.getUserId(), vehicleNo, (String) typeBox.getSelectedItem());
            if (id != -1) {
                JOptionPane.showMessageDialog(this, "Vehicle registered successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not register vehicle (maybe this number is already registered?).",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}