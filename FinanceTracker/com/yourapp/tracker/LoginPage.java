package com.yourapp.expense;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;

public class LoginPage extends JFrame {

    JTextField userField;
    JPasswordField passField;
    public static int loggedInUserId = -1;
    public static String loggedInUsername = "";
    public LoginPage() {

        setTitle("🔐 Login - Finance Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("🔐 Login", SwingConstants.CENTER);
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

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(new Color(52, 152, 219));
        loginButton.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);
        ExpenseTrackerGUI.makeRoundedButton(loginButton, new Color(52, 152, 219), Color.white);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(new Color(40, 180, 99));
        registerButton.setForeground(Color.WHITE);
        gbc.gridy = 4;
        panel.add(registerButton, gbc);
        ExpenseTrackerGUI.makeRoundedButton(registerButton, new Color(40, 180, 99), Color.white);

        // Login Action
        loginButton.addActionListener((ActionEvent _) -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword()).trim();
            if (authenticateUser(username, password)) {
                dispose();
                new ExpenseTrackerGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials");
            }
        });

        // Register Action
        registerButton.addActionListener((ActionEvent _) -> {
            dispose();
            new RegisterForm().setVisible(true);
        });

        add(panel);
        
    }

private boolean authenticateUser(String username, String password) {
           String sql = "SELECT * FROM users WHERE username=? AND password=?";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = DBConnection.getConnection(); 
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);

        ResultSet rs = ps.executeQuery();
 if (rs.next()) {
            ExpenseTrackerGUI.loggedInUserId = rs.getInt("id");
            ExpenseTrackerGUI.loggedInUsername = username;
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
            return false;
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
        return false;
    }
    }

   public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new LoginPage().setVisible(true));
}
}
