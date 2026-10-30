package com.pms.ui;

import com.pms.dao.BookingDAO;
import com.pms.dao.EntryExitDAO;
import com.pms.dao.SlotDAO;
import com.pms.dao.VehicleDAO;
import com.pms.exception.SlotNotAvailableException;
import com.pms.model.Booking;
import com.pms.model.Slot;
import com.pms.model.Vehicle;

import javax.swing.*;
import java.awt.*;

public class VehicleEntryFrame extends JFrame {

    private final JTextField vehicleNoField = new JTextField(15);
    private final JTextField qrCodeField = new JTextField(15);

    public VehicleEntryFrame() {
        setTitle("Vehicle Entry");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Vehicle Entry"), BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel walkInLabel = new JLabel("Walk-in vehicle (auto slot allocation)");
        walkInLabel.setFont(UITheme.FONT_HEADING);
        walkInLabel.setForeground(UITheme.PRIMARY_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(walkInLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Vehicle Number:"), gbc);
        gbc.gridx = 1; panel.add(vehicleNoField, gbc);

        JButton walkInBtn = new JButton("Record Walk-in Entry");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(walkInBtn, gbc);

        JSeparator separator = new JSeparator();
        gbc.gridy = 3;
        panel.add(separator, gbc);

        JLabel qrLabel = new JLabel("Entry via QR Parking Pass (USP)");
        qrLabel.setFont(UITheme.FONT_HEADING);
        qrLabel.setForeground(UITheme.PRIMARY_DARK);
        gbc.gridy = 4;
        panel.add(qrLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("QR Code (scanned/typed):"), gbc);
        gbc.gridx = 1; panel.add(qrCodeField, gbc);

        JButton qrBtn = new JButton("Validate QR & Record Entry");
        gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(qrBtn, gbc);

        add(panel, BorderLayout.CENTER);

        walkInBtn.addActionListener(e -> doWalkInEntry());
        qrBtn.addActionListener(e -> doQrEntry());
    }

    private void doWalkInEntry() {
        String vehicleNo = vehicleNoField.getText().trim();
        if (vehicleNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the vehicle number.", "Missing data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.getByVehicleNo(vehicleNo);
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this,
                    "This vehicle is not registered yet. Please register it first (or ask the operator to do a one-time walk-in registration).",
                    "Not registered", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            SlotDAO slotDAO = new SlotDAO();
            Slot slot = slotDAO.autoAllocateSlot();
            EntryExitDAO entryExitDAO = new EntryExitDAO();
            int recordId = entryExitDAO.recordEntry(vehicle.getVehicleId(), slot.getSlotId(), null);
            if (recordId != -1) {
                JOptionPane.showMessageDialog(this,
                        "Entry recorded!\nAllocated Slot: " + slot + "\nRecord ID: " + recordId,
                        "Entry Successful", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        } catch (SlotNotAvailableException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Parking full", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void doQrEntry() {
        String qrCode = qrCodeField.getText().trim();
        if (qrCode.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter the QR code value.", "Missing data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        BookingDAO bookingDAO = new BookingDAO();
        Booking booking = bookingDAO.getByQrCode(qrCode);
        if (booking == null || !"ACTIVE".equals(booking.getBookingStatus())) {
            JOptionPane.showMessageDialog(this, "Invalid or already-used QR pass.", "Validation failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String vehicleNo = JOptionPane.showInputDialog(this, "Enter the vehicle number for this booking:");
        if (vehicleNo == null || vehicleNo.trim().isEmpty()) return;

        VehicleDAO vehicleDAO = new VehicleDAO();
        Vehicle vehicle = vehicleDAO.getByVehicleNo(vehicleNo.trim());
        if (vehicle == null) {
            JOptionPane.showMessageDialog(this, "Vehicle not registered.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SlotDAO slotDAO = new SlotDAO();
        slotDAO.updateStatus(booking.getSlotId(), "OCCUPIED");
        bookingDAO.markUsed(booking.getBookingId());

        EntryExitDAO entryExitDAO = new EntryExitDAO();
        int recordId = entryExitDAO.recordEntry(vehicle.getVehicleId(), booking.getSlotId(), booking.getBookingId());

        Slot slot = slotDAO.getById(booking.getSlotId());
        JOptionPane.showMessageDialog(this,
                "QR Pass validated. Entry recorded!\nSlot: " + slot + "\nRecord ID: " + recordId,
                "Entry Successful", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }
}
