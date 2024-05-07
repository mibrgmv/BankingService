package bankingservice.database;

import bankingservice.bank.account.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TransactionDatabase {

    public static int add(int accountId, int bankId, double amount, TransactionType transactionType, LocalDate date) throws SQLException {
        var sql = "INSERT INTO transactions(account_id, bank_id, amount, type, date, is_undo)"
                + "VALUES(?, ?, ?, ?, ?, FALSE)";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, accountId);
        pstmt.setInt(2, bankId);
        pstmt.setDouble(3, amount);
        pstmt.setObject(4, transactionType.toString(), Types.OTHER);
        pstmt.setDate(5, Date.valueOf(date));

        pstmt.executeUpdate();

        var rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        throw new SQLException("Failed to return ID");
    }

    public static Transaction findById(int id) throws SQLException {
        var sql = "SELECT * FROM transactions WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setInt(1, id);
        var rs = pstmt.executeQuery();
        if (rs.next()) {
            return new Transaction(
                    id,
                    rs.getInt("account_id"),
                    rs.getInt("bank_id"),
                    rs.getDouble("amount"),
                    TransactionType.valueOf(rs.getString("type")),
                    rs.getDate("date").toLocalDate(),
                    rs.getBoolean("is_undo")
            );
        }
        return null;
    }

    public static void alterUndo(int id, boolean isUndo) throws SQLException {
        var sql = "UPDATE transactions SET is_undo=? WHERE id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);

        pstmt.setBoolean(1, isUndo);
        pstmt.setInt(2, id);

        pstmt.executeUpdate();
    }

    public static List<Transaction> getTransactionsForClient(int id) throws SQLException {
        var sql = "SELECT * FROM transactions where account_id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);

        var transactions = new ArrayList<Transaction>();
        var rs = pstmt.executeQuery();
        while (rs.next()) {
            var transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getInt("account_id"),
                    rs.getInt("bank_id"),
                    rs.getDouble("amount"),
                    TransactionType.valueOf(rs.getString("type")),
                    rs.getDate("date").toLocalDate(),
                    rs.getBoolean("is_undo")
            );
            transactions.add(transaction);
        }
        return transactions;
    }

    public static void deleteByAccountId(int accountId) throws SQLException {
        var sql = "DELETE FROM transactions WHERE account_id=?";

        var conn =  Database.connect();
        var pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setInt(1, accountId);

        pstmt.executeUpdate();
    }
}