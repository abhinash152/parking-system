package com.pms.ui;

import com.pms.dao.PaymentDAO;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;

public class ReportsFrame extends JFrame {

    private final JTextField fromField = new JTextField(LocalDate.now().withDayOfMonth(1).toString(), 12);
    private final JTextField toField = new JTextField(LocalDate.now().toString(), 12);
    private final JLabel resultLabel = new JLabel(" ");

    public ReportsFrame() {
        setTitle("Reports");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Revenue Report"), BorderLayout.NORTH);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("From (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; panel.add(fromField, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("To (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1; panel.add(toField, gbc);

        JButton genBtn = new JButton("Generate Report");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(genBtn, gbc);

        resultLabel.setFont(UITheme.FONT_HEADING);
        resultLabel.setForeground(UITheme.PRIMARY_DARK);
        gbc.gridy = 3;
        panel.add(resultLabel, gbc);

        add(panel, BorderLayout.CENTER);

        genBtn.addActionListener(e -> {
            try {
                LocalDate from = LocalDate.parse(fromField.getText().trim());
                LocalDate to = LocalDate.parse(toField.getText().trim());
                Timestamp fromTs = Timestamp.valueOf(from.atStartOfDay());
                Timestamp toTs = Timestamp.valueOf(to.plusDays(1).atStartOfDay());

                BigDecimal total = new PaymentDAO().getCollectionsBetween(fromTs, toTs);
                resultLabel.setText("<html>Total collections from " + from + " to " + to +
                        ": <b>Rs. " + total + "</b></html>");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid dates in yyyy-mm-dd format.", "Invalid input", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}