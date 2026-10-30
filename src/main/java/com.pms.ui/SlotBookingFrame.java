package com.pms.ui;

import com.pms.dao.BookingDAO;
import com.pms.dao.SlotDAO;
import com.pms.exception.SlotNotAvailableException;
import com.pms.model.Booking;
import com.pms.model.Slot;
import com.pms.model.User;
import com.pms.util.QRUtil;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SlotBookingFrame extends JFrame {

    private final User currentUser;
    private final JComboBox<Slot> slotBox = new JComboBox<>();
    private final JTextField hoursFromNowField = new JTextField("1", 5);
    private final JTextField durationHoursField = new JTextField("2", 5);
    private final JLabel qrLabel = new JLabel();

    public SlotBookingFrame(User user) {
        this.currentUser = user;

        setTitle("Book a Parking Slot in Advance");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 540);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Advance Slot Reservation (QR Pass)"), BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Available Slot:"), gbc);
        gbc.gridx = 1; panel.add(slotBox, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Arrive in (hours from now):"), gbc);
        gbc.gridx = 1; panel.add(hoursFromNowField, gbc);

        gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Reserve for (duration, hours):"), gbc);
        gbc.gridx = 1; panel.add(durationHoursField, gbc);

        JButton bookBtn = new JButton("Book Slot & Generate QR Pass");
        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(bookBtn, gbc);

        qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
        qrLabel.setBorder(BorderFactory.createLineBorder(UITheme.PRIMARY_LIGHT, 1));
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(qrLabel, gbc);

        add(panel, BorderLayout.CENTER);

        loadVacantSlots();

        bookBtn.addActionListener(e -> doBooking());
    }

    private void loadVacantSlots() {
        slotBox.removeAllItems();
        List<Slot> vacant = new SlotDAO().getVacantSlots();
        for (Slot s : vacant) slotBox.addItem(s);
        if (vacant.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No vacant slots available right now.", "Fully booked", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void doBooking() {
        Slot selected = (Slot) slotBox.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "No slot selected.", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            int hoursFromNow = Integer.parseInt(hoursFromNowField.getText().trim());
            int durationHours = Integer.parseInt(durationHoursField.getText().trim());

            LocalDateTime from = LocalDateTime.now().plusHours(hoursFromNow);
            LocalDateTime to = from.plusHours(durationHours);

            BookingDAO bookingDAO = new BookingDAO();
            Booking booking = bookingDAO.createBooking(
                    currentUser.getUserId(),
                    selected.getSlotId(),
                    Timestamp.valueOf(from),
                    Timestamp.valueOf(to)
            );

            if (booking != null) {
                ImageIcon qrIcon = QRUtil.generateQRImage(booking.getQrCode(), 200);
                qrLabel.setIcon(qrIcon);
                qrLabel.setText(null);

                DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MMM HH:mm");
                JOptionPane.showMessageDialog(this,
                        "Slot booked!\n\nBooking ID: " + booking.getBookingId() +
                                "\nSlot: " + selected +
                                "\nValid: " + from.format(fmt) + " to " + to.format(fmt) +
                                "\nQR Code: " + booking.getQrCode() +
                                "\n\nShow this QR pass at the gate to skip manual slot search.",
                        "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);

                loadVacantSlots();
            } else {
                JOptionPane.showMessageDialog(this, "Booking failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers for hours.", "Invalid input", JOptionPane.WARNING_MESSAGE);
        } catch (SlotNotAvailableException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Slot unavailable", JOptionPane.WARNING_MESSAGE);
            loadVacantSlots();
        }
    }
}
