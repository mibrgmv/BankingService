package bankingservice.bank.account;

import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.util.ArrayList;
import java.util.List;

public abstract class Account {

    private int accountId;
    private Client owner;
    private Bank bank;
    private List<Transaction> transactions;
    private boolean isSuspicious;
    private AccountType accountType;
    double balance;
    double limitForSuspiciousAccount;
    double interestRate;

    public Account(int accountId, Client owner, Bank bank, double balance, AccountType accountType) {
        this.accountId = accountId;
        this.owner = owner;
        this.bank = bank;
        this.balance = balance;
        this.transactions = new ArrayList<>();
        this.isSuspicious = false;
        this.limitForSuspiciousAccount = bank.getLimitForSuspiciousAccount();
        this.accountType = accountType;
        this.interestRate = bank.getInterestRateForAccountType(this.accountType, this.balance);
    }

    public int getAccountId() {
        return accountId;
    }

    public Client getOwner() {
        return owner;
    }

    public Bank getBank() {
        return bank;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public boolean isSuspicious() {
        return isSuspicious;
    }

    public double getLimitForSuspiciousAccount() {
        return limitForSuspiciousAccount;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void checkSuspiciousActivity() {
        if (!isSuspicious && (!owner.hasAddress() || !owner.hasPassport())) {
            isSuspicious = true;
        } else if (isSuspicious && (owner.hasAddress() && owner.hasPassport())) {
            isSuspicious = false;
        }
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException;

    public abstract void transfer(Account destinationAccount, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException;

    public abstract void addInterest();
}
