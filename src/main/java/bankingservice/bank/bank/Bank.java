package bankingservice.bank.bank;

import bankingservice.bank.account.*;
import bankingservice.bank.client.Client;
import bankingservice.database.AccountDatabase;
import bankingservice.database.ClientDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

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

    public double getLimitForSuspiciousAccount() {
        return limitForSuspiciousAccount;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public String toString() {
        return id + ". " + name +
                ": creditCommission=" + commissionForCreditAccount +
                ", debitInterestRate=" + interestRateForDebitAccount +
                ", savingInterestRate=" + interestRatesForSavingsAccount +
                ", creditLimit=" + creditLimit +
                ", suspiciousLimit=" + limitForSuspiciousAccount;
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

    public void deposit(int accountId, double amount) throws SQLException, SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        var account = AccountDatabase.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }
        if (account.getBankId() != this.id) {
            throw new IllegalArgumentException("Account not found in bank");
        }

        account.deposit(amount);
    }

    public void withdraw(int accountId, double amount) throws SQLException, SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        var account = AccountDatabase.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Account does not exist");
        }
        if (account.getBankId() != this.id) {
            throw new IllegalArgumentException("Account not found in bank");
        }
        if (account.isSuspicious() && amount > limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Cannot withdraw from suspicious account");
        }

        account.withdraw(amount);
    }

    public void addInterest() throws SQLException {
        var accounts = AccountDatabase.getAccountsForBank(this.id);
        for (var account : accounts) {
            account.addInterest();
        }
    }
}
