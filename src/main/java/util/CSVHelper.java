package util;

import model.User;
import model.Product;
import java.io.FileWriter;
import java.util.List;

public class CSVHelper {
    public static void exportUsers(List<User> users, String path) {
        try (FileWriter w = new FileWriter(path)) {
            w.write("ID,Name,Email,Verified\n");
            for (User u : users) {
                w.write(String.format("%d,%s,%s,%b\n", u.getId(), u.getName(), u.getEmail(), u.isVerified()));
            }
            System.out.println("✅ Users CSV exported: " + path);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to export users: " + e.getMessage());
        }
    }

    public static void exportProductsToFile(List<Product> products, String path) {
        try (FileWriter w = new FileWriter(path)) {
            w.write("ID,Name,Description,Price,Quantity,Limit\n");
            for (Product p : products) {
                w.write(String.format("%d,%s,%s,%.2f,%d,%d\n",
                        p.getId(), escape(p.getName()), escape(p.getDescription()), p.getPrice(), p.getQuantity(), p.getThreeFoldLimit()));
            }
            System.out.println("✅ Products CSV exported: " + path);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to export products: " + e.getMessage());
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace(",", " "); // simple escape
    }
}
