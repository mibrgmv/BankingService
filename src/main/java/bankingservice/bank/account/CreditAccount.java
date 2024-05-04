package bankingservice.bank.account;

import bankingservice.database.AccountDatabase;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;
import java.time.LocalDate;

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

    @Override
    public void addInterest() throws SQLException {
        if (balance < 0) {
            double interestAmount = balance * interestRate;
            balance -= interestAmount;
            AccountDatabase.alterBalance(id, balance);
            TransactionDatabase.add(id, bankId, interestAmount, TransactionType.INTEREST, LocalDate.now());
        }
    }
}
