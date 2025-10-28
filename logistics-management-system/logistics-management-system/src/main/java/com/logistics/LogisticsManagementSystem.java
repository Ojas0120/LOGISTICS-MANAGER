package com.logistics;

import com.logistics.ui.LoginFrame;
import com.logistics.util.DatabaseConnection;

import javax.swing.*;

public class LogisticsManagementSystem {
    public static void main(String[] args) {
        // Initialize database
        DatabaseConnection.getInstance();
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show login frame
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
