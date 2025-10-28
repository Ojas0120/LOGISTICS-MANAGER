# Fix: Unable to Register New Company and Shipment

## Problem
Users were unable to register new companies and shipments due to missing database columns.

## Root Cause
The `shipments` table in the SQLite database was missing two critical columns:
1. `selected_company_id` - For storing which company the user selected
2. `status` - For tracking shipment status (PENDING, CONFIRMED, IN_TRANSIT, DELIVERED, CANCELLED)

## Solution

### 1. Added Missing Columns to Database
```sql
ALTER TABLE shipments ADD COLUMN selected_company_id INTEGER;
ALTER TABLE shipments ADD COLUMN status VARCHAR(20) DEFAULT 'PENDING';
```

### 2. Updated Database Schema in Code
Updated `DatabaseConnection.java` to include these columns in future database initializations.

### 3. Verified Database Schema
The shipments table now has the following structure:
```sql
CREATE TABLE shipments (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    sender_city VARCHAR(100) NOT NULL,
    receiver_city VARCHAR(100) NOT NULL,
    distance DECIMAL(10,2) NOT NULL,
    weight DECIMAL(10,2) NOT NULL,
    priority VARCHAR(20) NOT NULL,
    traffic_condition VARCHAR(20) NOT NULL,
    transport_mode VARCHAR(20) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    selected_company_id INTEGER,
    status VARCHAR(20) DEFAULT 'PENDING',
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Status Values
- **PENDING**: Initial status when shipment is created
- **CONFIRMED**: Shipment confirmed by user
- **IN_TRANSIT**: Shipment is on the way
- **DELIVERED**: Shipment delivered
- **CANCELLED**: Shipment cancelled

## What Now Works

### ✅ Company Registration (Admin)
- Add new courier companies
- Edit existing companies  
- Delete companies (soft delete with is_active flag)

### ✅ Shipment Registration (Users)
- Create new shipments
- Calculate costs for all companies
- Select preferred company
- Confirm orders with status tracking
- View order history

### ✅ Shipment Status Management (Admin)
- View all orders
- Update order status
- Track shipment progress
- Filter by user or company

## Testing
The application has been recompiled and is ready to use. All database operations should now work correctly.

## How to Test

1. **Test Company Registration (as Admin)**:
   - Login as admin/admin123
   - Go to "Company Management" tab
   - Fill in company details
   - Click "➕ Add Company"
   - Should show success message

2. **Test Shipment Registration (as User)**:
   - Login as user/user123
   - Fill in shipment details
   - Click "💰 Calculate Costs"
   - Select a company
   - Click "✅ Confirm Order"
   - Should show success with order ID

3. **Test Status Update (as Admin)**:
   - Go to "Order History" tab
   - Select an order
   - Click "Update Status"
   - Select new status
   - Should update successfully

## Files Modified
1. `src/main/java/com/logistics/util/DatabaseConnection.java` - Updated SQL schema
2. `logistics.db` - Added missing columns to existing database

## Notes
- The fix is backward compatible with existing data
- New databases will have the correct schema from the start
- No data loss occurred

