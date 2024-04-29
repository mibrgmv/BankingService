package bankingservice.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() {
        try {
            var url = DatabaseConfig.getDbUrl();
            var username = DatabaseConfig.getDbUsername();
            var password = DatabaseConfig.getDbPassword();

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}