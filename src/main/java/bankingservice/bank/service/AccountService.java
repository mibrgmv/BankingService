package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.database.AccountDatabase;

import java.sql.SQLException;
import java.util.List;

public class AccountService {

    public Account findAccountById(int id) throws SQLException {
        return AccountDatabase.findById(id);
    }

    public List<Account> getAccountsForClient(int clientId) throws SQLException {
        return AccountDatabase.getAccountsForClient(clientId);
    }
}
