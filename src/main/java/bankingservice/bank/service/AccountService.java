package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.bank.bank.Bank;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

public class AccountService {

    private Bank bank;

    public AccountService(Bank bank) {
        this.bank = bank;
    }

    public void deposit(Account account, double amount) {
        account.deposit(amount);
    }

    public void withdraw(Account account, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        account.checkSuspiciousActivity();
        account.withdraw(amount);
    }

    public void transfer(Account sourceAccount, Account destinationAccount, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        sourceAccount.checkSuspiciousActivity();
        sourceAccount.transfer(destinationAccount, amount);
    }

    public void addInterest(Account account) {
        account.addInterest();
    }
}
