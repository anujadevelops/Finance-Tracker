package com.yourapp.expense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class RegisterForm extends JFrame {

    JTextField userField;
    JPasswordField passField;

    public RegisterForm() {
        setTitle("📝 Register - Expense Tracker ");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("📝 Register", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(title, gbc);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        panel.add(userLabel, gbc);

        userField = new JTextField();
        userField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panel.add(userField, gbc);

        JLabel passLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(passLabel, gbc);

        passField = new JPasswordField();
        passField.setPreferredSize(new Dimension(150, 30));
        gbc.gridx = 1;
        panel.add(passField, gbc);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(registerButton, gbc);
        ExpenseTrackerGUI.makeRoundedButton(registerButton, new Color(40, 180, 99), Color.WHITE);

        registerButton.addActionListener((ActionEvent _) -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (registerUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new LoginPage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed!");
            }
        });

        add(panel);
    }

    private boolean registerUser(String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DBConnection.getConnection();

            String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            int rowsInserted = ps.executeUpdate();

            ps.close();
            conn.close();
            return rowsInserted > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new RegisterForm().setVisible(true));
    }
}
