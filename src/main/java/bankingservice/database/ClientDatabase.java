package bankingservice.database;

import bankingservice.bank.client.Client;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;

public class ClientDatabase {

    public static void add(String name, String surname, String dateOfBirth) throws SQLException {
        var sql = "INSERT INTO clients(first_name, last_name, date_of_birth)"
                + "VALUES(?,?,?)";

        try (var conn =  Database.connect();
             var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setDate(3, Date.valueOf(dateOfBirth));

            pstmt.executeUpdate();
        }
    }

    public static Client findById(int id) throws SQLException {
        var sql = "SELECT * FROM clients WHERE id=?";

        try (var conn =  Database.connect();
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate()
                );
            }
        }
        return null;
    }

    public static void delete(int id) throws SQLException {
        var sql = "DELETE FROM clients WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, id);

        pstmt.executeUpdate();
    }
}