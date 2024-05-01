package bankingservice.database;

import bankingservice.bank.bank.Bank;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BankDatabase {

    public static void add(String name, double debitInterestRate, double savingsInterestRate, double creditCommission, double creditLimit, double suspiciousAccountLimit) throws SQLException {
        var sql = "INSERT INTO banks(name, debit_interest_rate, savings_interest_rate, credit_commission, credit_limit, suspicious_account_limit)"
                + "VALUES(?, ?, ?, ?, ?, ?)";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, name);
        pstmt.setDouble(2, debitInterestRate);
        pstmt.setDouble(3, savingsInterestRate);
        pstmt.setDouble(4, creditCommission);
        pstmt.setDouble(5, creditLimit);
        pstmt.setDouble(6, suspiciousAccountLimit);

        pstmt.executeUpdate();
    }

    public static Bank findById(int id) throws SQLException {
        var sql = "SELECT * FROM banks WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, id);
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Bank(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("debit_interest_rate"),
                    rs.getDouble("savings_interest_rate"),
                    rs.getDouble("credit_commission"),
                    rs.getDouble("credit_limit"),
                    rs.getDouble("suspicious_account_limit")
            );
        }
        return null;
    }

    public static List<Bank> getBanks() throws SQLException {
        var products = new ArrayList<Bank>();

        var sql = "SELECT id, name, debit_interest_rate, savings_interest_rate, credit_commission, credit_limit, suspicious_account_limit FROM banks ORDER BY id";

        var conn =  Database.connect();
        var stmt = conn.createStatement();
        var rs = stmt.executeQuery(sql);

        while (rs.next()) {
            var product = new Bank(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getDouble("debit_interest_rate"),
                    rs.getDouble("savings_interest_rate"),
                    rs.getDouble("credit_commission"),
                    rs.getDouble("credit_limit"),
                    rs.getDouble("suspicious_account_limit"));
            products.add(product);
        }
        return products;
    }
}
