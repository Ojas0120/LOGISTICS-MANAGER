# Logistics Management System

A comprehensive Java Swing desktop application for logistics management that helps users calculate and compare shipment costs and delivery times offered by different courier companies. **Now with MySQL Database Integration!**

## Features

### 🔐 User Authentication
- **Admin Role**: Full access to company management and system administration
- **User Role**: Access to shipment calculation and personal history
- Secure login system with default credentials
- **User Registration**: New users can register through the application

### 🏢 Admin Dashboard
- **Company Management**: Add, edit, and delete courier companies
- **Company Details**: Configure pricing (per km/kg), handling time, and transport modes
- **Order History**: View all orders across the system with company selections
- **Order Status Management**: Update order status (PENDING, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED)
- **User Management**: Manage system users

### 📦 User Dashboard
- **Shipment Calculator**: Calculate costs and delivery times for different companies
- **Cost Comparison**: Side-by-side comparison of all available courier companies
- **Company Selection**: Select preferred company from comparison table
- **Order Confirmation**: Complete order placement process
- **Real-time Calculations**: Instant cost and delivery time estimates
- **Personal History**: View past orders with company details

### 💰 Cost Calculation Formula (in Rupees ₹)
```
Total Cost = (price_per_km × distance) + (price_per_kg × weight) + Priority Surcharge
```

### ⏱️ Delivery Time Calculation
```
Delivery Time = (distance / transport_speed) + base_handling_time + traffic_delay
```

## Technology Stack

- **Java 11+**
- **Java Swing** for GUI
- **MySQL** for database (with JDBC)
- **Maven** for dependency management
- **SLF4J** for logging

## Prerequisites

- Java 11 or higher
- MySQL Server 8.0 or higher
- Maven 3.6 or higher (optional)

## Database Setup

