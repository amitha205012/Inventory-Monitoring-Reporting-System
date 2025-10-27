package util;
import DAO.UserDAO;
import DAO.UserDAOImpl;
import DAO.ProductsDAO;
import DAO.ProductsDAOImpl;
import service.*;
import report.DailyReportGenerator;
import ui.Dashboard;
import model.User;

import java.util.Scanner;

public class AppMain {
    public static void main(String[] args) {
        // DAOs
        UserDAO userDAO = new UserDAOImpl();
        ProductsDAO productDAO = new ProductsDAOImpl();

        // Services
        EmailService emailService = new EmailService();
        OTPService otpService = new OTPService();
        UserService userService = new UserService(userDAO, emailService, otpService);

        StockAlertService alertService = new StockAlertService(productDAO, emailService);
        InventoryService inventoryService = new InventoryService(productDAO, alertService);

        DailyReportGenerator reportGenerator = new DailyReportGenerator(userDAO, productDAO);

        Scanner sc = new Scanner(System.in);

        System.out.println("🌟 Inventory Management System 🌟");

        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1️⃣ Register as User");
            System.out.println("2️⃣ Register as Admin");
            System.out.println("3️⃣ Login");
            System.out.println("4️⃣ Exit");
            System.out.print("👉 Choose: ");
            String ch = sc.nextLine().trim();

            switch (ch) {
                case "1" -> userService.register("user");
                case "2" -> userService.register("admin");
                case "3" -> {
                    User u = userService.login();
                    if (u != null) {
                        Dashboard dashboard = new Dashboard(inventoryService, userDAO, reportGenerator, u);
                        dashboard.show();
                    }
                }
                case "4" -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid choice!");
            }
        }
    }
}
