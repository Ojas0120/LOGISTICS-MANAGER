package com.logistics.ui;

import com.logistics.dao.UserDAO;
import com.logistics.model.User;
import com.logistics.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;
    private JButton registerButton;
    private UserDAO userDAO;
    
    public LoginFrame() {
        this.userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Logistics Management System - Login");
        setResizable(false);
        getContentPane().setBackground(UIStyles.BG_COLOR);
        pack();
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        usernameField = UIStyles.createStyledTextField(20);
        passwordField = createStyledPasswordField();
        loginButton = UIStyles.createPrimaryButton("🔓 Login", UIStyles.PRIMARY_COLOR);
        cancelButton = UIStyles.createSecondaryButton("❌ Cancel");
        registerButton = UIStyles.createPrimaryButton("✨ Register", UIStyles.ACCENT_COLOR);
    }
    
    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return field;
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Title panel with gradient
        JPanel titlePanel = UIStyles.createHeaderPanel();
        titlePanel.setLayout(new BorderLayout());
        titlePanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 20, 0));
        
        JLabel titleLabel = new JLabel("📦 Logistics Management System");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Login form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        UIStyles.stylePanel(formPanel);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 40, 25, 40)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(UIStyles.createLabel("Username:", 13, false), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(UIStyles.createLabel("Password:", 13, false), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(passwordField, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        UIStyles.stylePanel(buttonPanel);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(25, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        // Register button
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        UIStyles.stylePanel(registerPanel);
        registerPanel.add(registerButton);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(registerPanel, gbc);
        
        // Info panel with credentials
        JPanel infoPanel = new JPanel();
        infoPanel.setBackground(UIStyles.PANEL_COLOR);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Check database type
        DatabaseConnection dbConn = DatabaseConnection.getInstance();
        String dbType = dbConn.isUsingMySQL() ? "MySQL" : "SQLite";
        
        JLabel infoLabel = new JLabel("<html><center>" +
            "<b style='color:#2563eb'>Default Credentials:</b><br/>" +
            "<b>Admins:</b><br/>" +
            "• admin / admin123<br/>" +
            "• admin2 / admin456<br/><br/>" +
            "<b>Users:</b><br/>" +
            "• user / user123<br/>" +
            "• john / john123<br/>" +
            "• sarah / sarah123<br/>" +
            "• mike / mike123<br/><br/>" +
            "<b>Database:</b> " + dbType + "<br/>" +
            "<b>Currency:</b> Indian Rupees (₹)" +
            "</center></html>");
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoLabel.setForeground(UIStyles.TEXT_COLOR);
        infoPanel.add(infoLabel);
        
        add(titlePanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegistrationDialog();
            }
        });
        
        // Enter key on password field
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", 
                                        "Login Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User user = userDAO.authenticate(username, password);
        
        if (user != null) {
            dispose();
            
            if (user.getRole() == User.UserRole.ADMIN) {
                new AdminDashboard(user).setVisible(true);
            } else {
                new UserDashboard(user).setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid username or password.\n\n" +
                "Available credentials:\n" +
                "Admins: admin/admin123, admin2/admin456\n" +
                "Users: user/user123, john/john123, sarah/sarah123, mike/mike123", 
                "Login Error", JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
            usernameField.requestFocus();
        }
    }
    
    private void showRegistrationDialog() {
        UserRegistrationDialog registrationDialog = new UserRegistrationDialog(this);
        registrationDialog.setVisible(true);
        
        if (registrationDialog.isRegistrationSuccessful()) {
            JOptionPane.showMessageDialog(this, "Registration successful! You can now login with your new credentials.", 
                                        "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
