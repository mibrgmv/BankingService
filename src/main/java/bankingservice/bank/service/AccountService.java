package bankingservice.bank.service;

import bankingservice.bank.account.Account;
import bankingservice.bank.bank.Bank;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AccountService {

    private Bank bank;
    private List<Integer> usedIds = new ArrayList<>();

    public AccountService(Bank bank) {
        this.bank = bank;
    }

    public int generateAccountId() {
        int id;
        do {
            id = Math.abs(new Random().nextInt(1000));
        } while (usedIds.contains(id));

        usedIds.add(id);
        return id;
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
