package bankingservice.bank.bank;

import bankingservice.bank.account.Account;
import bankingservice.bank.account.Transaction;
import bankingservice.bank.account.TransactionType;
import bankingservice.database.AccountDatabase;
import bankingservice.database.BankDatabase;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.CannotUndoException;

import java.sql.SQLException;
import java.util.List;

public class CentralBank {

    private static CentralBank instance;

    private CentralBank() { }

    public static CentralBank getInstance() {
        if (instance == null) {
            instance = new CentralBank();
        }
        return instance;
    }

    public List<Bank> getBanks() throws SQLException {
        return BankDatabase.getBanks();
    }

    public void registerBank(String name, double debitInterestRate, double savingsInterestRate, double creditCommission, double creditLimit, double suspiciousAccountLimit) throws SQLException {
        if (name.isEmpty() || name.isBlank() || debitInterestRate < 0 || savingsInterestRate < 0 || creditCommission < 0 || creditLimit < 0 || suspiciousAccountLimit < 0) {
            throw new IllegalArgumentException("Blank or negative arguments");
        }
        BankDatabase.add(name, debitInterestRate, savingsInterestRate, creditCommission, creditLimit, suspiciousAccountLimit);
    }

    public void addInterest() throws SQLException {
        for (var bank : getBanks()) {
            bank.addInterest();
        }
    }

    public void undoTransaction(int transactionId) throws SQLException, IllegalArgumentException, CannotUndoException {
        Transaction transaction = TransactionDatabase.findById(transactionId);
        if (transaction.isUndo()) {
            throw new CannotUndoException("Transaction has already been undone");
        }
        if (transaction == null) {
            throw new IllegalArgumentException("Nonexistent transaction");
        }
        if (transaction.transactionType() == TransactionType.RECEIVING) {
            throw new CannotUndoException("Cannot undo receiving type transaction");
        }

        int accountId = transaction.accountId();
        Account account = AccountDatabase.findById(transaction.accountId());

        switch (transaction.transactionType()) {
            case DEPOSIT:
                AccountDatabase.alterBalance(accountId, account.getBalance() - transaction.amount());
                TransactionDatabase.alterUndo(transactionId, true);
                break;
            case WITHDRAWAL:
                AccountDatabase.alterBalance(accountId, account.getBalance() + transaction.amount());
                TransactionDatabase.alterUndo(transactionId, true);
                break;
            case TRANSFER:
                AccountDatabase.alterBalance(accountId, account.getBalance() + transaction.amount());
                TransactionDatabase.alterUndo(transactionId, true);
                Transaction receivingTransaction = TransactionDatabase.findById(transactionId + 1);
                int receivingAccountId = receivingTransaction.accountId();
                Account receivingAccount = AccountDatabase.findById(receivingAccountId);
                AccountDatabase.alterBalance(receivingAccountId, receivingAccount.getBalance() - receivingTransaction.amount());
                TransactionDatabase.alterUndo(transactionId + 1, true);
                break;
            default:
                throw new CannotUndoException("Cannot undo " + transaction.transactionType() + " type transaction");
        }
    }
}

