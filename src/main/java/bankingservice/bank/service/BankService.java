package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.bank.account.AccountType;
import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.database.AccountDatabase;

import java.util.List;

public class BankService {

    private Bank bank;

    public BankService(Bank bank) {
        this.bank = bank;
    }

    public List<Account> getAccountsForClient(Client client) {
        return AccountDatabase.getAccountsForClient(client.getClientId());
    }

    public Account openAccount(Client client, AccountType accountType, int duration) {
        return bank.openAccount(client, accountType, duration);
    }

    public void addInterestToAllAccounts() {
        bank.addInterestToAllAccounts();
    }
}
