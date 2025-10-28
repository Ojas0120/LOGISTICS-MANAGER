package com.logistics.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseConnection {
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/logistics_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String SQLITE_URL = "jdbc:sqlite:logistics.db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "password";
    private static final String SCHEMA_FILE = "src/main/resources/database_schema.sql";
    private static DatabaseConnection instance;
    private Connection connection;
    private boolean useMySQL = true;

    static {
        try {
            // Load both drivers
            Class.forName("com.mysql.cj.jdbc.Driver");
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Database drivers not found: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private DatabaseConnection() {
        initializeDatabase();
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (useMySQL) {
                try {
                    connection = DriverManager.getConnection(MYSQL_URL, DB_USER, DB_PASSWORD);
                    System.out.println("Connected to MySQL database");
                } catch (SQLException e) {
                    System.err.println("MySQL connection failed, falling back to SQLite: " + e.getMessage());
                    useMySQL = false;
                    connection = DriverManager.getConnection(SQLITE_URL);
                    System.out.println("Connected to SQLite database");
                }
            } else {
                connection = DriverManager.getConnection(SQLITE_URL);
            }
        }
        return connection;
    }

    private void initializeDatabase() {
        try {
            if (useMySQL) {
                initializeMySQL();
            } else {
                initializeSQLite();
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initializeMySQL() {
        try (Connection conn = DriverManager.getConnection(MYSQL_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // Read and execute schema file
            String schema = new String(Files.readAllBytes(Paths.get(SCHEMA_FILE)));
            String[] statements = schema.split(";");
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty() && !statement.startsWith("--")) {
                    try {
                        stmt.execute(statement);
                    } catch (SQLException e) {
                        // Ignore errors for existing tables/data
                        if (!e.getMessage().contains("already exists") && 
                            !e.getMessage().contains("Duplicate entry")) {
                            System.err.println("Warning: " + e.getMessage());
                        }
                    }
                }
            }
            
            System.out.println("MySQL Database initialized successfully!");
            
        } catch (SQLException | IOException e) {
            System.err.println("Error initializing MySQL database: " + e.getMessage());
            System.err.println("Falling back to SQLite...");
            useMySQL = false;
            initializeSQLite();
        }
    }

    private void initializeSQLite() {
        try (Connection conn = DriverManager.getConnection(SQLITE_URL);
             Statement stmt = conn.createStatement()) {
            
            // Create tables for SQLite
            String sqliteSchema = getSQLiteSchema();
            String[] statements = sqliteSchema.split(";");
            
            for (String statement : statements) {
                statement = statement.trim();
                if (!statement.isEmpty()) {
                    stmt.execute(statement);
                }
            }
            
            // Insert default users
            insertDefaultUsers(stmt);
            insertDefaultCompanies(stmt);
            
            System.out.println("SQLite Database initialized successfully!");
            
        } catch (SQLException e) {
            System.err.println("Error initializing SQLite database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String getSQLiteSchema() {
        return "-- SQLite Schema for Logistics Management System\n" +
               "CREATE TABLE IF NOT EXISTS users (\n" +
               "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
               "    username VARCHAR(50) UNIQUE NOT NULL,\n" +
               "    password VARCHAR(255) NOT NULL,\n" +
               "    email VARCHAR(100) NOT NULL,\n" +
               "    role VARCHAR(20) NOT NULL CHECK (role IN ('ADMIN', 'USER')),\n" +
               "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
               ");\n" +
               "\n" +
               "CREATE TABLE IF NOT EXISTS courier_companies (\n" +
               "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
               "    name VARCHAR(100) NOT NULL,\n" +
               "    price_per_km DECIMAL(10,2) NOT NULL,\n" +
               "    price_per_kg DECIMAL(10,2) NOT NULL,\n" +
               "    base_handling_time INTEGER NOT NULL,\n" +
               "    transport_modes VARCHAR(50) NOT NULL,\n" +
               "    is_active BOOLEAN DEFAULT 1,\n" +
               "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP\n" +
               ");\n" +
               "\n" +
               "CREATE TABLE IF NOT EXISTS shipments (\n" +
               "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
               "    user_id INTEGER NOT NULL,\n" +
               "    sender_city VARCHAR(100) NOT NULL,\n" +
               "    receiver_city VARCHAR(100) NOT NULL,\n" +
               "    distance DECIMAL(10,2) NOT NULL,\n" +
               "    weight DECIMAL(10,2) NOT NULL,\n" +
               "    priority VARCHAR(20) NOT NULL CHECK (priority IN ('LOW', 'NORMAL', 'HIGH', 'URGENT')),\n" +
               "    traffic_condition VARCHAR(20) NOT NULL CHECK (traffic_condition IN ('LOW', 'NORMAL', 'HIGH')),\n" +
               "    transport_mode VARCHAR(20) NOT NULL CHECK (transport_mode IN ('AIR', 'ROAD', 'RAIL')),\n" +
               "    selected_company_id INTEGER,\n" +
               "    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'CONFIRMED', 'IN_TRANSIT', 'DELIVERED', 'CANCELLED')),\n" +
               "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
               "    FOREIGN KEY (user_id) REFERENCES users(id)\n" +
               ");\n" +
               "\n" +
               "CREATE TABLE IF NOT EXISTS shipment_calculations (\n" +
               "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
               "    shipment_id INTEGER NOT NULL,\n" +
               "    company_id INTEGER NOT NULL,\n" +
               "    total_cost DECIMAL(10,2) NOT NULL,\n" +
               "    estimated_delivery_time INTEGER NOT NULL,\n" +
               "    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,\n" +
               "    FOREIGN KEY (shipment_id) REFERENCES shipments(id),\n" +
               "    FOREIGN KEY (company_id) REFERENCES courier_companies(id)\n" +
               ");";
    }

    private void insertDefaultUsers(Statement stmt) throws SQLException {
        String[] users = {
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('admin', 'admin123', 'admin@logistics.com', 'ADMIN')",
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('user', 'user123', 'user@logistics.com', 'USER')",
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('john', 'john123', 'john@example.com', 'USER')",
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('sarah', 'sarah123', 'sarah@example.com', 'USER')",
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('mike', 'mike123', 'mike@example.com', 'USER')",
            "INSERT OR IGNORE INTO users (username, password, email, role) VALUES ('admin2', 'admin456', 'admin2@logistics.com', 'ADMIN')"
        };
        
        for (String user : users) {
            try {
                stmt.execute(user);
            } catch (SQLException e) {
                // Ignore if user already exists
            }
        }
    }

    private void insertDefaultCompanies(Statement stmt) throws SQLException {
        String[] companies = {
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('SkyExpress Premium', 4.50, 28.00, 1, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('AeroLogistics Elite', 5.20, 32.00, 1, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('JetCargo International', 4.80, 30.00, 1, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('AirSpeed Express', 3.90, 25.00, 2, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Falcon Delivery', 4.20, 26.00, 1, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Cloud Courier', 3.20, 20.00, 2, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Wing Logistics', 3.80, 18.00, 3, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('SkyLink Transport', 3.50, 22.00, 2, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('AirBridge Express', 3.00, 19.00, 2, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Eagle Cargo', 3.70, 24.00, 1, 'AIR')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('RoadMaster Premium', 2.80, 16.00, 2, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Highway Express Elite', 3.20, 20.00, 2, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('TruckStar Logistics', 2.50, 15.00, 3, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('RoadRunner Express', 2.90, 17.00, 2, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Velocity Transport', 3.10, 19.00, 2, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('City Logistics', 1.50, 10.00, 3, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Metro Delivery', 1.80, 12.00, 4, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Urban Express', 2.00, 13.00, 3, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Local Logistics', 1.20, 8.00, 5, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Quick Delivery', 2.20, 14.00, 3, 'ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('RailCargo Express', 1.80, 12.00, 4, 'RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Iron Horse Logistics', 1.60, 11.00, 5, 'RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('TrackMaster', 2.00, 13.00, 4, 'RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('RailBridge Transport', 1.70, 12.00, 4, 'RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Steel Wheels', 1.90, 13.00, 4, 'RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Universal Logistics', 2.50, 15.00, 3, 'AIR,ROAD,RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Global Express', 3.00, 18.00, 2, 'AIR,ROAD')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('MultiMode Transport', 2.20, 14.00, 3, 'ROAD,RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('FlexiCargo', 2.80, 17.00, 2, 'AIR,ROAD,RAIL')",
            "INSERT OR IGNORE INTO courier_companies (name, price_per_km, price_per_kg, base_handling_time, transport_modes) VALUES ('Omni Logistics', 2.60, 16.00, 3, 'AIR,ROAD,RAIL')"
        };
        
        for (String company : companies) {
            try {
                stmt.execute(company);
            } catch (SQLException e) {
                // Ignore if company already exists
            }
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }

    public boolean isUsingMySQL() {
        return useMySQL;
    }
}
