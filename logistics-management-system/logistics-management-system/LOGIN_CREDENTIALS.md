# Login Credentials

This document contains all the dummy login credentials for testing the Logistics Management System.

## Admin Credentials

| Username | Password | Email                  | Role  |
|----------|----------|------------------------|-------|
| admin    | admin123 | admin@logistics.com    | ADMIN |
| admin2   | admin456 | admin2@logistics.com   | ADMIN |

## User Credentials

| Username | Password | Email                  | Role |
|----------|----------|------------------------|------|
| user     | user123  | user@logistics.com     | USER |
| john     | john123  | john@example.com       | USER |
| sarah    | sarah123 | sarah@example.com      | USER |
| mike     | mike123  | mike@example.com       | USER |

## How to Use These Credentials

### If using MySQL:
1. Make sure MySQL is running
2. The credentials are already included in `database_schema.sql`
3. Or run `add_dummy_users.sql` to insert them into your existing database

### If using SQLite:
1. The SQLite database (`logistics.db`) already contains these credentials
2. The application will use them automatically

### Testing
- Try logging in with any of the above credentials
- Admin users will see the Admin Dashboard
- Regular users will see the User Dashboard

## Troubleshooting

If you're getting "Invalid credentials" error:

1. **For MySQL**: Make sure the database has been initialized
   - Run the application once to auto-initialize, OR
   - Manually run `setup_mysql.sql`

2. **For SQLite**: Make sure the database file exists
   - The application will create it automatically on first run

3. **Check Database Connection**: 
   - MySQL: Make sure MySQL is running on localhost:3306
   - The app will fall back to SQLite if MySQL fails

## Quick Setup

To add these users to your existing database, run:

```bash
# For MySQL
mysql -u root -p logistics_db < add_dummy_users.sql

# For SQLite
sqlite3 logistics.db < add_dummy_users.sql
```


