package bankingservice.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connect() {
        try {
            // TODO
//            var url = DatabaseConfig.getDbUrl();
//            var username = DatabaseConfig.getDbUsername();
//            var password = DatabaseConfig.getDbPassword();
            var url = "jdbc:postgresql://localhost:5432/bankingservice";
            var username = "mibrgmv";
            var password = "mibrgmv";

            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}