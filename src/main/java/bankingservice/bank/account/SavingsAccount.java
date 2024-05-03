package bankingservice.bank.account;

import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;
import java.time.LocalDate;

public class SavingsAccount extends Account {

    private final LocalDate endDate;

    public SavingsAccount(int id, int ownerId, int bankId, double balance, boolean isSuspicious, double limitForSuspiciousAccount, double interestRate, LocalDate endDate) {
        super(id, ownerId, bankId, balance, isSuspicious, AccountType.SAVINGS, limitForSuspiciousAccount, interestRate);
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public void withdraw(double amount) throws WithdrawalBeforeEndDateException, InsufficientFundsException, SuspiciousLimitExceedingException, SQLException {
        if (LocalDate.now().isBefore(endDate)) {
            throw new WithdrawalBeforeEndDateException("Account is unavailable for withdrawal");
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.withdraw(amount);
    }

    @Override
    public void transfer(int destinationId, double amount) throws InsufficientFundsException, SuspiciousLimitExceedingException, WithdrawalBeforeEndDateException, SQLException {
        if (LocalDate.now().isBefore(endDate)) {
            throw new WithdrawalBeforeEndDateException("Account is unavailable for withdrawal");
        }
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.transfer(destinationId, amount);
    }
}
