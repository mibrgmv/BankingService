package bankingservice.bank.account;

import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;

public class CreditAccount extends Account {

    private double creditLimit;

    public CreditAccount(int id, int ownerId, int bankId, double balance, boolean isSuspicious, double limitForSuspiciousAccount, double interestRate, double creditLimit) {
        super(id, ownerId, bankId, balance, isSuspicious, AccountType.CREDIT, limitForSuspiciousAccount, interestRate);
        this.creditLimit = creditLimit;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException, SQLException {
        if (balance + creditLimit < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.withdraw(amount);
    }

    @Override
    public void transfer(int destinationId, double amount) throws SuspiciousLimitExceedingException, SQLException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        if (balance + creditLimit < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.transfer(destinationId, amount);
    }
}
