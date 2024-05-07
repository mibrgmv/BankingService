package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.database.AccountDatabase;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.AccountDeletionException;
import bankingservice.exceptions.CannotFindException;

import java.sql.SQLException;
import java.util.List;

public class AccountService {

    public Account findAccountById(int id) throws SQLException {
        return AccountDatabase.findById(id);
    }

    public List<Account> getAccountsForClient(int clientId) throws SQLException {
        return AccountDatabase.getAccountsForClient(clientId);
    }

    public void deleteAccount(int id) throws SQLException, AccountDeletionException, CannotFindException {
        Account account = AccountDatabase.findById(id);
        if (account == null) {
            throw new CannotFindException("Cannot find account");
        }
        if (account.getBalance() > 0.01) {
            throw new AccountDeletionException("Cannot delete an account with funds on it");
        }

        TransactionDatabase.deleteByAccountId(id);
        AccountDatabase.delete(id);
    }
}
