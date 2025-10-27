package report;

import DAO.UserDAO;
import DAO.ProductsDAO;
import util.CSVHelper;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyReportGenerator {
    private final UserDAO userDAO;
    private final ProductsDAO productDAO;

    public DailyReportGenerator(UserDAO userDAO, ProductsDAO productDAO) {
        this.userDAO = userDAO;
        this.productDAO = productDAO;
    }

    public void generateReport() {
        try {
            String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
            CSVHelper.exportUsers(userDAO.getAllUsers(), "data/users_report_" + ts + ".csv");
            CSVHelper.exportProductsToFile(productDAO.getAllProducts(), "data/products_report_" + ts + ".csv");
            System.out.println("🕒 Reports generated at " + ts);
        } catch (Exception e) {
            System.out.println("⚠️ Report generation failed: " + e.getMessage());
        }
    }
}
