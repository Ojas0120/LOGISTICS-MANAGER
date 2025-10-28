#!/bin/bash
echo "Starting Logistics Management System with MySQL..."
echo "Features:"
echo "- MySQL Database Integration"
echo "- User Registration (Register New User button)"
echo "- Shipment Cost Calculation in Rupees (₹)"
echo "- Admin Dashboard for Company Management"
echo "- User Dashboard for Shipment Management"
echo "- 25+ Courier Companies with Different Rates"
echo ""
echo "Make sure MySQL is running on localhost:3306"
echo "Default MySQL credentials: root/password"
echo ""
java -cp "target/classes:lib/mysql-connector-java-8.0.33.jar:lib/sqlite-jdbc-3.44.1.0.jar:lib/slf4j-api-1.7.36.jar:lib/slf4j-simple-1.7.36.jar" com.logistics.LogisticsManagementSystem
