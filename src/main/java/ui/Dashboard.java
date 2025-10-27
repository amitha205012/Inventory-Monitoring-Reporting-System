package ui;

import DAO.UserDAO;
import DAO.ProductsDAO;
import report.DailyReportGenerator;
import service.InventoryService;
import model.User;

import java.util.Scanner;

public class Dashboard {
    private final InventoryService inventoryService;
    private final UserDAO userDAO;
    private final DailyReportGenerator reportGenerator;
    private final User currentUser;
    private final Scanner sc = new Scanner(System.in);

    public Dashboard(InventoryService inventoryService, UserDAO userDAO, DailyReportGenerator reportGenerator, User currentUser) {
        this.inventoryService = inventoryService;
        this.userDAO = userDAO;
        this.reportGenerator = reportGenerator;
        this.currentUser = currentUser;
    }

    public void show() {
        while (true) {
            System.out.println("\n🏠 --- Dashboard (" + currentUser.getRole() + ") ---");

            System.out.println("1️⃣ View all products (tabular)");
            System.out.println("2️⃣ Search product by ID");
            System.out.println("3️⃣ Filter by price range");
            System.out.println("4️⃣ Generate inventory report");
            if (currentUser.getRole().equals("admin")) {
                System.out.println("5️⃣ Add product");
                System.out.println("6️⃣ Update product");
                System.out.println("7️⃣ Delete product");
                System.out.println("8️⃣ Generate users & products report");
                System.out.println("9️⃣ Logout");
            } else {
                System.out.println("5️⃣ Logout");
            }

            System.out.print("👉 Choose option: ");
            String choice = sc.nextLine().trim();

            if (currentUser.getRole().equals("admin")) {
                switch (choice) {
                    case "1" -> inventoryService.viewProductsTabular();
                    case "2" -> inventoryService.searchById();
                    case "3" -> inventoryService.filterByPrice();
                    case "4" -> inventoryService.generateInventoryReport();
                    case "5" -> inventoryService.addProduct();
                    case "6" -> inventoryService.updateProduct();
                    case "7" -> inventoryService.deleteProduct();
                    case "8" -> reportGenerator.generateReport();
                    case "9" -> { System.out.println("👋 Logging out..."); return; }
                    default -> System.out.println("❌ Invalid choice!");
                }
            } else {
                switch (choice) {
                    case "1" -> inventoryService.viewProductsTabular();
                    case "2" -> inventoryService.searchById();
                    case "3" -> inventoryService.filterByPrice();
                    case "4" -> inventoryService.generateInventoryReport();
                    case "5" -> { System.out.println("👋 Logging out..."); return; }
                    default -> System.out.println("❌ Invalid choice!");
                }
            }
        }
    }
}
