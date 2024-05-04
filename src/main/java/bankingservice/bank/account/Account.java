package bankingservice.bank.account;

import bankingservice.bank.service.AccountInterface;
import bankingservice.database.AccountDatabase;
import bankingservice.database.TransactionDatabase;
import bankingservice.exceptions.InsufficientFundsException;
import bankingservice.exceptions.SuspiciousLimitExceedingException;
import bankingservice.exceptions.WithdrawalBeforeEndDateException;

import java.sql.SQLException;
import java.time.LocalDate;

public abstract class Account implements AccountInterface {

    int id;
    int ownerId;
    int bankId;
    double balance;
    boolean isSuspicious;
    AccountType accountType;
    double limitForSuspiciousAccount;
    double interestRate;

    public Account(int id, int ownerId, int bankId, double balance, boolean isSuspicious, AccountType accountType, double limitForSuspiciousAccount, double interestRate) {
        this.id = id;
        this.ownerId = ownerId;
        this.bankId = bankId;
        this.balance = balance;
        this.isSuspicious = isSuspicious;
        this.accountType = accountType;
        this.limitForSuspiciousAccount = limitForSuspiciousAccount;
        this.interestRate = interestRate;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public int getBankId() {
        return bankId;
    }

    public boolean isSuspicious() {
        return isSuspicious;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", ownerId=" + ownerId +
                ", bankId=" + bankId +
                ", isSuspicious=" + isSuspicious +
                ", accountType=" + accountType +
                ", balance=" + balance +
                ", limitForSuspiciousAccount=" + limitForSuspiciousAccount +
                ", interestRate=" + interestRate +
                '}';
    }

    public void deposit(double amount) throws SQLException {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }

        balance += amount;
        AccountDatabase.alterBalance(id, balance);
        TransactionDatabase.add(id, bankId, amount, TransactionType.DEPOSIT, LocalDate.now());
    }

    public void withdraw(double amount) throws SuspiciousLimitExceedingException, InsufficientFundsException, WithdrawalBeforeEndDateException, SQLException {
        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (this.isSuspicious && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }

        balance -= amount;
        AccountDatabase.alterBalance(id, balance);
        TransactionDatabase.add(id, bankId, amount, TransactionType.WITHDRAW, LocalDate.now());
    }

    public void transfer(int destinationId, double amount) throws SuspiciousLimitExceedingException, SQLException, InsufficientFundsException, WithdrawalBeforeEndDateException {
        var destinationAccount = AccountDatabase.findById(destinationId);

        if (amount < 0) {
            throw new IllegalArgumentException("Invalid amount");
        }
        if (destinationAccount == null) {
            throw new IllegalArgumentException("Cannot find destination account");
        }
        if (this.isSuspicious && amount > this.limitForSuspiciousAccount) {
            throw new SuspiciousLimitExceedingException("Account is suspicious. Withdrawal amount above allowed limit");
        }

        balance -= amount;
        destinationAccount.balance += amount;
        TransactionDatabase.add(id, bankId, amount, TransactionType.TRANSFER, LocalDate.now());
        TransactionDatabase.add(destinationAccount.id, bankId, amount, TransactionType.RECEIVE, LocalDate.now());
    }

    public void addInterest() throws SQLException {
        double interestAmount = balance * interestRate;
        balance += interestAmount;
        AccountDatabase.alterBalance(id, balance);
        TransactionDatabase.add(id, bankId, interestAmount, TransactionType.INTEREST, LocalDate.now());
    }

//    // todo перенести в банк -> банк имеет контроль и над клиентами, и над аккаунтами
//    public void checkSuspiciousActivity() {
//        if (!isSuspicious && (!owner.hasAddress() || !owner.hasPassport())) {
//            isSuspicious = true;
//        } else if (isSuspicious && (owner.hasAddress() && owner.hasPassport())) {
//            isSuspicious = false;
//        }
//    }
}
