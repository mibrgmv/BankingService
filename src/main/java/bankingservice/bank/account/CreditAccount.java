package bankingservice.bank.account;

import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;

import java.sql.SQLException;
import java.time.LocalDate;

public class CreditAccount extends Account {

    private double creditLimit;

    public CreditAccount(int accountId, Client owner, Bank bank, double balance, double creditLimit) {
        super(accountId, owner, bank, balance, AccountType.CREDIT);
        this.creditLimit = creditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (this.isSuspicious() && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }
        if (balance + creditLimit < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        balance -= amount;
//        addTransaction(new Transaction(TransactionType.WITHDRAW, amount, LocalDate.now()));
    }

    @Override
    public void transfer(Account destinationAccount, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException {
        if (amount < 0 || destinationAccount == null) {
            throw new IllegalArgumentException("Invalid amount or destination account");
        }
        if (this.isSuspicious() && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }
        if (balance + creditLimit < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        balance -= amount;
        destinationAccount.balance += amount;
    }

    @Override
    public void addInterest() {
        double interestAmount = -balance * interestRate / 100;
        balance += interestAmount;

        try {
            TransactionDatabase.add(this.getId(), this.getBank().getBankId(), interestAmount, TransactionType.INTEREST, LocalDate.now());
        } catch (SQLException e) {
            // TODO LOG
        }
    }
}
