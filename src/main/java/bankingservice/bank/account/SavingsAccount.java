package bankingservice.bank.account;

import bankingservice.bank.bank.Bank;
import bankingservice.bank.client.Client;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.time.LocalDate;

public class SavingsAccount extends Account {

    private final LocalDate endDate;

    public SavingsAccount(int accountId, Client owner, Bank bank, double balance, LocalDate endDate) {
        super(accountId, owner, bank, balance, AccountType.SAVINGS);
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public void withdraw(double amount) throws WithdrawalBeforeEndDateException, InsufficientFundsException, SuspiciousLimitExceedingException {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (LocalDate.now().isBefore(endDate)) {
            throw new WithdrawalBeforeEndDateException("Account is unavailable for withdrawal");
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        if (this.isSuspicious() && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }

        balance -= amount;
//        addTransaction(new Transaction(TransactionType.WITHDRAW, amount, LocalDate.now()));
    }

    @Override
    public void transfer(Account destinationAccount, double amount) throws WithdrawalBeforeEndDateException, InsufficientFundsException, SuspiciousLimitExceedingException {
        if (amount < 0 || destinationAccount == null) {
            throw new IllegalArgumentException("Invalid amount or destination account");
        }
        if (LocalDate.now().isBefore(endDate)) {
            throw new WithdrawalBeforeEndDateException("Account is unavailable for withdrawal");
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        if (this.isSuspicious() && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }

        balance -= amount;
        destinationAccount.balance += amount;
//        addTransaction(new Transaction(TransactionType.TRANSFER, amount, destinationAccount.getAccountId(), LocalDate.now()));
//        destinationAccount.addTransaction(new Transaction(TransactionType.RECEIVE, amount, getAccountId(), LocalDate.now()));
    }
}
