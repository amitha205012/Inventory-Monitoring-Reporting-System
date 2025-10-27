package util;

import exception.DataAccessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    private static final String DEFAULT_URL = "jdbc:sqlite:data/inventory.db";

    public static Connection getConnection() throws DataAccessException {
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String pass = System.getenv("DB_PASS");

        if (url == null || url.isBlank()) url = DEFAULT_URL;

        try {
            if (user == null || user.isBlank()) {
                return DriverManager.getConnection(url);
            } else {
                return DriverManager.getConnection(url, user, pass);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Cannot open database connection: " + e.getMessage(), e);
        }
    }
}
