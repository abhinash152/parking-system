package com.pms.ui;

import com.pms.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final JTextField nameField = new JTextField(18);
    private final JTextField emailField = new JTextField(18);
    private final JTextField phoneField = new JTextField(18);
    private final JPasswordField passwordField = new JPasswordField(18);

    public RegisterFrame() {
        setTitle("Register - Parking Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(440, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        add(UITheme.buildHeader("Create Your Account"), BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UITheme.BACKGROUND);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(formPanel, gbc, 0, "Full Name:", nameField);
        addRow(formPanel, gbc, 1, "Email:", emailField);
        addRow(formPanel, gbc, 2, "Phone:", phoneField);
        addRow(formPanel, gbc, 3, "Password:", passwordField);

        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back to Login");
        backBtn.setBackground(UITheme.BACKGROUND);
        backBtn.setForeground(UITheme.PRIMARY);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        formPanel.add(registerBtn, gbc);
        gbc.gridy = 5;
        formPanel.add(backBtn, gbc);

        add(formPanel, BorderLayout.CENTER);

        registerBtn.addActionListener(e -> doRegister());
        backBtn.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridy = row; gbc.gridx = 0; panel.add(new JLabel(label), gbc);
        gbc.gridx = 1; panel.add(field, gbc);
    }

    private void doRegister() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Missing data", JOptionPane.WARNING_MESSAGE);
            return;
        }

        UserDAO userDAO = new UserDAO();
        if (userDAO.emailExists(email)) {
            JOptionPane.showMessageDialog(this, "An account with this email already exists.", "Duplicate email", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = userDAO.registerUser(name, email, phone, password);
        if (userId != -1) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please log in.");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
