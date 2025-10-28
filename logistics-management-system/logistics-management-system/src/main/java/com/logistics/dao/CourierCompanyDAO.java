package com.logistics.dao;

import com.logistics.model.CourierCompany;
import com.logistics.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourierCompanyDAO {
    private final DatabaseConnection dbConnection;

    public CourierCompanyDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public CourierCompany findById(int id) {
        String sql = "SELECT * FROM courier_companies WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCourierCompany(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding courier company by ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }

    public List<CourierCompany> findAll() {
        String sql = "SELECT * FROM courier_companies WHERE is_active = 1 ORDER BY name";
        List<CourierCompany> companies = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                companies.add(mapResultSetToCourierCompany(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error finding all courier companies: " + e.getMessage());
            e.printStackTrace();
        }
        
        return companies;
    }

    public List<CourierCompany> findByTransportMode(CourierCompany.TransportMode mode) {
        String sql = "SELECT * FROM courier_companies WHERE is_active = 1 AND transport_modes LIKE ? ORDER BY name";
        List<CourierCompany> companies = new ArrayList<>();
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + mode.name() + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    companies.add(mapResultSetToCourierCompany(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error finding courier companies by transport mode: " + e.getMessage());
            e.printStackTrace();
        }
        
        return companies;
    }

    public boolean create(CourierCompany company) {
        String sql = "INSERT INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, company.getName());
            stmt.setDouble(2, company.getPricePerKm());
            stmt.setDouble(3, company.getPricePerKg());
            stmt.setInt(4, company.getBaseHandlingTime());
            stmt.setString(5, company.getTransportModes());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the generated key
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        company.setId(generatedKeys.getInt(1));
                        System.out.println("Company created with ID: " + company.getId());
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating courier company: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean update(CourierCompany company) {
        String sql = "UPDATE courier_companies SET name = ?, price_per_km = ?, price_per_kg = ?, base_handling_time = ?, transport_modes = ?, is_active = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, company.getName());
            stmt.setDouble(2, company.getPricePerKm());
            stmt.setDouble(3, company.getPricePerKg());
            stmt.setInt(4, company.getBaseHandlingTime());
            stmt.setString(5, company.getTransportModes());
            stmt.setBoolean(6, company.isActive());
            stmt.setInt(7, company.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating courier company: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    public boolean delete(int id) {
        String sql = "UPDATE courier_companies SET is_active = 0 WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting courier company: " + e.getMessage());
            e.printStackTrace();
        }
        
        return false;
    }

    private CourierCompany mapResultSetToCourierCompany(ResultSet rs) throws SQLException {
        CourierCompany company = new CourierCompany();
        company.setId(rs.getInt("id"));
        company.setName(rs.getString("name"));
        company.setPricePerKm(rs.getDouble("price_per_km"));
        company.setPricePerKg(rs.getDouble("price_per_kg"));
        company.setBaseHandlingTime(rs.getInt("base_handling_time"));
        company.setTransportModes(rs.getString("transport_modes"));
        company.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            company.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return company;
    }
}
