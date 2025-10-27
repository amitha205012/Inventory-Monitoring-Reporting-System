package service;

import DAO.ProductsDAO;
import model.Product;

import java.util.List;

public class StockAlertService {
    private final ProductsDAO productDAO;
    private final EmailService emailService;
    private final String alertTo; // admin email or list

    public StockAlertService(ProductsDAO productDAO, EmailService emailService) {
        this.productDAO = productDAO;
        this.emailService = emailService;
        this.alertTo = System.getenv("ALERT_TO") != null ? System.getenv("ALERT_TO") : "admin@example.com";
    }

    public void checkAndAlertAll() {
        List<Product> list = productDAO.getAllProducts();
        for (Product p : list) {
            if (p.getQuantity() <= p.getThreeFoldLimit()) {
                sendAlert(p);
            }
        }
    }

    public void sendAlert(Product p) {
        String subject = "⚠️ Low stock: " + p.getName();
        String msg = String.format("Product: %s (ID:%d)\nQty: %d\nThreshold: %d\nPlease restock.",
                p.getName(), p.getId(), p.getQuantity(), p.getThreeFoldLimit());
        try {
            emailService.sendEmail(alertTo, subject, msg);
        } catch (Exception e) {
            System.out.println("⚠️ Could not send stock alert email: " + e.getMessage());
        }
    }
}
