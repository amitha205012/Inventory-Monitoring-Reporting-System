package service;

import DAO.ProductsDAO;
import DAO.ProductsDAOImpl;
import model.Product;
import util.CSVHelper;
import exception.InvalidInputException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class InventoryService {
    private final ProductsDAO productDAO;
    private final StockAlertService alertService;
    private final Scanner sc = new Scanner(System.in);

    public InventoryService(ProductsDAO productDAO, StockAlertService alertService) {
        this.productDAO = productDAO;
        this.alertService = alertService;
    }

    public void addProduct() {
        try {
            System.out.print("📦 Name: ");
            String name = sc.nextLine().trim();
            System.out.print("📝 Description: ");
            String desc = sc.nextLine().trim();
            System.out.print("💰 Price: ");
            double price = Double.parseDouble(sc.nextLine().trim());
            System.out.print("📊 Quantity: ");
            int qty = Integer.parseInt(sc.nextLine().trim());
            System.out.print("⚙️ Alert limit (default 10): ");
            String lim = sc.nextLine().trim();
            int limit = lim.isEmpty() ? 10 : Integer.parseInt(lim);

            Product p = new Product(0, name, desc, price, qty, limit);
            int id = productDAO.addProduct(p);
            System.out.println("✅ Product added with ID: " + id);
            alertService.checkAndAlertAll();
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Please enter valid numbers for price/quantity/limit.");
        } catch (Exception e) {
            System.out.println("⚠️ Add product failed: " + e.getMessage());
        }
    }

    public void viewProductsTabular() {
        try {
            List<Product> list = productDAO.getAllProducts();
            if (list.isEmpty()) {
                System.out.println("❌ No products available.");
                return;
            }
            System.out.println("\n┌───── Products ──────────────────────────────────────────────────────────────┐");
            System.out.printf("│ %-3s │ %-20s │ %-8s │ %-6s │ %-5s │ %-10s │%n",
                    "ID","Name","Price","Qty","Limit","Status");
            System.out.println("├──────────────────────────────────────────────────────────────────────────────┤");
            for (Product p : list) {
                String status = p.getQuantity() <= p.getThreeFoldLimit() ? "⚠️ Low" : "✅ OK";
                System.out.printf("│ %-3d │ %-20s │ ₹%7.2f │ %-6d │ %-5d │ %-10s │%n",
                        p.getId(), truncate(p.getName(),20), p.getPrice(), p.getQuantity(), p.getThreeFoldLimit(), status);
            }
            System.out.println("└──────────────────────────────────────────────────────────────────────────────┘");
        } catch (Exception e) {
            System.out.println("⚠️ Could not display products: " + e.getMessage());
        }
    }

    private String truncate(String s, int n) {
        return s.length() <= n ? s : s.substring(0, n-3) + "...";
    }

    public void searchById() {
        try {
            System.out.print("🔍 Enter product ID: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Product p = productDAO.getProductById(id);
            if (p == null) System.out.println("❌ Not found.");
            else System.out.println(p);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Enter a valid number.");
        } catch (Exception e) {
            System.out.println("⚠️ Search failed: " + e.getMessage());
        }
    }

    public void updateProduct() {
        try {
            System.out.print("✏️ Enter product ID to update: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            Product p = productDAO.getProductById(id);
            if (p == null) { System.out.println("❌ Not found."); return; }

            System.out.print("New name ("+p.getName()+"): ");
            String name = sc.nextLine().trim();
            System.out.print("New description ("+p.getDescription()+"): ");
            String desc = sc.nextLine().trim();
            System.out.print("New price ("+p.getPrice()+"): ");
            String sp = sc.nextLine().trim();
            System.out.print("New quantity ("+p.getQuantity()+"): ");
            String sq = sc.nextLine().trim();
            System.out.print("New limit ("+p.getThreeFoldLimit()+"): ");
            String sl = sc.nextLine().trim();

            if (!name.isEmpty()) p.setName(name);
            if (!desc.isEmpty()) p.setDescription(desc);
            if (!sp.isEmpty()) p.setPrice(Double.parseDouble(sp));
            if (!sq.isEmpty()) p.setQuantity(Integer.parseInt(sq));
            if (!sl.isEmpty()) p.setThreeFoldLimit(Integer.parseInt(sl));

            productDAO.updateProduct(p);
            System.out.println("✅ Product updated.");
            alertService.checkAndAlertAll();
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Please enter valid numbers.");
        } catch (Exception e) {
            System.out.println("⚠️ Update failed: " + e.getMessage());
        }
    }

    public void deleteProduct() {
        try {
            System.out.print("🗑️ Enter product ID to delete: ");
            int id = Integer.parseInt(sc.nextLine().trim());
            productDAO.deleteProduct(id);
            System.out.println("✅ Product deleted (if existed).");
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Enter a valid number.");
        } catch (Exception e) {
            System.out.println("⚠️ Delete failed: " + e.getMessage());
        }
    }

    public void filterByPrice() {
        try {
            System.out.print("Min price: ");
            double min = Double.parseDouble(sc.nextLine().trim());
            System.out.print("Max price: ");
            double max = Double.parseDouble(sc.nextLine().trim());
            List<Product> list = productDAO.getProductsByPriceRange(min, max);
            if (list.isEmpty()) System.out.println("❌ No products in range.");
            else {
                System.out.println("\n📋 Products in range:");
                for (Product p : list) System.out.println(p);
            }
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Enter valid prices.");
        } catch (Exception e) {
            System.out.println("⚠️ Filter failed: " + e.getMessage());
        }
    }

    public void generateInventoryReport() {
        try {
            List<Product> list = productDAO.getAllProducts();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            String path = "data/inventory_report_" + timestamp + ".csv";
            CSVHelper.exportProductsToFile(list, path);
            System.out.println("📊 Report saved: " + path);
        } catch (Exception e) {
            System.out.println("⚠️ Could not create report: " + e.getMessage());
        }
    }
}
