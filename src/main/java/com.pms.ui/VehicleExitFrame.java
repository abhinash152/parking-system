package com.pms.ui;

import com.pms.dao.EntryExitDAO;
import com.pms.dao.SlotDAO;
import com.pms.dao.VehicleDAO;
import com.pms.model.EntryExit;
import com.pms.model.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class VehicleExitFrame extends JFrame {

    private final JTextField vehicleNoField = new JTextField(15);
    private final JLabel feeLabel = new JLabel(" ");
    private EntryExit openSession;
    private Vehicle vehicle;

    public VehicleExitFrame() {
        setTitle("Vehicle Exit & Fee Calculation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(420, 260);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(new JLabel("Vehicle Exit", SwingConstants.CENTER), gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1; panel.add(vehicleNoField, gbc);

        JButton calcBtn = new JButton("Calculate Fee");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(calcBtn, gbc);

        gbc.gridy = 3;
        panel.add(feeLabel, gbc);

        JButton confirmExitBtn = new JButton("Confirm Exit & Go to Payment");
        confirmExitBtn.setEnabled(false);
        gbc.gridy = 4;
        panel.add(confirmExitBtn, gbc);

        add(panel);

        calcBtn.addActionListener(e -> {
            if (calculateFee()) confirmExitBtn.setEnabled(true);
        });

        confirmExitBtn.addActionListener(e -> {
            EntryExitDAO entryExitDAO = new EntryExitDAO();
            entryExitDAO.recordExit(openSession.getRecordId());
            new SlotDAO().updateStatus(openSession.getSlotId(), "VACANT");

            BigDecimal fee = entryExitDAO.calculateFee(vehicle.getVehicleType(), openSession.getEntryTime(),
                    new Timestamp(System.currentTimeMillis()));

            new PaymentFrame(openSession.getRecordId(), fee).setVisible(true);
            dispose();
        });
    }

    private boolean calculateFee() {
        String vehicleNo = vehicleNoField.getText().trim();
        if (vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the vehicle number.", "Missing data", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        vehicle = new VehicleDAO().getByVehicleNo(vehicleNo);
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not found.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        EntryExitDAO entryExitDAO = new EntryExitDAO();
        openSession = entryExitDAO.getOpenSessionByVehicleNo(vehicleNo);
        if (openSession == null) {
            JOptionPane.showMessageDialog(this, "No open parking session found for this vehicle.", "Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        BigDecimal fee = entryExitDAO.calculateFee(vehicle.getVehicleType(), openSession.getEntryTime(),
                new Timestamp(System.currentTimeMillis()));
        feeLabel.setText("<html>Entry: " + openSession.getEntryTime() +
                "<br>Fee due: <b>Rs. " + fee + "</b></html>");
        return true;
    }
}