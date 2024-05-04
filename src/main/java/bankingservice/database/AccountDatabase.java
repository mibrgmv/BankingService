package bankingservice.database;

import bankingservice.bank.account.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountDatabase {

    public static int add(int ownerId, int bankId, AccountType accountType, double limitForSuspiciousAccount, double interestRate) throws SQLException {
        var sql = "INSERT INTO accounts(owner_id, bank_id, balance, is_suspicious, account_type, limit_for_suspicious, interest_rate)"
                + "VALUES(?, ?, 0.0, FALSE, ?, ?, ?)";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, ownerId);
        pstmt.setInt(2, bankId);
        pstmt.setObject(3, accountType.toString(), Types.OTHER);
        pstmt.setDouble(4, limitForSuspiciousAccount);
        pstmt.setDouble(5, interestRate);

        pstmt.executeUpdate();

        var rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Failed to return ID");
    }

    public static void alterBalance(int id, double newBalance) throws SQLException {
        var sql = "UPDATE accounts SET balance=? WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setDouble(1, newBalance);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();
    }

    public static void alterSuspicious(int id, boolean isSuspicious) throws SQLException {
        var sql = "UPDATE accounts SET is_suspicious=? WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setBoolean(1, isSuspicious);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();
    }

    public static void alterCreditLimit(int id, double creditLimit) throws SQLException {
        var sql = "UPDATE accounts SET credit_limit=? WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setDouble(1, creditLimit);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();
    }

    public static void alterEndDate(int id, LocalDate endDate) throws SQLException {
        var sql = "UPDATE accounts SET end_date=? WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setDate(1, Date.valueOf(endDate));
        pstmt.setInt(2, id);

        pstmt.executeUpdate();
    }

    public static Account findById(int id) throws SQLException {
        var sql = "SELECT * FROM accounts WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, id);
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
            switch (accountType) {
                case SAVINGS -> {
                    return new SavingsAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDate("end_date").toLocalDate()
                    );
                }
                case DEBIT -> {
                    return new DebitAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate")
                    );
                }
                case CREDIT -> {
                    return new CreditAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDouble("credit_limit")
                    );
                }
            }
        }
        return null;
    }

    public static List<Account> getAccountsForClient(int clientId) throws SQLException {
        var accounts = new ArrayList<Account>();

        var sql = "SELECT * FROM accounts WHERE owner_id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, clientId);

        var rs = pstmt.executeQuery();

        while (rs.next()) {
            AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
            Account account;
            switch (accountType) {
                case SAVINGS -> {
                    account = new SavingsAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDate("end_date").toLocalDate()
                    );
                    accounts.add(account);
                }
                case DEBIT -> {
                    account = new DebitAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate")
                    );
                    accounts.add(account);
                }
                case CREDIT -> {
                    account = new CreditAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDouble("credit_limit")
                    );
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }

    public static List<Account> getAccountsForBank(int bankId) throws SQLException {
        var accounts = new ArrayList<Account>();

        var sql = "SELECT * FROM accounts WHERE bank_id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setInt(1, bankId);

        var rs = pstmt.executeQuery();

        while (rs.next()) {
            AccountType accountType = AccountType.valueOf(rs.getString("account_type"));
            Account account;
            switch (accountType) {
                case SAVINGS -> {
                    account = new SavingsAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDate("end_date").toLocalDate()
                    );
                    accounts.add(account);
                }
                case DEBIT -> {
                    account = new DebitAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate")
                    );
                    accounts.add(account);
                }
                case CREDIT -> {
                    account = new CreditAccount(
                            rs.getInt("id"),
                            rs.getInt("owner_id"),
                            rs.getInt("bank_id"),
                            rs.getDouble("balance"),
                            rs.getBoolean("is_suspicious"),
                            rs.getDouble("limit_for_suspicious"),
                            rs.getDouble("interest_rate"),
                            rs.getDouble("credit_limit")
                    );
                    accounts.add(account);
                }
            }
        }
        return accounts;
    }
}
