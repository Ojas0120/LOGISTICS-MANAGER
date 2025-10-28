package com.logistics.dao;

import com.logistics.model.ShipmentCalculation;
import com.logistics.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShipmentCalculationDAO {
    private final DatabaseConnection dbConnection;

    public ShipmentCalculationDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public List<ShipmentCalculation> findByShipmentId(int shipmentId) {
        String sql = "SELECT * FROM shipment_calculations WHERE shipment_id = ? ORDER BY total_cost";
        List<ShipmentCalculation> calculations = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shipmentId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    calculations.add(mapResultSetToShipmentCalculation(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding calculations by shipment ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return calculations;
    }

    public List<ShipmentCalculation> findByCompanyId(int companyId) {
        String sql = "SELECT * FROM shipment_calculations WHERE company_id = ? ORDER BY created_at DESC";
        List<ShipmentCalculation> calculations = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, companyId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    calculations.add(mapResultSetToShipmentCalculation(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding calculations by company ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return calculations;
    }

    public boolean create(ShipmentCalculation calculation) {
        String sql = "INSERT INTO shipment_calculations (shipment_id, company_id, total_cost, estimated_delivery_time) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, calculation.getShipmentId());
            stmt.setInt(2, calculation.getCompanyId());
            stmt.setDouble(3, calculation.getTotalCost());
            stmt.setInt(4, calculation.getEstimatedDeliveryTime());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated key
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        calculation.setId(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating shipment calculation: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean deleteByShipmentId(int shipmentId) {
        String sql = "DELETE FROM shipment_calculations WHERE shipment_id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shipmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting calculations by shipment ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM shipment_calculations WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting shipment calculation: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private ShipmentCalculation mapResultSetToShipmentCalculation(ResultSet rs) throws SQLException {
        ShipmentCalculation calculation = new ShipmentCalculation();
        calculation.setId(rs.getInt("id"));
        calculation.setShipmentId(rs.getInt("shipment_id"));
        calculation.setCompanyId(rs.getInt("company_id"));
        calculation.setTotalCost(rs.getDouble("total_cost"));
        calculation.setEstimatedDeliveryTime(rs.getInt("estimated_delivery_time"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            calculation.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return calculation;
    }
}
