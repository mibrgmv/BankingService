package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.bank.account.AccountType;
import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.database.AccountDatabase;

import java.sql.SQLException;
import java.util.List;

public class BankService {

    private Bank bank;
    private AccountService accountService;

    public BankService() {
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public List<Account> getAccountsForClient(Client client) {
        return AccountDatabase.getAccountsForClient(client.getClientId());
    }

    public int openAccount(Client client, AccountType accountType) throws SQLException {
        if (accountType == AccountType.SAVINGS) {
            throw new IllegalArgumentException("Need to specify the duration for Savings Account");
        } else {
            return this.openAccount(client, accountType, 0);
        }
    }

    public int openAccount(Client client, AccountType accountType, int yearsDuration) throws SQLException {
        return bank.openAccount(client, accountType, yearsDuration);
    }

    public void addInterestToAllAccounts() {
        for (var account : getBank().getAccounts()) {
            accountService.addInterest(account);
        }
    }
}
