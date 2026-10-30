package com.pms.ui;

import com.pms.dao.UserDAO;
import com.pms.model.User;

import javax.swing.*;
import java.awt.*;

public class ChangePasswordFrame extends JFrame {

    public ChangePasswordFrame(User user) {
        setTitle("Change Password");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(380, 260);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Change Password"), BorderLayout.NORTH);

        JPasswordField newPasswordField = new JPasswordField(15);
        JPasswordField confirmPasswordField = new JPasswordField(15);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UITheme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1; panel.add(newPasswordField, gbc);

        gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1; panel.add(confirmPasswordField, gbc);

        JButton saveBtn = new JButton("Update Password");
        gbc.gridy = 2; gbc.gridx = 0; gbc.gridwidth = 2;
        panel.add(saveBtn, gbc);

        add(panel, BorderLayout.CENTER);

        saveBtn.addActionListener(e -> {
            String p1 = new String(newPasswordField.getPassword());
            String p2 = new String(confirmPasswordField.getPassword());
            if (p1.isEmpty() || !p1.equals(p2)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match or are empty.", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            boolean ok = new UserDAO().changePassword(user.getUserId(), p1);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Password updated successfully.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}