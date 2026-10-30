package com.pms.ui;

import com.pms.dao.PaymentDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class PaymentFrame extends JFrame {

    public PaymentFrame(int recordId, BigDecimal amount) {
        setTitle("Payment");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(380, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Payment"), BorderLayout.NORTH);

        JComboBox<String> modeBox = new JComboBox<>(new String[]{"Cash", "Card", "UPI"});

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel amountLabel = new JLabel("Amount Due: Rs. " + amount, SwingConstants.CENTER);
        amountLabel.setFont(UITheme.FONT_TITLE);
        amountLabel.setForeground(UITheme.PRIMARY_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(amountLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Payment Mode:"), gbc);
        gbc.gridx = 1; panel.add(modeBox, gbc);

        JButton payBtn = new JButton("Confirm Payment");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(payBtn, gbc);

        add(panel, BorderLayout.CENTER);

        payBtn.addActionListener(e -> {
            PaymentDAO paymentDAO = new PaymentDAO();
            int paymentId = paymentDAO.recordPayment(recordId, amount, (String) modeBox.getSelectedItem());
            if (paymentId != -1) {
                JOptionPane.showMessageDialog(this,
                        "Payment successful!\nReceipt No: " + paymentId + "\nAmount: Rs. " + amount +
                                "\nMode: " + modeBox.getSelectedItem(),
                        "Payment Complete", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Payment failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
