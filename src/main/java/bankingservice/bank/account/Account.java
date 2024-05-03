package bankingservice.bank.account;

import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;
import java.time.LocalDate;

public abstract class Account {

    private int id;
    private Client owner;
    private Bank bank;
    private boolean isSuspicious;
    private AccountType accountType;
    double balance;
    double limitForSuspiciousAccount;
    double interestRate;

    public Account(int accountId, Client owner, Bank bank, double balance, AccountType accountType) {
        this.id = accountId;
        this.owner = owner;
        this.bank = bank;
        this.balance = balance;
        this.isSuspicious = false;
        this.limitForSuspiciousAccount = bank.getLimitForSuspiciousAccount();
        this.accountType = accountType;
    }

    public int getId() {
        return id;
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

    public boolean isSuspicious() {
        return isSuspicious;
    }

    public double getLimitForSuspiciousAccount() {
        return limitForSuspiciousAccount;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void checkSuspiciousActivity() {
        if (!isSuspicious && (!owner.hasAddress() || !owner.hasPassport())) {
            isSuspicious = true;
        } else if (isSuspicious && (owner.hasAddress() && owner.hasPassport())) {
            isSuspicious = false;
        }
    }

    @Override
    public String toString() {
        return "id: " + this.getId() + '\n' +
                "owner: " + this.getOwner().getFirstName() + ' ' + this.getOwner().getLastName() + '\n' +
                "bank: " + this.getBank().getName() + '\n' +
                "type: " + this.getAccountType() + '\n' +
                "balance: " + this.getBalance();
    }

    public void deposit(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        balance += amount;
        try {
            TransactionDatabase.add(this.getId(), this.bank.getBankId(), amount, TransactionType.DEPOSIT, LocalDate.now());
        } catch (SQLException e) {
            // LOG
            System.out.println(e.getMessage());
        }
    }

    public abstract void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException;

    public abstract void transfer(Account destinationAccount, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException;

    public void addInterest() {
        double interestAmount = balance * interestRate / 100;
        balance += interestAmount;

        try {
            TransactionDatabase.add(this.id, this.bank.getBankId(), interestAmount, TransactionType.INTEREST, LocalDate.now());
        } catch (SQLException e) {
            // LOG
        }
    }
}
