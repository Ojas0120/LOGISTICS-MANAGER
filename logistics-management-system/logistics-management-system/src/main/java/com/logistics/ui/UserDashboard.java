package com.logistics.ui;

import com.logistics.dao.CourierCompanyDAO;
import com.logistics.dao.ShipmentDAO;
import com.logistics.model.*;
import com.logistics.service.CalculationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UserDashboard extends JFrame {
    private User currentUser;
    private CourierCompanyDAO companyDAO;
    private ShipmentDAO shipmentDAO;
    private CalculationService calculationService;
    
    private JTable calculationTable;
    private DefaultTableModel calculationTableModel;
    private JComboBox<CourierCompany> companySelectionComboBox;
    private JButton selectCompanyButton;
    private JButton confirmOrderButton;
    
    // Form components
    private JTextField senderCityField, receiverCityField, distanceField, weightField;
    private JComboBox<Shipment.Priority> priorityComboBox;
    private JComboBox<Shipment.TrafficCondition> trafficComboBox;
    private JComboBox<CourierCompany.TransportMode> transportModeComboBox;
    private JButton calculateButton, clearButton, historyButton;
    
    private Shipment currentShipment;
    private List<ShipmentCalculation> currentCalculations;

    public UserDashboard(User user) {
        this.currentUser = user;
        this.companyDAO = new CourierCompanyDAO();
        this.shipmentDAO = new ShipmentDAO();
        this.calculationService = new CalculationService();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Logistics Management System - User Dashboard");
        getContentPane().setBackground(UIStyles.BG_COLOR);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
    }

    private void initializeComponents() {
        // Calculation results table
        calculationTableModel = new DefaultTableModel(new String[]{"Company", "Total Cost (₹)", "Delivery Time (hrs)", "Cost per Km (₹)", "Cost per Kg (₹)", "Select"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the Select column is editable
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 5) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };
        calculationTable = new JTable(calculationTableModel);
        UIStyles.styleTable(calculationTable);
        calculationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Company selection components
        companySelectionComboBox = new JComboBox<>();
        selectCompanyButton = UIStyles.createSecondaryButton("✓ Select Company");
        confirmOrderButton = UIStyles.createPrimaryButton("✅ Confirm Order", UIStyles.SUCCESS_COLOR);
        confirmOrderButton.setEnabled(false);
        
        // Form components
        senderCityField = UIStyles.createStyledTextField(20);
        receiverCityField = UIStyles.createStyledTextField(20);
        distanceField = UIStyles.createStyledTextField(10);
        weightField = UIStyles.createStyledTextField(10);
        
        priorityComboBox = new JComboBox<>(Shipment.Priority.values());
        trafficComboBox = new JComboBox<>(Shipment.TrafficCondition.values());
        transportModeComboBox = new JComboBox<>(CourierCompany.TransportMode.values());
        
        calculateButton = UIStyles.createPrimaryButton("💰 Calculate Costs", UIStyles.PRIMARY_COLOR);
        clearButton = UIStyles.createSecondaryButton("🗑️ Clear Form");
        historyButton = UIStyles.createPrimaryButton("📜 View History", UIStyles.SUCCESS_COLOR);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Top panel with gradient header
        JPanel topPanel = UIStyles.createHeaderPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        
        JLabel welcomeLabel = new JLabel("👤 Welcome, " + currentUser.getUsername() + " (User)");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JButton logoutButton = UIStyles.createSecondaryButton("Logout");
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        topPanel.add(logoutButton, BorderLayout.EAST);
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Shipment form
        JPanel formPanel = createShipmentForm();
        
        // Calculation results
        JScrollPane tableScrollPane = new JScrollPane(calculationTable);
        tableScrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "Cost Comparison Results - Click checkbox to select a company",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        tableScrollPane.setBackground(Color.WHITE);
        tableScrollPane.setPreferredSize(new Dimension(900, 300));
        
        // Company selection panel
        JPanel companySelectionPanel = createCompanySelectionPanel();
        
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(companySelectionPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        
        // Event handlers
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private JPanel createShipmentForm() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "Shipment Details",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(10, 15, 15, 15)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Sender City
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Sender City:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(senderCityField, gbc);
        
        // Receiver City
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Receiver City:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(receiverCityField, gbc);
        
        // Distance
        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Distance (km):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(distanceField, gbc);
        
        // Weight
        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Weight (kg):"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(weightField, gbc);
        
        // Priority
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Priority:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(priorityComboBox, gbc);
        
        // Traffic Condition
        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Traffic Condition:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(trafficComboBox, gbc);
        
        // Transport Mode
        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.EAST;
        panel.add(new JLabel("Transport Mode:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(transportModeComboBox, gbc);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(historyButton);
        
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }

    private JPanel createCompanySelectionPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_COLOR, 1),
                "Company Selection & Order Confirmation",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                UIStyles.TEXT_COLOR
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.add(new JLabel("Selected Company:"));
        leftPanel.add(companySelectionComboBox);
        leftPanel.add(selectCompanyButton);
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(confirmOrderButton);
        
        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.EAST);
        
        return panel;
    }

    private void setupEventHandlers() {
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateCosts();
            }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHistory();
            }
        });
        
        selectCompanyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectCompanyFromTable();
            }
        });
        
        confirmOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmOrder();
            }
        });
        
        // Add checkbox change listener
        calculationTable.getModel().addTableModelListener(e -> {
            if (e.getColumn() == 5) { // Select column
                updateCompanySelection();
            }
        });
    }

    private void calculateCosts() {
        if (!validateForm()) {
            return;
        }
        
        try {
            // Create shipment object
            currentShipment = new Shipment();
            currentShipment.setUserId(currentUser.getId());
            currentShipment.setSenderCity(senderCityField.getText().trim());
            currentShipment.setReceiverCity(receiverCityField.getText().trim());
            currentShipment.setDistance(Double.parseDouble(distanceField.getText()));
            currentShipment.setWeight(Double.parseDouble(weightField.getText()));
            currentShipment.setPriority((Shipment.Priority) priorityComboBox.getSelectedItem());
            currentShipment.setTrafficCondition((Shipment.TrafficCondition) trafficComboBox.getSelectedItem());
            currentShipment.setTransportMode((CourierCompany.TransportMode) transportModeComboBox.getSelectedItem());
            
            System.out.println("Creating shipment for user ID: " + currentUser.getId());
            System.out.println("Shipment details: " + currentShipment.toString());
            
            // Calculate costs for all companies
            currentCalculations = calculationService.calculateShipmentCosts(currentShipment);
            System.out.println("Calculated costs for " + currentCalculations.size() + " companies");
            
            // Display results
            displayCalculations(currentCalculations);
            
            JOptionPane.showMessageDialog(this, "Cost calculation completed! Select a company to proceed with your order.");
            
        } catch (Exception e) {
            System.err.println("Error in calculateCosts: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error calculating costs: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displayCalculations(List<ShipmentCalculation> calculations) {
        calculationTableModel.setRowCount(0);
        
        for (int i = 0; i < calculations.size(); i++) {
            ShipmentCalculation calculation = calculations.get(i);
            CourierCompany company = companyDAO.findById(calculation.getCompanyId());
            if (company != null) {
                Object[] row = {
                    company.getName(),
                    String.format("₹%.2f", calculation.getTotalCost()),
                    calculation.getEstimatedDeliveryTime(),
                    String.format("₹%.2f", company.getPricePerKm()),
                    String.format("₹%.2f", company.getPricePerKg()),
                    false // Checkbox for selection
                };
                calculationTableModel.addRow(row);
            }
        }
    }

    private void selectCompanyFromTable() {
        int selectedRow = calculationTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a company from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (currentCalculations == null || selectedRow >= currentCalculations.size()) {
            JOptionPane.showMessageDialog(this, "No calculations available. Please calculate costs first.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        ShipmentCalculation selectedCalculation = currentCalculations.get(selectedRow);
        CourierCompany selectedCompany = companyDAO.findById(selectedCalculation.getCompanyId());
        
        if (selectedCompany != null) {
            companySelectionComboBox.removeAllItems();
            companySelectionComboBox.addItem(selectedCompany);
            confirmOrderButton.setEnabled(true);
            
            JOptionPane.showMessageDialog(this, 
                String.format("Company selected: %s\nTotal Cost: ₹%.2f\nDelivery Time: %d hours", 
                    selectedCompany.getName(), 
                    selectedCalculation.getTotalCost(), 
                    selectedCalculation.getEstimatedDeliveryTime()),
                "Company Selected", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateCompanySelection() {
        // This method is called when checkboxes are clicked
        // You can add logic here if needed
    }

    private void confirmOrder() {
        if (currentShipment == null) {
            JOptionPane.showMessageDialog(this, "No shipment data available. Please calculate costs first.", "No Data", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        CourierCompany selectedCompany = (CourierCompany) companySelectionComboBox.getSelectedItem();
        if (selectedCompany == null) {
            JOptionPane.showMessageDialog(this, "Please select a company first.", "No Company Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Set the selected company and status
            currentShipment.setSelectedCompanyId(selectedCompany.getId());
            currentShipment.setStatus(Shipment.Status.CONFIRMED);
            
            System.out.println("Attempting to save shipment with company ID: " + selectedCompany.getId());
            System.out.println("Shipment status: " + currentShipment.getStatus());
            
            // Save shipment to database
            if (shipmentDAO.create(currentShipment)) {
                System.out.println("Order confirmed successfully with ID: " + currentShipment.getId());
                
                // Save calculations to database
                calculationService.saveCalculations(currentCalculations);
                
                JOptionPane.showMessageDialog(this, 
                    String.format("Order confirmed successfully!\nOrder ID: %d\nCompany: %s\nStatus: %s", 
                        currentShipment.getId(), 
                        selectedCompany.getName(), 
                        currentShipment.getStatus()),
                    "Order Confirmed", JOptionPane.INFORMATION_MESSAGE);
                
                // Reset form
                clearForm();
            } else {
                System.err.println("Failed to create shipment in database");
                JOptionPane.showMessageDialog(this, "Error confirming order. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            System.err.println("Error in confirmOrder: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error confirming order: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        senderCityField.setText("");
        receiverCityField.setText("");
        distanceField.setText("");
        weightField.setText("");
        priorityComboBox.setSelectedIndex(0);
        trafficComboBox.setSelectedIndex(0);
        transportModeComboBox.setSelectedIndex(0);
        calculationTableModel.setRowCount(0);
        companySelectionComboBox.removeAllItems();
        confirmOrderButton.setEnabled(false);
        currentShipment = null;
        currentCalculations = null;
    }

    private void showHistory() {
        List<Shipment> shipments = shipmentDAO.findByUserId(currentUser.getId());
        
        if (shipments.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No shipment history found.", "History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create history dialog
        JDialog historyDialog = new JDialog(this, "Order History", true);
        historyDialog.setSize(1000, 600);
        historyDialog.setLocationRelativeTo(this);
        
        // Create table for history
        String[] columns = {"Order ID", "From", "To", "Distance", "Weight", "Priority", "Mode", "Company", "Status", "Date"};
        DefaultTableModel historyModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable historyTable = new JTable(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        // Populate history table
        for (Shipment shipment : shipments) {
            String companyName = "Not Selected";
            if (shipment.getSelectedCompanyId() != null) {
                CourierCompany company = companyDAO.findById(shipment.getSelectedCompanyId());
                if (company != null) {
                    companyName = company.getName();
                }
            }
            
            Object[] row = {
                shipment.getId(),
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
            historyModel.addRow(row);
        }
        
        historyDialog.add(scrollPane, BorderLayout.CENTER);
        historyDialog.setVisible(true);
    }

    private boolean validateForm() {
        if (senderCityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter sender city.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (receiverCityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter receiver city.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            double distance = Double.parseDouble(distanceField.getText());
            double weight = Double.parseDouble(weightField.getText());
            
            if (distance <= 0) {
                JOptionPane.showMessageDialog(this, "Distance must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (weight <= 0) {
                JOptionPane.showMessageDialog(this, "Weight must be greater than 0.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for distance and weight.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
}
