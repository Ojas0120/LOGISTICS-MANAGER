package com.logistics.ui;

import com.logistics.dao.CourierCompanyDAO;
import com.logistics.dao.ShipmentDAO;
import com.logistics.dao.UserDAO;
import com.logistics.model.CourierCompany;
import com.logistics.model.Shipment;
import com.logistics.model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminDashboard extends JFrame {
    private User currentUser;
    private CourierCompanyDAO companyDAO;
    private ShipmentDAO shipmentDAO;
    private UserDAO userDAO;
    
    private JTable companyTable;
    private JTable shipmentTable;
    private DefaultTableModel companyTableModel;
    private DefaultTableModel shipmentTableModel;
    
    private JTextField nameField, pricePerKmField, pricePerKgField, handlingTimeField;
    private JCheckBox airCheckBox, roadCheckBox, railCheckBox;

    public AdminDashboard(User user) {
        this.currentUser = user;
        this.companyDAO = new CourierCompanyDAO();
        this.shipmentDAO = new ShipmentDAO();
        this.userDAO = new UserDAO();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Logistics Management System - Admin Dashboard");
        getContentPane().setBackground(UIStyles.BG_COLOR);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Company management components
        companyTableModel = new DefaultTableModel(new String[]{"ID", "Name", "Price/Km", "Price/Kg", "Handling Time", "Transport Modes", "Active"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        companyTable = new JTable(companyTableModel);
        UIStyles.styleTable(companyTable);
        companyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Shipment table with enhanced columns
        shipmentTableModel = new DefaultTableModel(new String[]{"Order ID", "User", "From", "To", "Distance", "Weight", "Priority", "Mode", "Company", "Status", "Date"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        shipmentTable = new JTable(shipmentTableModel);
        UIStyles.styleTable(shipmentTable);
        shipmentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Form components
        nameField = UIStyles.createStyledTextField(20);
        pricePerKmField = UIStyles.createStyledTextField(10);
        pricePerKgField = UIStyles.createStyledTextField(10);
        handlingTimeField = UIStyles.createStyledTextField(10);
        airCheckBox = new JCheckBox("Air");
        roadCheckBox = new JCheckBox("Road");
        railCheckBox = new JCheckBox("Rail");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with gradient header
        JPanel topPanel = UIStyles.createHeaderPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel welcomeLabel = new JLabel("👨‍💼 Welcome, " + currentUser.getUsername() + " (Admin)");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = UIStyles.createSecondaryButton("Logout");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        // Main content with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        
        // Company Management Tab
        tabbedPane.addTab("Company Management", createCompanyManagementPanel());
        
        // Order History Tab
        tabbedPane.addTab("Order History", createOrderHistoryPanel());
        
        // User Management Tab
        tabbedPane.addTab("User Management", createUserManagementPanel());
        
        add(topPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        
        // Event handlers
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JPanel createCompanyManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Company form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "Add/Edit Company",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Company Name:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(nameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Price per Km:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(pricePerKmField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Price per Kg:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(pricePerKgField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Handling Time (hrs):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(handlingTimeField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Transport Modes:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        JPanel modePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        modePanel.add(airCheckBox);
        modePanel.add(roadCheckBox);
        modePanel.add(railCheckBox);
        formPanel.add(modePanel, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        JButton addButton = UIStyles.createPrimaryButton("➕ Add Company", UIStyles.SUCCESS_COLOR);
        JButton updateButton = UIStyles.createPrimaryButton("🔄 Update", UIStyles.PRIMARY_COLOR);
        JButton deleteButton = UIStyles.createPrimaryButton("🗑️ Delete", UIStyles.WARNING_COLOR);
        JButton clearButton = UIStyles.createSecondaryButton("🧹 Clear");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        formPanel.add(buttonPanel, gbc);
        
        // Company table
        JScrollPane tableScrollPane = new JScrollPane(companyTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "Companies",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tableScrollPane.setBackground(Color.WHITE);
        
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Event handlers for company management
        addButton.addActionListener(e -> addCompany());
        updateButton.addActionListener(e -> updateCompany());
        deleteButton.addActionListener(e -> deleteCompany());
        clearButton.addActionListener(e -> clearForm());
        
        companyTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadSelectedCompany();
            }
        });
        
        return panel;
    }

    private JPanel createOrderHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Add refresh button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(Color.WHITE);
        JButton refreshButton = UIStyles.createSecondaryButton("Refresh Orders");
        JButton statusUpdateButton = UIStyles.createPrimaryButton("Update Status", UIStyles.PRIMARY_COLOR);
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(statusUpdateButton);
        
        JScrollPane tableScrollPane = new JScrollPane(shipmentTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "All Orders",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tableScrollPane.setBackground(Color.WHITE);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        // Event handlers
        refreshButton.addActionListener(e -> loadShipments());
        statusUpdateButton.addActionListener(e -> updateOrderStatus());
        
        return panel;
    }

    private JPanel createUserManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("User Management - Coming Soon", JLabel.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private void setupEventHandlers() {
        // Additional event handlers can be added here
    }

    private void loadData() {
        loadCompanies();
        loadShipments();
    }

    private void loadCompanies() {
        companyTableModel.setRowCount(0);
        List<CourierCompany> companies = companyDAO.findAll();
        
        for (CourierCompany company : companies) {
            Object[] row = {
                company.getId(),
                company.getName(),
                String.format("%.2f", company.getPricePerKm()),
                String.format("%.2f", company.getPricePerKg()),
                company.getBaseHandlingTime(),
                company.getTransportModes(),
                company.isActive() ? "Yes" : "No"
            };
            companyTableModel.addRow(row);
        }
    }

    private void loadShipments() {
        shipmentTableModel.setRowCount(0);
        List<Shipment> shipments = shipmentDAO.findAll();
        
        for (Shipment shipment : shipments) {
            User user = userDAO.findById(shipment.getUserId());
            String username = user != null ? user.getUsername() : "Unknown";
            
            String companyName = "Not Selected";
            if (shipment.getSelectedCompanyId() != null) {
                CourierCompany company = companyDAO.findById(shipment.getSelectedCompanyId());
                if (company != null) {
                    companyName = company.getName();
                }
            }
            
            Object[] row = {
                shipment.getId(),
                username,
                shipment.getSenderCity(),
                shipment.getReceiverCity(),
                String.format("%.2f", shipment.getDistance()),
                String.format("%.2f", shipment.getWeight()),
                shipment.getPriority().name(),
                shipment.getTransportMode().name(),
                companyName,
                shipment.getStatus().name(),
                shipment.getCreatedAt() != null ? shipment.getCreatedAt().toString() : "N/A"
            };
            shipmentTableModel.addRow(row);
        }
    }

    private void updateOrderStatus() {
        int selectedRow = shipmentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to update status.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int orderId = (Integer) shipmentTableModel.getValueAt(selectedRow, 0);
        Shipment shipment = shipmentDAO.findById(orderId);
        
        if (shipment != null) {
            String[] statusOptions = {"PENDING", "CONFIRMED", "IN_TRANSIT", "DELIVERED", "CANCELLED"};
            String currentStatus = shipment.getStatus().name();
            
            String selectedStatus = (String) JOptionPane.showInputDialog(
                this,
                "Select new status for Order #" + orderId + ":",
                "Update Order Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                currentStatus
            );
            
            if (selectedStatus != null && !selectedStatus.equals(currentStatus)) {
                shipment.setStatus(Shipment.Status.valueOf(selectedStatus));
                if (shipmentDAO.update(shipment)) {
                    JOptionPane.showMessageDialog(this, "Order status updated successfully!");
                    loadShipments();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating order status.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void addCompany() {
        if (validateCompanyForm()) {
            CourierCompany company = new CourierCompany();
            company.setName(nameField.getText().trim());
            company.setPricePerKm(Double.parseDouble(pricePerKmField.getText()));
            company.setPricePerKg(Double.parseDouble(pricePerKgField.getText()));
            company.setBaseHandlingTime(Integer.parseInt(handlingTimeField.getText()));
            company.setTransportModes(getSelectedTransportModes());
            
            if (companyDAO.create(company)) {
                JOptionPane.showMessageDialog(this, "Company added successfully!");
                clearForm();
                loadCompanies();
            } else {
                JOptionPane.showMessageDialog(this, "Error adding company.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateCompany() {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a company to update.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (validateCompanyForm()) {
            int companyId = (Integer) companyTableModel.getValueAt(selectedRow, 0);
            CourierCompany company = companyDAO.findById(companyId);
            
            if (company != null) {
                company.setName(nameField.getText().trim());
                company.setPricePerKm(Double.parseDouble(pricePerKmField.getText()));
                company.setPricePerKg(Double.parseDouble(pricePerKgField.getText()));
                company.setBaseHandlingTime(Integer.parseInt(handlingTimeField.getText()));
                company.setTransportModes(getSelectedTransportModes());
                
                if (companyDAO.update(company)) {
                    JOptionPane.showMessageDialog(this, "Company updated successfully!");
                    clearForm();
                    loadCompanies();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating company.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteCompany() {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a company to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int companyId = (Integer) companyTableModel.getValueAt(selectedRow, 0);
        String companyName = (String) companyTableModel.getValueAt(selectedRow, 1);
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete company '" + companyName + "'?", 
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (companyDAO.delete(companyId)) {
                JOptionPane.showMessageDialog(this, "Company deleted successfully!");
                clearForm();
                loadCompanies();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting company.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        pricePerKmField.setText("");
        pricePerKgField.setText("");
        handlingTimeField.setText("");
        airCheckBox.setSelected(false);
        roadCheckBox.setSelected(false);
        railCheckBox.setSelected(false);
        companyTable.clearSelection();
    }

    private void loadSelectedCompany() {
        int selectedRow = companyTable.getSelectedRow();
        if (selectedRow != -1) {
            int companyId = (Integer) companyTableModel.getValueAt(selectedRow, 0);
            CourierCompany company = companyDAO.findById(companyId);
            
            if (company != null) {
                nameField.setText(company.getName());
                pricePerKmField.setText(String.valueOf(company.getPricePerKm()));
                pricePerKgField.setText(String.valueOf(company.getPricePerKg()));
                handlingTimeField.setText(String.valueOf(company.getBaseHandlingTime()));
                
                // Set transport mode checkboxes
                String modes = company.getTransportModes();
                airCheckBox.setSelected(modes.contains("AIR"));
                roadCheckBox.setSelected(modes.contains("ROAD"));
                railCheckBox.setSelected(modes.contains("RAIL"));
            }
        }
    }

    private boolean validateCompanyForm() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter company name.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            Double.parseDouble(pricePerKmField.getText());
            Double.parseDouble(pricePerKgField.getText());
            Integer.parseInt(handlingTimeField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!airCheckBox.isSelected() && !roadCheckBox.isSelected() && !railCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please select at least one transport mode.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private String getSelectedTransportModes() {
        StringBuilder modes = new StringBuilder();
        if (airCheckBox.isSelected()) {
            if (modes.length() > 0) modes.append(",");
            modes.append("AIR");
        }
        if (roadCheckBox.isSelected()) {
            if (modes.length() > 0) modes.append(",");
            modes.append("ROAD");
        }
        if (railCheckBox.isSelected()) {
            if (modes.length() > 0) modes.append(",");
            modes.append("RAIL");
        }
        return modes.toString();
    }
}