### 1. Install MySQL
- Download and install MySQL Server from [mysql.com](https://dev.mysql.com/downloads/mysql/)
- Start MySQL service
- Default port: 3306

### 2. Create Database
```sql
-- Run this in MySQL command line or MySQL Workbench
CREATE DATABASE IF NOT EXISTS logistics_db;
```

### 3. Update Database Credentials (if needed)
Edit `src/main/java/com/logistics/util/DatabaseConnection.java`:
```java
private static final String DB_USER = "root";           // Your MySQL username
private static final String DB_PASSWORD = "password";   // Your MySQL password
```

## Quick Start

### Option 1: Using Run Scripts (Recommended)

**On macOS/Linux:**
```bash
./run.sh
```

**On Windows:**
```cmd
run.bat
```

### Option 2: Manual Compilation and Execution

1. **Compile the project:**
```bash
javac -cp "lib/mysql-connector-java-8.0.33.jar:lib/sqlite-jdbc-3.44.1.0.jar" -d target/classes src/main/java/com/logistics/*.java src/main/java/com/logistics/model/*.java src/main/java/com/logistics/dao/*.java src/main/java/com/logistics/service/*.java src/main/java/com/logistics/ui/*.java src/main/java/com/logistics/util/*.java
```

2. **Run the application:**
```bash
java -cp "target/classes:lib/mysql-connector-java-8.0.33.jar:lib/sqlite-jdbc-3.44.1.0.jar:lib/slf4j-api-1.7.36.jar:lib/slf4j-simple-1.7.36.jar" com.logistics.LogisticsManagementSystem
```

**On Windows (use semicolons instead of colons):**
```cmd
java -cp "target/classes;lib/mysql-connector-java-8.0.33.jar;lib/sqlite-jdbc-3.44.1.0.jar;lib/slf4j-api-1.7.36.jar;lib/slf4j-simple-1.7.36.jar" com.logistics.LogisticsManagementSystem
```

## Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| User | user | user123 |

## Database Schema (MySQL)

The application uses MySQL database with the following tables:

- **users**: User accounts and authentication
- **courier_companies**: Courier company information and pricing
- **shipments**: Shipment details and history with company selection
- **shipment_calculations**: Calculated costs and delivery times

## Sample Data

The application comes pre-loaded with **25 diverse courier companies**:

### Premium Air Services (5 companies)
- SkyExpress Premium, AeroLogistics Elite, JetCargo International, AirSpeed Express, Falcon Delivery
- Rates: ₹3.90-₹5.20/km, ₹25-₹32/kg

### Standard Air Services (5 companies)
- Cloud Courier, Wing Logistics, SkyLink Transport, AirBridge Express, Eagle Cargo
- Rates: ₹2.80-₹3.70/km, ₹18-₹24/kg

### Premium Road Services (5 companies)
- RoadMaster Premium, Highway Express Elite, TruckStar Logistics, RoadRunner Express, Velocity Transport
- Rates: ₹2.50-₹3.20/km, ₹15-₹20/kg

### Standard Road Services (5 companies)
- City Logistics, Metro Delivery, Urban Express, Local Logistics, Quick Delivery
- Rates: ₹1.20-₹2.20/km, ₹8-₹14/kg

### Rail Services (5 companies)
- RailCargo Express, Iron Horse Logistics, TrackMaster, RailBridge Transport, Steel Wheels
- Rates: ₹1.60-₹2.00/km, ₹11-₹13/kg

### Multi-Modal Services (5 companies)
- Universal Logistics, Global Express, MultiMode Transport, FlexiCargo, Omni Logistics
- Support multiple transport modes

## Usage Guide

### For Users
1. **Login** with user credentials
2. **Enter Shipment Details**:
   - Sender and receiver cities
   - Distance and weight
   - Priority level (affects surcharge)
   - Traffic conditions (affects delivery time)
   - Preferred transport mode
3. **Calculate Costs** to see comparison table with all companies
4. **Select Company** by clicking on preferred company in the table
5. **Confirm Order** to place the order
6. **View History** to see past orders with company details

### For Admins
1. **Login** with admin credentials
2. **Manage Companies**:
   - Add new courier companies
   - Edit existing company details
   - Delete inactive companies
3. **View Order History** across all users with company selections
4. **Update Order Status** to track order workflow
5. **Monitor System** usage and performance

## Transport Modes & Speeds

| Mode | Average Speed | Best For |
|------|---------------|----------|
| Air | 800 km/h | Long distances, urgent deliveries |
| Rail | 100 km/h | Heavy cargo, medium distances |
| Road | 60 km/h | Short distances, local delivery |

## Priority Levels & Surcharges (in Rupees)

| Priority | Surcharge | Description |
|----------|-----------|-------------|
| Low | ₹0.00 | Standard delivery |
| Normal | ₹0.00 | Regular priority |
| High | ₹50.00 | Express delivery |
| Urgent | ₹100.00 | Same-day/next-day delivery |

## Traffic Conditions & Delays

| Condition | Delay Factor | Description |
|-----------|--------------|-------------|
| Low | 0% | Minimal traffic impact |
| Normal | 20% | Standard traffic delays |
| High | 50% | Heavy traffic conditions |

## Project Structure

```
logistics-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/logistics/
│   │   │   ├── model/          # Data models
│   │   │   ├── dao/            # Data Access Objects (MySQL)
│   │   │   ├── service/        # Business logic
│   │   │   ├── ui/             # User interface
│   │   │   └── util/           # Utilities
│   │   └── resources/
│   │       └── database_schema.sql
│   └── test/java/              # Test classes
├── lib/                        # Dependencies
├── target/classes/             # Compiled classes
├── setup_mysql.sql            # MySQL setup script
├── run.sh                      # Unix/Linux run script
├── run.bat                     # Windows run script
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## Dependencies Included

- **mysql-connector-java-8.0.33.jar** - MySQL database driver
- **sqlite-jdbc-3.44.1.0.jar** - SQLite database driver (backup)
- **slf4j-api-1.7.36.jar** - SLF4J logging API
- **slf4j-simple-1.7.36.jar** - SLF4J simple implementation

## Troubleshooting

### Common Issues

1. **MySQL Connection Error**
   - Ensure MySQL is running on localhost:3306
   - Check database credentials in DatabaseConnection.java
   - Verify database 'logistics_db' exists

2. **GUI Not Displaying**
   - Verify Java version compatibility (Java 11+)
   - Check system look and feel support

3. **ClassNotFoundException**
   - Ensure all dependencies are in the classpath
   - Recompile the project if needed

4. **Shipment Registration Issues**
   - Check MySQL connection
   - Verify database tables are created
   - Check console output for error messages

### System Requirements

- **Minimum RAM**: 512MB
- **Disk Space**: 100MB
- **OS**: Windows, macOS, or Linux
- **Java Version**: 11 or higher
- **MySQL Version**: 8.0 or higher

## Features Implemented

✅ **Complete Authentication System**
✅ **Admin Dashboard with Company Management**
✅ **User Dashboard with Cost Calculation**
✅ **Real-time Cost and Time Calculations**
✅ **MySQL Database Integration**
✅ **25+ Sample Companies Pre-loaded**
✅ **Cross-platform Compatibility**
✅ **Comprehensive Error Handling**
✅ **User Registration System**
✅ **Company Selection and Order Confirmation**
✅ **Order Status Management**
✅ **Currency Display in Rupees (₹)**

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is open source and available under the MIT License.

## Support

For support and questions, please create an issue in the project repository.

---

**Note**: This is a desktop application designed for offline use with MySQL database. Make sure MySQL is running before starting the application.
