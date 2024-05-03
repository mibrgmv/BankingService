package bankingservice.bank.account;

import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;

public class DebitAccount extends Account {

    public DebitAccount(int id, int ownerId, int bankId, double balance, boolean isSuspicious, double limitForSuspiciousAccount, double interestRate) {
        super(id, ownerId, bankId, balance, isSuspicious, AccountType.DEBIT, limitForSuspiciousAccount, interestRate);
    }

    @Override
    public void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException, SQLException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.withdraw(amount);
    }

    @Override
    public void transfer(int destinationId, double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, SQLException, WithdrawalBeforeEndDateException {
        if (balance < amount) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }

        super.transfer(destinationId, amount);
    }
}
