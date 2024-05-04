package bankingservice.bank.service;

import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;

public interface AccountInterface {

    void deposit(double amount) throws SQLException;

    void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException, SQLException;

    void transfer(int destinationId, double amount) throws SuspiciousLimitExceedingException, SQLException, InsufficientFundsException, WithdrawalBeforeEndDateException;

    void addInterest() throws SQLException;
}
