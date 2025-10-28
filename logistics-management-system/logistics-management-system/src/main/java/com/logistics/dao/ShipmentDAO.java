package com.logistics.dao;

import com.logistics.model.Shipment;
import com.logistics.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentDAO {
    private final DatabaseConnection dbConnection;

    public ShipmentDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public Shipment findById(int id) {
        String sql = "SELECT * FROM shipments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToShipment(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding shipment by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<Shipment> findByUserId(int userId) {
        String sql = "SELECT * FROM shipments WHERE user_id = ? ORDER BY created_at DESC";
        List<Shipment> shipments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    shipments.add(mapResultSetToShipment(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding shipments by user ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return shipments;
    }

    public List<Shipment> findAll() {
        String sql = "SELECT * FROM shipments ORDER BY created_at DESC";
        List<Shipment> shipments = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                shipments.add(mapResultSetToShipment(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all shipments: " + e.getMessage());
            e.printStackTrace();
        }
        
        return shipments;
    }

    public boolean create(Shipment shipment) {
        String sql = "INSERT INTO shipments (user_id, sender_city, receiver_city, distance, weight, priority, traffic_condition, transport_mode, selected_company_id, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, shipment.getUserId());
            stmt.setString(2, shipment.getSenderCity());
            stmt.setString(3, shipment.getReceiverCity());
            stmt.setDouble(4, shipment.getDistance());
            stmt.setDouble(5, shipment.getWeight());
            stmt.setString(6, shipment.getPriority().name());
            stmt.setString(7, shipment.getTrafficCondition().name());
            stmt.setString(8, shipment.getTransportMode().name());
            stmt.setObject(9, shipment.getSelectedCompanyId());
            stmt.setString(10, shipment.getStatus().name());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated key
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        shipment.setId(generatedKeys.getInt(1));
                        System.out.println("Shipment created with ID: " + shipment.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating shipment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean update(Shipment shipment) {
        String sql = "UPDATE shipments SET sender_city = ?, receiver_city = ?, distance = ?, weight = ?, priority = ?, traffic_condition = ?, transport_mode = ?, selected_company_id = ?, status = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, shipment.getSenderCity());
            stmt.setString(2, shipment.getReceiverCity());
            stmt.setDouble(3, shipment.getDistance());
            stmt.setDouble(4, shipment.getWeight());
            stmt.setString(5, shipment.getPriority().name());
            stmt.setString(6, shipment.getTrafficCondition().name());
            stmt.setString(7, shipment.getTransportMode().name());
            stmt.setObject(8, shipment.getSelectedCompanyId());
            stmt.setString(9, shipment.getStatus().name());
            stmt.setInt(10, shipment.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating shipment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM shipments WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting shipment: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private Shipment mapResultSetToShipment(ResultSet rs) throws SQLException {
        Shipment shipment = new Shipment();
        shipment.setId(rs.getInt("id"));
        shipment.setUserId(rs.getInt("user_id"));
        shipment.setSenderCity(rs.getString("sender_city"));
        shipment.setReceiverCity(rs.getString("receiver_city"));
        shipment.setDistance(rs.getDouble("distance"));
        shipment.setWeight(rs.getDouble("weight"));
        shipment.setPriority(Shipment.Priority.valueOf(rs.getString("priority")));
        shipment.setTrafficCondition(Shipment.TrafficCondition.valueOf(rs.getString("traffic_condition")));
        shipment.setTransportMode(com.logistics.model.CourierCompany.TransportMode.valueOf(rs.getString("transport_mode")));
        
        // Handle selected_company_id (can be null)
        int selectedCompanyId = rs.getInt("selected_company_id");
        if (!rs.wasNull()) {
            shipment.setSelectedCompanyId(selectedCompanyId);
        }
        
        // Handle status
        String statusStr = rs.getString("status");
        if (statusStr != null) {
            shipment.setStatus(Shipment.Status.valueOf(statusStr));
        } else {
            shipment.setStatus(Shipment.Status.PENDING);
        }
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            shipment.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return shipment;
    }
}
