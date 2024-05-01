package bankingservice.bank.bank;

import bankingservice.bank.account.*;
import bankingservice.bank.client.Client;
import bankingservice.bank.service.AccountService;
import bankingservice.database.AccountDatabase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Bank {

    private int bankId;
    private String name;
    private double commissionForCreditAccount;
    private double interestRateForDebitAccount;
    private double interestRatesForSavingsAccount;
    private double creditLimit;
    private double limitForSuspiciousAccount;
    private List<Client> clients;
    private List<Account> accounts;
    private AccountService accountService;

    public Bank(int bankId, String name, double interestRatesForSavingsAccount, double interestRateForDebitAccount, double commissionForCreditAccount, double creditLimit, double limitForSuspiciousAccount) {
        this.bankId = bankId;
        this.name = name;
        this.commissionForCreditAccount = commissionForCreditAccount;
        this.interestRateForDebitAccount = interestRateForDebitAccount;
        this.interestRatesForSavingsAccount = interestRatesForSavingsAccount;
        this.creditLimit = creditLimit;
        this.limitForSuspiciousAccount = limitForSuspiciousAccount;
        this.clients = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.accountService = new AccountService(this);

    }

    public int getBankId() {
        return bankId;
    }

    public String getName() {
        return name;
    }

    public double getCommissionForCreditAccount() {
        return commissionForCreditAccount;
    }

    public double getInterestRateForDebitAccount() {
        return interestRateForDebitAccount;
    }

    public double getInterestRatesForSavingsAccount() {
        return interestRatesForSavingsAccount;
    }

    public double getLimitForSuspiciousAccount() {
        return limitForSuspiciousAccount;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public List<Client> getClients() {
        return clients;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public int openAccount(Client client, AccountType accountType, int yearsDuration) throws SQLException {
        int id;
        switch (accountType) {
            case SAVINGS:
                id = AccountDatabase.add(client.getClientId(), this.bankId, accountType, this.limitForSuspiciousAccount, this.interestRatesForSavingsAccount);
                AccountDatabase.alterEndDate(id, LocalDate.now().plusYears(yearsDuration));
                return id;
            case DEBIT:
                id = AccountDatabase.add(client.getClientId(), this.bankId, accountType, this.limitForSuspiciousAccount, this.interestRateForDebitAccount);
                return id;
            case CREDIT:
                id = AccountDatabase.add(client.getClientId(), this.bankId, accountType, this.limitForSuspiciousAccount, this.commissionForCreditAccount);
                AccountDatabase.alterCreditLimit(id, this.creditLimit);
                return id;
            default:
                throw new IllegalStateException("Unexpected value: " + accountType);
        }
    }

    public double getInterestRateForAccountType(AccountType accountType, double balance) {
        switch (accountType) {
            case SAVINGS:
                if (balance < 50000) {
                    return interestRatesForSavingsAccount;
                }
                else if (balance < 100000) {
                    return interestRatesForSavingsAccount * 1.5;
                }
                else {
                    return interestRatesForSavingsAccount * 2;
                }
            case DEBIT:
                return interestRateForDebitAccount;
            case CREDIT:
                return commissionForCreditAccount;
            default:
                throw new IllegalArgumentException("Неизвестный тип счета: " + accountType);
        }
    }

    public void addInterestToAllAccounts() {
        for (Account account : accounts) {
            account.addInterest();
        }
    }
}
