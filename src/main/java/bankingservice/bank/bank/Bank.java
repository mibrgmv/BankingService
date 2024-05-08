package bankingservice.bank.bank;

import bankingservice.bank.account.*;
import bankingservice.bank.client.Client;
import bankingservice.database.AccountDatabase;
import java.sql.SQLException;
import java.time.LocalDate;

public class Bank {

    private int id;
    private String name;
    private double commissionForCreditAccount;
    private double interestRateForDebitAccount;
    private double interestRatesForSavingsAccount;
    private double creditLimit;
    private double limitForSuspiciousAccount;

    public Bank(int bankId, String name, double interestRatesForSavingsAccount, double interestRateForDebitAccount, double commissionForCreditAccount, double creditLimit, double limitForSuspiciousAccount) {
        this.id = bankId;
        this.name = name;
        this.commissionForCreditAccount = commissionForCreditAccount;
        this.interestRateForDebitAccount = interestRateForDebitAccount;
        this.interestRatesForSavingsAccount = interestRatesForSavingsAccount;
        this.creditLimit = creditLimit;
        this.limitForSuspiciousAccount = limitForSuspiciousAccount;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "\"" + name + "\"" +
                ", Credit Commission = " + commissionForCreditAccount +
                ", Debit Interest Rate = " + interestRateForDebitAccount +
                ", Savings Interest Rate = " + interestRatesForSavingsAccount +
                ", Credit Limit = " + creditLimit +
                ", Limit for Suspicious Accounts = " + limitForSuspiciousAccount;
    }

    public int openAccount(Client client, AccountType accountType, int yearsDuration) throws SQLException {
        int id;
        switch (accountType) {
            case SAVINGS:
                id = AccountDatabase.add(client.id(), this.id, accountType, this.limitForSuspiciousAccount, this.interestRatesForSavingsAccount);
                AccountDatabase.alterEndDate(id, LocalDate.now().plusYears(yearsDuration));
                return id;
            case DEBIT:
                id = AccountDatabase.add(client.id(), this.id, accountType, this.limitForSuspiciousAccount, this.interestRateForDebitAccount);
                return id;
            case CREDIT:
                id = AccountDatabase.add(client.id(), this.id, accountType, this.limitForSuspiciousAccount, this.commissionForCreditAccount);
                AccountDatabase.alterCreditLimit(id, this.creditLimit);
                return id;
            default:
                throw new IllegalStateException("Unexpected value: " + accountType);
        }
    }

    public void addInterest() throws SQLException {
        var accounts = AccountDatabase.getAccountsForBank(this.id);
        for (var account : accounts) {
            account.addInterest();
        }
    }
}
