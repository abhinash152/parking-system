package com.pms.ui;

import com.pms.dao.AdminDAO;
import com.pms.dao.UserDAO;
import com.pms.model.User;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField emailField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);
    private final JComboBox<String> roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});

    public LoginFrame() {
        setTitle("Parking Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 360);
        setLocationRelativeTo(null);
        setIconImage(AppIcon.get());
        setResizable(false);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Parking Management System"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel subtitle = new JLabel("Sign in to continue", SwingConstants.CENTER);
        subtitle.setFont(UITheme.FONT_HEADING);
        subtitle.setForeground(UITheme.PRIMARY_DARK);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(subtitle, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; formPanel.add(new JLabel("Login as:"), gbc);
        gbc.gridx = 1; formPanel.add(roleBox, gbc);

        gbc.gridy = 2; gbc.gridx = 0; formPanel.add(new JLabel("Email / Username:"), gbc);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        gbc.gridy = 3; gbc.gridx = 0; formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("New user? Register");
        registerBtn.setBackground(UITheme.BACKGROUND);
        registerBtn.setForeground(UITheme.PRIMARY);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        formPanel.add(loginBtn, gbc);
        gbc.gridy = 5;
        formPanel.add(registerBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        loginBtn.addActionListener(e -> doLogin());
        registerBtn.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });
    }

    private void doLogin() {
        String emailOrUsername = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleBox.getSelectedItem();

        if (emailOrUsername.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if ("ADMIN".equals(role)) {
            AdminDAO adminDAO = new AdminDAO();
            if (adminDAO.login(emailOrUsername, password)) {
                new AdminDashboardFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin credentials.", "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            UserDAO userDAO = new UserDAO();
            User user = userDAO.login(emailOrUsername, password);
            if (user != null) {
                new UserDashboardFrame(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid email or password.", "Login failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}