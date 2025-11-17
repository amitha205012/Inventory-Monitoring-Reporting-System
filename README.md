# Inventory Monitoring & Reporting System

A **Java based application** for managing inventory, monitoring stock levels, and generating reports. This project is designed to help small businesses or organizations keep track of products, their quantities, limits, and other key details efficiently.

---

## Features

* ✅ Add, update, delete, and view products
* ✅ Stock monitoring with threshold alerts
* ✅ Generate inventory reports in CSV format
* ✅ Supports multiple product categories
* ✅ Interactive menu
* ✅ Handles exceptions gracefully
* ✅ Database connectivity using MySQL (or SQLite)

---

## Project Structure

```

Inventory-Monitoring-Reporting-System/
│
├── src/
│   ├── dao/            # DAO interfaces & implementations (ProductDAO, UserDAO)
│   ├── model/          # Entity classes (Product, User)
│   ├── service/        # Business logic (InventoryService, UserService, EmailService, OTPService, StockAlertService)
│   ├── util/           # Utilities (DBConnection, CSVHelper)
│   ├── exception/      # Custom exceptions (DataAccessException, EmailException, InvalidInputException)
│   ├── report/         # Report generator (DailyReportGenerator)
│   └── ui/             # Dashboard (Dashboard)
│
├── data/               # SQLite database & exported CSV reports
├── AppMain.java        # Entry point for application
├── pom.xml             # Maven configuration
└── README.md

````

---

## Prerequisites

* Java JDK 17 or later
* Maven (for dependency management)
* MySQL or SQLite database
* IDE like IntelliJ IDEA or Eclipse

---

## Database Setup

1. Create a database named `inventory_db` (or any name you prefer).
2. Execute the table creation scripts for `products`, `users`, etc.
3. Add environment variables for database connection:

| Name    | Value                                    |
| ------- | ---------------------------------------- |
| DB_URL  | jdbc:mysql://localhost:3306/inventorydb  |
| DB_USER | root                                     |
| DB_PASS | 12345                                    |

---

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/amitha205012/Inventory-Monitoring-Reporting-System.git
````

2. Import the project into your IDE as a **Maven Project**.

3. Configure environment variables for DB connection.

4. Run the `AppMain.java` file to start the application.

---

## Usage

Upon running the application, you will see a **menu-driven interface**:

```
1. Add Product
2. View Products
3. Update Product
4. Delete Product
5. Generate Inventory Report
6. Exit
```

* Enter the number corresponding to the action.
* Follow prompts to add or modify product details.
* Reports are saved in the `data/` folder with a timestamp.

---

## CSV Report Example

| ID | Name   | Description       | Price | Quantity | Limit |
| -- | ------ | ----------------- | ----- | -------- | ----- |
| 1  | Modem  | Electronic        | 2500  | 2        | 2     |
| 2  | Router | Networking device | 3200  | 5        | 3     |

---

## Exception Handling

* Handles invalid input and database errors
* Custom exceptions for data access (`DataAccessException`)
* Alerts for low stock based on limit

---

## Contribution

1. Fork the repository
2. Create a new branch (`feature/your-feature`)
3. Commit your changes (`git commit -m 'Add new feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License.

---

## Author

**Amitha205012**
[GitHub Profile](https://github.com/amitha205012)






