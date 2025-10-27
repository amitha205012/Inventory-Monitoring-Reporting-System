package util;

import java.sql.*;
import java.util.*;
import java.io.*;

// 🎨 Console color helper
class ConsoleColors {
    public static final String RESET = "\033[0m";
    public static final String GREEN = "\033[0;32m";
    public static final String RED = "\033[0;31m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String CYAN = "\033[0;36m";
    public static final String PURPLE = "\033[0;35m";
}

// 🛍️ util.Product model
class Product {
    int id;
    String name;
    String category;
    int quantity;
    double price;
    Timestamp dateAdded;

    public Product(int id, String name, String category, int quantity, double price, Timestamp dateAdded) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
        this.dateAdded = dateAdded;
    }
}

// 🧠 Custom exception for user-friendly messages
class ValidationException extends Exception {
    public ValidationException(String message) {
        super(ConsoleColors.RED + "❌ " + message + ConsoleColors.RESET);
    }
}

// 📦 Inventory System
class InventorySystem {

    static Scanner sc = new Scanner(System.in);

    // 💡 Connect to database
    public static Connection connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/InventoryDB", "root", "12345");
    }

    // 🖨️ Print table header
    public static void printTableHeader() {
        System.out.printf(ConsoleColors.CYAN + "%-5s | %-20s | %-15s | %-8s | %-10s | %-20s\n", "ID", "Name", "Category", "Qty", "Price", "Added On");
        System.out.println("---------------------------------------------------------------------------------------------" + ConsoleColors.RESET);
    }

    // 🖨️ Print each product row
    public static void printProductRow(util.Product p) {
        System.out.printf("%-5d | %-20s | %-15s | %-8d | %-10.2f | %-20s\n",
                p.id, p.name, p.category, p.quantity, p.price, p.dateAdded.toString());
    }

    // 🧹 Clear screen
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                System.out.print("\033[H\033[2J");
        } catch (Exception e) {
            System.out.println("⚠️ Screen clear not supported.");
        }
    }

    // 🔍 Validate inputs
    public static void validateInputs(String name, String category, int quantity, double price) throws ValidationException {
        if (name.isEmpty()) throw new ValidationException("util.Product name cannot be empty!");
        if (category.isEmpty()) throw new ValidationException("Category cannot be empty!");
        if (quantity < 0) throw new ValidationException("Quantity cannot be negative!");
        if (price <= 0) throw new ValidationException("Price must be greater than 0!");
    }

    // ➕ Add product
    public static void addProduct(Connection con) throws SQLException {
        try {
            System.out.print("🔤 Name: ");
            String name = sc.nextLine();

            System.out.print("📦 Category: ");
            String category = sc.nextLine();

            System.out.print("🔢 Quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());

            System.out.print("💰 Price: ");
            double price = Double.parseDouble(sc.nextLine());

            validateInputs(name, category, quantity, price);

            String sql = "INSERT INTO products(name, category, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.executeUpdate();

            System.out.println(ConsoleColors.GREEN + "✅ util.Product added successfully!" + ConsoleColors.RESET);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.RED + "❌ Invalid number format!" + ConsoleColors.RESET);
        }
    }

    // 📋 View products
    public static void viewProducts(Connection con, String orderBy) throws SQLException {
        String sql = "SELECT * FROM products";
        if (orderBy != null) sql += " ORDER BY " + orderBy;
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sql);

        printTableHeader();
        while (rs.next()) {
            Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getTimestamp("date_added")
            );
            printProductRow(p);
        }
    }

    // ✏️ Update product
    public static void updateProduct(Connection con) throws SQLException {
        try {
            System.out.print("🆔 util.Product ID to update: ");
            int id = Integer.parseInt(sc.nextLine());

            System.out.print("🔤 New name: ");
            String name = sc.nextLine();

            System.out.print("📦 New category: ");
            String category = sc.nextLine();

            System.out.print("🔢 New quantity: ");
            int quantity = Integer.parseInt(sc.nextLine());

            System.out.print("💰 New price: ");
            double price = Double.parseDouble(sc.nextLine());

            validateInputs(name, category, quantity, price);

            String sql = "UPDATE products SET name=?, category=?, quantity=?, price=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setInt(3, quantity);
            ps.setDouble(4, price);
            ps.setInt(5, id);

            int rows = ps.executeUpdate();
            if (rows > 0)
                System.out.println(ConsoleColors.GREEN + "✅ util.Product updated!" + ConsoleColors.RESET);
            else
                System.out.println(ConsoleColors.RED + "❌ util.Product not found!" + ConsoleColors.RESET);
        } catch (ValidationException e) {
            System.out.println(e.getMessage());
        }
    }

    // ❌ Delete product
    public static void deleteProduct(Connection con) throws SQLException {
        System.out.print("🆔 util.Product ID to delete: ");
        int id = Integer.parseInt(sc.nextLine());

        String sql = "DELETE FROM products WHERE id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, id);

        int rows = ps.executeUpdate();
        if (rows > 0)
            System.out.println(ConsoleColors.GREEN + "🗑️ util.Product deleted!" + ConsoleColors.RESET);
        else
            System.out.println(ConsoleColors.RED + "❌ util.Product not found!" + ConsoleColors.RESET);
    }

    // 🔎 Search product
    public static void searchProduct(Connection con) throws SQLException {
        System.out.print("🔍 Enter product name or ID: ");
        String input = sc.nextLine();

        PreparedStatement ps;
        if (input.matches("\\d+")) {
            ps = con.prepareStatement("SELECT * FROM products WHERE id=?");
            ps.setInt(1, Integer.parseInt(input));
        } else {
            ps = con.prepareStatement("SELECT * FROM products WHERE name LIKE ?");
            ps.setString(1, "%" + input + "%");
        }

        ResultSet rs = ps.executeQuery();
        printTableHeader();
        while (rs.next()) {
            util.Product p = new util.Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getTimestamp("date_added")
            );
            printProductRow(p);
        }
    }

    // 📉 Low stock
    public static void viewLowStock(Connection con) throws SQLException {
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM products WHERE quantity < 10");

        System.out.println(ConsoleColors.YELLOW + "📉 Low Stock Products (Qty < 10)" + ConsoleColors.RESET);
        printTableHeader();
        while (rs.next()) {
            Product p = new Product(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("quantity"),
                    rs.getDouble("price"),
                    rs.getTimestamp("date_added")
            );
            printProductRow(p);
        }
    }

    // 📤 Export
    public static void exportToCSV(Connection con) {
        try (PrintWriter writer = new PrintWriter("products_export.csv")) {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM products");
            writer.println("ID,Name,Category,Quantity,Price,Date_Added");

            while (rs.next()) {
                writer.printf("%d,%s,%s,%d,%.2f,%s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getInt("quantity"),
                        rs.getDouble("price"),
                        rs.getTimestamp("date_added").toString());
            }

            System.out.println(ConsoleColors.GREEN + "✅ Exported to products_export.csv" + ConsoleColors.RESET);
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "❌ Export failed!" + ConsoleColors.RESET);
        }
    }

    // 🧠 Main method
    public static void main(String[] args) {
        try (Connection con = connect()) {
            while (true) {
                System.out.println(ConsoleColors.BLUE + """
                    ══════════════════════════════════════
                           📦 INVENTORY SYSTEM
                     ═════════════════════════════════════
                    ║ 1️⃣  Add util.Product               
                    ║ 2️⃣  View All Products               ║
                    ║ 3️⃣  Update util.Product             ║
                    ║ 4️⃣  Delete util.Product             ║
                    ║ 5️⃣  Search util.Product             ║
                    ║ 6️⃣  View Low Stock Products         ║
                    ║ 7️⃣  Sort Products                   ║
                    ║ 8️⃣  Export to CSV                   ║
                    ║ 9️⃣  Clear Screen                    ║
                    ║ 0️⃣  Exit                            ║
                     ══════════════════════════════════════
                    """ + ConsoleColors.RESET);
                System.out.print("👉 Enter your choice: ");
                int choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1 -> addProduct(con);
                    case 2 -> viewProducts(con, null);
                    case 3 -> updateProduct(con);
                    case 4 -> deleteProduct(con);
                    case 5 -> searchProduct(con);
                    case 6 -> viewLowStock(con);
                    case 7 -> {
                        System.out.print("🔃 Sort by (1-Name, 2-Price, 3-Quantity): ");
                        String opt = sc.nextLine();
                        String col = switch (opt) {
                            case "1" -> "name";
                            case "2" -> "price";
                            case "3" -> "quantity";
                            default -> null;
                        };
                        if (col != null) viewProducts(con, col);
                        else System.out.println("❌ Invalid sort option.");
                    }
                    case 8 -> exportToCSV(con);
                    case 9 -> clearScreen();
                    case 0 -> {
                        System.out.println("👋 Exiting... Bye!");
                        return;
                    }
                    default -> System.out.println("❌ Invalid option! Try again.");
                }

                System.out.println("\n🔁 Press Enter to continue...");
                sc.nextLine();
            }
        } catch (Exception e) {
            System.out.println(ConsoleColors.RED + "🔥 ERROR: " + e.getMessage() + ConsoleColors.RESET);
        }
    }
}
